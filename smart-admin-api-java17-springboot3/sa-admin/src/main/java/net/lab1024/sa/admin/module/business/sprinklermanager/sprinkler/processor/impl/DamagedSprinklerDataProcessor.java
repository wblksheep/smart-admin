package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeChineseEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.DamagedSprinklerImportForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerImportForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.DamagedSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 破损仓喷头数据处理实现类
 * 处理导入的破损仓喷头数据，进行数据校验、分仓存储及主表状态更新
 */
@Component("damaged") // 通过组件名称标识处理器类型
public class DamagedSprinklerDataProcessor implements DataProcessor<DamagedSprinklerImportForm> {

    // 使用MyBatis-Plus仓库接口
    @Resource
    private SprinklerRepository sprinklerRepository; // 喷头主表数据访问层

    @Resource
    private DamagedSprinklerRepository damagedSprinklerRepository; // 破损仓数据访问层

    /**
     * 核心处理方法
     *
     * @param importVOs 前端传入的创建表单列表
     * @return 处理结果响应
     */
    @Override
    public ResponseDTO<String> process(List<DamagedSprinklerImportForm> importVOs) {
        // 1. 空数据校验（基础校验优化）
        if (CollectionUtils.isEmpty(importVOs)) {
            return ResponseDTO.userErrorParam("导入数据为空");
        }

        try {
            // 2. 数据预处理分区（使用Stream分区优化处理效率）
            Map<Boolean, List<DamagedSprinklerImportForm>> preprocessed = importVOs.stream()
                    .collect(Collectors.partitioningBy(
                            form -> StringUtils.isNotBlank(form.getSprinklerSerial())
                                    && form.getStatus().equals(RepositorySprinklerTypeChineseEnum.DAMAGED_REPOSITORY.getDesc()))
                    );
            List<DamagedSprinklerImportForm> validForms = preprocessed.get(true);
            List<DamagedSprinklerImportForm> invalidForms = preprocessed.get(false);

            // 3. 收集无效序列号（并行流优化处理大数据量场景）
            Set<String> invalidSerials = collectInvalidSerials(invalidForms);

            // 4. 批量查询主表数据（优化点：合并查询减少数据库IO）
            Set<String> serials = validForms.stream()
                    .map(DamagedSprinklerImportForm::getSprinklerSerial)
                    .collect(Collectors.toSet());
            Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);

            // 5. 主表校验及破损仓重复校验（双重校验优化）
            // 5.1 批量查询已存在的序列号（优化点：合并查询条件）
            Set<String> existingSerials = getExistingSprinklerSerials(validForms);

            // 5.2 过滤有效数据（使用Map快速查找优化性能）
            List<DamagedSprinklerImportForm> filteredForms = validForms.stream()
                    .filter(form -> {
                        SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
                        return mainRecord != null && !existingSerials.contains(form.getSprinklerSerial());
                    })
                    .toList();

            // 6. 实体转换（使用Bean拷贝工具优化代码简洁性）
            List<DamagedSprinklerEntity> entities = filteredForms.stream()
                    .map(form -> convertToWarehouseEntity(form, mainTableMap))
                    .filter(entity -> entity.getSprinklerId() != null)
                    .toList();

            // 7. 准备主表更新数据（状态更新优化）
            List<SprinklerEntity> mainTableUpdates = new ArrayList<>();
            filteredForms.forEach(form -> {
                SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
                if (mainRecord.getStatus() != RepositorySprinklerTypeEnum.DAMAGED_REPOSITORY.getValue().byteValue()) {
                    mainRecord.setStatus(RepositorySprinklerTypeEnum.DAMAGED_REPOSITORY.getValue().byteValue());
                    mainTableUpdates.add(mainRecord);
                }
            });

            // 7.1 收集需要更新的主表ID（ID提取优化）
            List<Long> mainIdsToUpdate = mainTableUpdates.stream()
                    .map(SprinklerEntity::getSprinklerId)
                    .toList();

            // 8. 批量操作（数据库操作优化）
            // 8.1 批量更新主表状态（使用UpdateWrapper优化更新效率）
            if (!mainTableUpdates.isEmpty()) {
                UpdateWrapper<SprinklerEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.in("sprinkler_id", mainIdsToUpdate)
                        .set("status", RepositorySprinklerTypeEnum.DAMAGED_REPOSITORY.getValue().byteValue());
                sprinklerRepository.update(updateWrapper);
            }
            // 8.2 批量插入破损仓数据（使用MyBatis-Plus批量操作优化）
            if (!entities.isEmpty()) {
                try {
                    damagedSprinklerRepository.saveBatch(entities);
                } catch (DuplicateKeyException e) {
                    throw new BusinessException("存在重复的喷头序列号");
                }
            }

            // 9. 返回处理结果（结果信息优化）
            return ResponseDTO.okMsg(invalidSerials.isEmpty() ? "处理成功" : "处理成功，但存在无效数据");
        } catch (NullPointerException e) {
            return ResponseDTO.userErrorParam("所在仓status不能为空");
        }
    }

    /**
     * 实体转换方法（使用SmartBeanUtil优化属性拷贝）
     *
     * @param form         表单对象
     * @param mainTableMap 主表数据映射
     * @return 破损仓实体
     */
    private DamagedSprinklerEntity convertToWarehouseEntity(
            DamagedSprinklerImportForm form,
            Map<String, SprinklerEntity> mainTableMap
    ) {
        // 使用Bean拷贝工具优化属性复制
        DamagedSprinklerEntity validSprinkler = SmartBeanUtil.copy(form, DamagedSprinklerEntity.class);
        // 预定义支持的日期格式
        final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy.M.dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.d"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        );
        // 通用日期解析方法
        Function<String, LocalDate> parseDate = (dateStr) -> {
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    // 尝试下一个格式
                }
            }
            throw new BusinessException("非法日期格式: " + dateStr);
        };
        // 处理日期字段（支持动态添加新字段）
        Map<String, Consumer<LocalDate>> dateSetters = Map.of(
                "retWarehouseDate", validSprinkler::setRetWarehouseDate);
        dateSetters.forEach((fieldName, setter) -> {
            try {
                setter.accept(parseDate.apply(getDateField(form, fieldName)));
            } catch (BusinessException e) {
                throw new BusinessException(String.format("喷头序列号 %s 的%s失败: %s", form.getSprinklerSerial(), fieldName, e.getMessage()));
            }
        });
        if (!form.getStatus().equals("破损仓")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        SprinklerEntity mainEntity = mainTableMap.get(form.getSprinklerSerial());
        if (mainEntity != null) {
            validSprinkler.setSprinklerId(mainEntity.getSprinklerId());
        }
        return validSprinkler;
    }

    private String getDateField(DamagedSprinklerImportForm form, String fieldName) {
        switch (fieldName) {
            case "retWarehouseDate":
                return form.getRetWarehouseDate();
            default:
                throw new BusinessException("非法的参数：" + fieldName);
        }
    }

    /**
     * 批量获取主表数据（优化点：单次批量查询）
     *
     * @param serials 喷头序列号集合
     * @return 主表数据映射
     */
    private Map<String, SprinklerEntity> getMainTableMap(Set<String> serials) {
        if (serials.isEmpty()) {
            return Collections.emptyMap();
        }
        // 使用IN查询优化数据库访问
        return sprinklerRepository.getBaseMapper()
                .selectList(new QueryWrapper<SprinklerEntity>().in("sprinkler_serial", serials))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));
    }

    /**
     * 收集无效序列号（空值过滤优化）
     *
     * @param invalidForms 无效表单列表
     * @return 无效序列号集合
     */
    private Set<String> collectInvalidSerials(List<DamagedSprinklerImportForm> invalidForms) {
        return invalidForms.stream()
                .map(DamagedSprinklerImportForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    /**
     * 获取已存在的序列号（优化点：批量去重查询）
     *
     * @param validForms 有效表单列表
     * @return 已存在序列号集合
     */
    private Set<String> getExistingSprinklerSerials(List<DamagedSprinklerImportForm> validForms) {
        Set<String> serialsToCheck = validForms.stream()
                .map(DamagedSprinklerImportForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        if (serialsToCheck.isEmpty()) {
            return Collections.emptySet();
        }
        // 使用单次查询优化数据库访问
        return damagedSprinklerRepository.getBaseMapper()
                .selectList(new QueryWrapper<DamagedSprinklerEntity>().in("sprinkler_serial", serialsToCheck))
                .stream()
                .map(DamagedSprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toSet());
    }
}
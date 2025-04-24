package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 维修仓喷头数据处理实现类
 * 处理导入的维修仓喷头数据，进行数据校验、分仓存储及主表状态更新
 */
@Component("maintaining") // 通过组件名称标识处理器类型
public class MaintainingSprinklerDataProcessor implements DataProcessor<MaintainingSprinklerCreateForm> {

    // 使用MyBatis-Plus仓库接口
    @Resource
    private SprinklerRepository sprinklerRepository; // 喷头主表数据访问层

    @Resource
    private MaintainingSprinklerRepository maintainingSprinklerRepository; // 维修仓数据访问层

    /**
     * 核心处理方法
     * @param createVOs 前端传入的创建表单列表
     * @return 处理结果响应
     */
    @Override
    public ResponseDTO<String> process(List<MaintainingSprinklerCreateForm> createVOs) {
        // 1. 空数据校验（基础校验优化）
        if (CollectionUtils.isEmpty(createVOs)) {
            return ResponseDTO.userErrorParam("导入数据为空");
        }

        // 2. 数据预处理分区（使用Stream分区优化处理效率）
        Map<Boolean, List<MaintainingSprinklerCreateForm>> preprocessed = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        form -> StringUtils.isNotBlank(form.getSprinklerSerial())
                                && form.getStatus() == RepositorySprinklerTypeEnum.MAINTAINING_REPOSITORY.getValue().byteValue())
                );
        List<MaintainingSprinklerCreateForm> validForms = preprocessed.get(true);
        List<MaintainingSprinklerCreateForm> invalidForms = preprocessed.get(false);

        // 3. 收集无效序列号（并行流优化处理大数据量场景）
        Set<String> invalidSerials = collectInvalidSerials(invalidForms);

        // 4. 批量查询主表数据（优化点：合并查询减少数据库IO）
        Set<String> serials = validForms.stream()
                .map(MaintainingSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);

        // 5. 主表校验及维修仓重复校验（双重校验优化）
        // 5.1 批量查询已存在的序列号（优化点：合并查询条件）
        Set<String> existingSerials = getExistingSprinklerSerials(validForms);

        // 5.2 过滤有效数据（使用Map快速查找优化性能）
        List<MaintainingSprinklerCreateForm> filteredForms = validForms.stream()
                .filter(form -> {
                    SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
                    return mainRecord != null && !existingSerials.contains(form.getSprinklerSerial());
                })
                .toList();

        // 6. 实体转换（使用Bean拷贝工具优化代码简洁性）
        List<MaintainingSprinklerEntity> entities = filteredForms.stream()
                .map(form -> convertToWarehouseEntity(form, mainTableMap))
                .filter(entity -> entity.getSprinklerId() != null)
                .toList();

        // 7. 准备主表更新数据（状态更新优化）
        List<SprinklerEntity> mainTableUpdates = new ArrayList<>();
        filteredForms.forEach(form -> {
            SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
            if (mainRecord.getStatus() != RepositorySprinklerTypeEnum.MAINTAINING_REPOSITORY.getValue().byteValue()) {
                mainRecord.setStatus(RepositorySprinklerTypeEnum.MAINTAINING_REPOSITORY.getValue().byteValue());
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
                    .set("status", RepositorySprinklerTypeEnum.MAINTAINING_REPOSITORY.getValue().byteValue());
            sprinklerRepository.update(updateWrapper);
        }
        // 8.2 批量插入维修仓数据（使用MyBatis-Plus批量操作优化）
        if (!entities.isEmpty()) {
            maintainingSprinklerRepository.saveBatch(entities);
        }

        // 9. 返回处理结果（结果信息优化）
        return ResponseDTO.ok("处理成功，无效数据：" + invalidSerials);
    }

    /**
     * 实体转换方法（使用SmartBeanUtil优化属性拷贝）
     * @param form 表单对象
     * @param mainTableMap 主表数据映射
     * @return 维修仓实体
     */
    private MaintainingSprinklerEntity convertToWarehouseEntity(
            MaintainingSprinklerCreateForm form,
            Map<String, SprinklerEntity> mainTableMap
    ) {
        // 使用Bean拷贝工具优化属性复制
        MaintainingSprinklerEntity entity = SmartBeanUtil.copy(form, MaintainingSprinklerEntity.class);
        SprinklerEntity mainEntity = mainTableMap.get(form.getSprinklerSerial());
        if (mainEntity != null) {
            entity.setSprinklerId(mainEntity.getSprinklerId());
        }
        return entity;
    }

    /**
     * 批量获取主表数据（优化点：单次批量查询）
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
     * @param invalidForms 无效表单列表
     * @return 无效序列号集合
     */
    private Set<String> collectInvalidSerials(List<MaintainingSprinklerCreateForm> invalidForms) {
        return invalidForms.stream()
                .map(MaintainingSprinklerCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    /**
     * 获取已存在的序列号（优化点：批量去重查询）
     * @param validForms 有效表单列表
     * @return 已存在序列号集合
     */
    private Set<String> getExistingSprinklerSerials(List<MaintainingSprinklerCreateForm> validForms) {
        Set<String> serialsToCheck = validForms.stream()
                .map(MaintainingSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        if (serialsToCheck.isEmpty()) {
            return Collections.emptySet();
        }
        // 使用单次查询优化数据库访问
        return maintainingSprinklerRepository.getBaseMapper()
                .selectList(new QueryWrapper<MaintainingSprinklerEntity>().in("sprinkler_serial", serialsToCheck))
                .stream()
                .map(MaintainingSprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toSet());
    }
}
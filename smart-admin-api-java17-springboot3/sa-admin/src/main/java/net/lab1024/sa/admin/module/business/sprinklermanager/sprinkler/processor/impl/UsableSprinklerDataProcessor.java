package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 可用喷淋器数据处理实现类
 * 处理导入的可用喷淋器数据，进行数据校验、分仓存储及主表状态更新
 */
@Component("usable") // Spring组件，标识为可用仓处理器
public class UsableSprinklerDataProcessor implements DataProcessor<UsableSprinklerCreateForm> {

    @Resource
    private SprinklerRepository sprinklerRepository; // 喷淋器主表仓库

    @Resource
    private UsableSprinklerRepository usableSprinklerRepository; // 可用仓仓库

    /**
     * 处理导入的可用喷淋器数据
     * @param createVOs 前端传入的创建表单列表
     * @return 处理结果
     */
    @Override
    public ResponseDTO<String> process(List<UsableSprinklerCreateForm> createVOs) {
        // 1. 空数据校验
        if (CollectionUtils.isEmpty(createVOs)) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 2. 数据预处理：分区有效数据（序列号非空且状态为0）和无效数据
        Map<Boolean, List<UsableSprinklerCreateForm>> preprocessed = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        form -> StringUtils.isNotBlank(form.getSprinklerSerial())
                                && form.getStatus() == 0)
                );
        List<UsableSprinklerCreateForm> validForms = preprocessed.get(true); // 有效数据
        List<UsableSprinklerCreateForm> invalidForms = preprocessed.get(false); // 无效数据

        // 3. 收集无效序列号（后续可返回给前端提示）
        Set<String> invalidSerials = collectInvalidSerials(invalidForms);

        // 4. 批量查询主表数据（减少数据库IO）
        Set<String> serials = validForms.stream()
                .map(UsableSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);

        // 5. 主表校验及可用仓重复校验（优化点：合并校验逻辑，减少数据库查询次数）
        // 5.1 批量查询已存在的可用仓序列号
        Set<String> existingSerials = getExistingSprinklerSerials(validForms);

        // 5.2 过滤有效数据：主表存在且可用仓不重复
        List<UsableSprinklerCreateForm> filteredForms = validForms.stream()
                .filter(form -> {
                    SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
                    return mainRecord != null && !existingSerials.contains(form.getSprinklerSerial());
                })
                .collect(Collectors.toList());

        // 6. 转换实体对象（可用仓）
        List<UsableSprinklerEntity> entities = filteredForms.stream()
                .map(this::convertToWarehouseEntity)
                .collect(Collectors.toList());

        // 7. 准备主表状态更新（状态非0则更新为0）
        List<SprinklerEntity> mainTableUpdates = new ArrayList<>();
        filteredForms.forEach(form -> {
            SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
            if (mainRecord.getStatus() != 0) {
                mainRecord.setStatus((byte) 0);
                mainTableUpdates.add(mainRecord);
            }
        });

        // 8. 批量持久化操作
        if (!mainTableUpdates.isEmpty()) {
            sprinklerRepository.saveOrUpdateBatch(mainTableUpdates);
        }
        if (!entities.isEmpty()) {
            usableSprinklerRepository.saveBatch(entities);
        }

        // 9. 返回结果（示例简化，实际应包含无效数据信息）
        return ResponseDTO.ok("处理成功，无效数据：" + invalidSerials);
    }

    /**
     * 转换表单为可用仓实体（使用工具类进行属性拷贝）
     */
    public UsableSprinklerEntity convertToWarehouseEntity(UsableSprinklerCreateForm form) {
        return SmartBeanUtil.copy(form, UsableSprinklerEntity.class);
    }

    /**
     * 批量查询主表数据映射（优化关键点：减少IO次数）
     */
    private Map<String, SprinklerEntity> getMainTableMap(Set<String> serials) {
        if (serials.isEmpty()) {
            return Collections.emptyMap();
        }
        return sprinklerRepository.getBaseMapper()
                .selectList(new QueryWrapper<SprinklerEntity>().in("sprinkler_serial", serials))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));
    }

    /**
     * 收集无效表单中的序列号（过滤空值）
     */
    private Set<String> collectInvalidSerials(List<UsableSprinklerCreateForm> invalidForms) {
        return invalidForms.stream()
                .map(UsableSprinklerCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    /**
     * 批量查询已存在的可用仓序列号（优化关键点：将N次查询合并为1次）
     */
    private Set<String> getExistingSprinklerSerials(List<UsableSprinklerCreateForm> validForms) {
        Set<String> serialsToCheck = validForms.stream()
                .map(UsableSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        if (serialsToCheck.isEmpty()) {
            return Collections.emptySet();
        }
        return usableSprinklerRepository.getBaseMapper()
                .selectList(new QueryWrapper<UsableSprinklerEntity>().in("sprinkler_serial", serialsToCheck))
                .stream()
                .map(UsableSprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toSet());
    }
}
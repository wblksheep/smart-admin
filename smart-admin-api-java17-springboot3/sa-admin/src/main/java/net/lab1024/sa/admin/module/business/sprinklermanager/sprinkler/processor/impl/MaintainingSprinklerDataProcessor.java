package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 可用仓喷头数据处理实现类
 * 处理导入的可用仓喷头数据，进行数据校验、分仓存储及主表状态更新
 */
@Component("maintaining")
public class MaintainingSprinklerDataProcessor implements DataProcessor<MaintainingSprinklerCreateForm> {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private MaintainingSprinklerRepository machineSprinklerRepository;

    public MaintainingSprinklerDataProcessor(SprinklerRepository sprinklerRepository) {
        this.sprinklerRepository = sprinklerRepository;
    }

    /**
     * 处理导入的可用仓喷头数据
     *
     * @param createVOs 前端传入的创建表单列表
     * @return 处理结果
     */
    @Override
    public ResponseDTO<String> process(List<MaintainingSprinklerCreateForm> createVOs) {
        // 1. 空数据校验
        if (CollectionUtils.isEmpty(createVOs)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "导入数据为空");
        }

        // 2. 数据预处理：分区有效数据（序列号非空且状态为0）和无效数据
        Map<Boolean, List<MaintainingSprinklerCreateForm>> preprocessed = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        form-> StringUtils.isNotBlank(form.getSprinklerSerial())
                                && form.getStatus() == 2)
                );
        List<MaintainingSprinklerCreateForm> validForms = preprocessed.get(true);//有效数据
        List<MaintainingSprinklerCreateForm> invalidForms = preprocessed.get(false);//无效数据

        // 3. 收集无效序列号（后续可返回给前端提示）
        Set<String> invalidSerials = collectInvalidSerials(invalidForms);

        // 4. 批量查询主表数据（减少数据库IO）
        Set<String> serials = validForms.stream()
                .map(MaintainingSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);
        // 5. 主表校验及可用仓重复校验（优化点：合并校验逻辑，减少数据库查询次数）
        // 5.1 批量查询已存在的可用仓序列号
        Set<String> existingSerials = getExistingSprinklerSerials(validForms);

        // 5.2 过滤有效数据：主表存在且可用仓不重复
        List<MaintainingSprinklerCreateForm> filteredForms = validForms.stream()
                .filter(form->{
                    SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
                    return mainRecord != null && !existingSerials.contains(form.getSprinklerSerial());
                })
                .toList();

        // 6. 转换实体对象（可用仓）
        List<MaintainingSprinklerEntity> entities = filteredForms.stream()
                .map(this::convertToWarehouseEntity)
                .toList();

        // 7. 准备主表状态更新（状态非0则更新为0）
        List<SprinklerEntity> mainTableUpdates = new ArrayList<>();
        filteredForms.forEach(form->{
            SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
            if(mainRecord.getStatus() != 2){
                mainRecord.setStatus((byte) 2);
                mainTableUpdates.add(mainRecord);
            }
        });

        // 7.1：收集需要更新的主表ID和状态
        List<Long> mainIdsToUpdate = mainTableUpdates.stream()
                .map(SprinklerEntity::getSprinklerId)
                .toList();

        //8：批量更新状态字段
        if (!mainIdsToUpdate.isEmpty()) {
            UpdateWrapper<SprinklerEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("sprinkler_id", mainIdsToUpdate)  // 根据主键ID批量匹配
                    .set("status", 2);           // 仅更新status字段
            sprinklerRepository.update(updateWrapper);
        }
        if(!entities.isEmpty()){
            machineSprinklerRepository.saveBatch(entities);
        }

        // 9. 返回结果（示例简化，实际应包含无效数据信息）
        return ResponseDTO.ok("处理成功，无效数据：" + invalidSerials);
    }

    /**
     * 转换表单为可用仓实体（使用工具类进行属性拷贝）
     */
    private MaintainingSprinklerEntity convertToWarehouseEntity(MaintainingSprinklerCreateForm form) {
        return SmartBeanUtil.copy(form, MaintainingSprinklerEntity.class);
    }

    /**
     * 批量查询已存在的可用仓序列号（优化关键点：将N次查询合并为1次）
     */
    private Set<String> getExistingSprinklerSerials(List<MaintainingSprinklerCreateForm> validForms) {
        Set<String> serialsToCheck = validForms.stream()
                .map(MaintainingSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        if(serialsToCheck.isEmpty()){
            return Collections.emptySet();
        }
        return machineSprinklerRepository.getBaseMapper()
                .selectList(new QueryWrapper<MaintainingSprinklerEntity>().in("sprinkler_serial", serialsToCheck))
                .stream()
                .map(MaintainingSprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toSet());
    }

    /**
     * 批量查询主表数据映射（优化关键点：减少IO次数）
     */
    private Map<String, SprinklerEntity> getMainTableMap(Set<String> serials) {
        if(serials.isEmpty()){
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
    private Set<String> collectInvalidSerials(List<MaintainingSprinklerCreateForm> invalidForms) {
        return invalidForms.stream()
                .map(MaintainingSprinklerCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }
}

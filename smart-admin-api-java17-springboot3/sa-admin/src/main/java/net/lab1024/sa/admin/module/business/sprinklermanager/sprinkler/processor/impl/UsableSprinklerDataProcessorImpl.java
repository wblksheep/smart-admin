package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.ValidateList;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.assertj.core.util.Arrays;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("usable")
public class UsableSprinklerDataProcessorImpl implements DataProcessor<UsableSprinklerCreateForm> {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private UsableSprinklerRepository usableSprinklerRepository;

    @Override
    public ResponseDTO<String> process(List<UsableSprinklerCreateForm> createVOs) {

        if (CollectionUtils.isEmpty(createVOs)) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 预处理阶段：过滤无效数据
        Map<Boolean, List<UsableSprinklerCreateForm>> preprocessed = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        form -> StringUtils.isNotBlank(form.getSprinklerSerial())
                                && form.getStatus()==0)
                );

        List<UsableSprinklerCreateForm> validForms = preprocessed.get(true);
        Set<String> invalidSerials = collectInvalidSerials(preprocessed.get(false));
        // 批量查询主表信息（减少IO次数）
        Set<String> serials = validForms.stream()
                .map(UsableSprinklerCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);
        // 分组处理（状态校验+分仓处理）
        Map<Byte, List<UsableSprinklerCreateForm>> statusGroups = validForms.stream()
                .filter(form -> validateMainRecord(form, mainTableMap)) // 主表存在性校验
                .collect(Collectors.groupingBy(UsableSprinklerCreateForm::getStatus));
        // 分仓插入 & 准备主表更新
        List<SprinklerEntity> mainTableUpdates = new ArrayList<>();

        List<UsableSprinklerCreateForm> forms = statusGroups.get((byte) 0);

        List<UsableSprinklerEntity> entities = null;
        // 转换仓库实体
        if(forms!=null && !forms.isEmpty()){
            entities = forms.stream()
                    .map(this::convertToWarehouseEntity)
                    .collect(Collectors.toList());
        }

        // 准备主表更新
        forms.forEach(form -> {
            SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
            if (mainRecord.getStatus() != 0) {
                mainRecord.setStatus((byte) 0);
                mainTableUpdates.add(mainRecord);
            }
        });
        // 批量操作阶段
        if (!mainTableUpdates.isEmpty()) {
            sprinklerRepository.saveOrUpdateBatch(mainTableUpdates);
        }
        // 遍历每个仓库类型进行数据插入
        // 通过工厂模式获取对应仓库的Mapper（先完成，再优化）
        if (entities!=null && !entities.isEmpty()) {
            usableSprinklerRepository.saveBatch(entities);
        }

//        // 构建包含详细处理结果的数据传输对象
//        return buildResult(invalidSerials, mainTableMap, statusGroups);
        return ResponseDTO.ok();
    }

    public UsableSprinklerEntity convertToWarehouseEntity(UsableSprinklerCreateForm form){
        return SmartBeanUtil.copy(form, UsableSprinklerEntity.class);
    }

    // 主表记录校验（包含状态有效性）
    private boolean validateMainRecord(UsableSprinklerCreateForm form,
                                       Map<String, SprinklerEntity> mainTableMap) {
        // 主表存在性校验
        SprinklerEntity mainRecord = mainTableMap.get(form.getSprinklerSerial());
        if (mainRecord == null) return false;

        //检查可用仓是否已存在相同序列号的记录
        boolean exists = usableSprinklerRepository.existsBySprinklerSerial(
                form.getSprinklerSerial()
        );
        return !exists;
    }

    // 获取主表记录映射（批量查询优化）
    private Map<String, SprinklerEntity> getMainTableMap(Set<String> serials) {
        if (serials.isEmpty()) return Collections.emptyMap();

        return sprinklerRepository.getBaseMapper().selectList(
                new QueryWrapper<SprinklerEntity>()
                        .in("sprinkler_serial", serials)
        ).stream().collect(Collectors.toMap(
                SprinklerEntity::getSprinklerSerial,
                Function.identity()
        ));
    }

    private Set<String> collectInvalidSerials(List<UsableSprinklerCreateForm> usableSprinklerCreateForms) {
        return usableSprinklerCreateForms.stream().map(form->form.getSprinklerSerial()).collect(Collectors.toSet());
    }



}

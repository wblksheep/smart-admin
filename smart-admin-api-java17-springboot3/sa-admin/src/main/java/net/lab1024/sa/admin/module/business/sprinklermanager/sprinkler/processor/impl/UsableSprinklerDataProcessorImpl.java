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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("usable")
public class UsableSprinklerDataProcessorImpl implements DataProcessor<UsableSprinklerCreateForm> {

    private static final Set<Integer> VALID_STATUS_SET = Set.of(0);

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
        return ResponseDTO.ok();


    }

    // 辅助方法：获取已存在序列号
    private <Entity> Set<String> getExistingSerials(
            BaseMapper<Entity> mapper, // 假设使用Spring Data JPA
            List<? extends BaseCreateForm> createVOs,
            Function<Entity, String> serialExtractor) {

        // 提取所有序列号
        List<String> serials = createVOs.stream()
                .map(BaseCreateForm::getSprinklerSerial)
                .toList();

        List<Entity> entities = mapper.selectList(new QueryWrapper<Entity>()
                .in("sprinkler_serial", serials) // 假设数据库字段名为sprinkler_serial
        );

        return entities.stream()
                .map(serialExtractor)
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    // 辅助方法：对象转换
    private UsableSprinklerEntity convertToEntity(UsableSprinklerCreateForm form) {
        return SmartBeanUtil.copy(form, UsableSprinklerEntity.class);
    }


    private ResponseDTO<String> buildResponse(int successCount, Set<String> errorData) {
        String msg = String.format(
                "成功插入%d条，错误数据（空值/重复）:%s",
                successCount,
                errorData.isEmpty() ? "无" : String.join(",", errorData)
        );
        return ResponseDTO.okMsg(msg);
    }


}

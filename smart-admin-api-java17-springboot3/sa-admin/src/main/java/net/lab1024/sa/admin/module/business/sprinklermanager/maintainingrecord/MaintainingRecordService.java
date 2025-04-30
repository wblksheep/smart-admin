package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.SprinklerService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaintainingRecordService {

    @Resource
    private SprinklerRepository sprinklerRepository;

    /**
     * 批量导入维修记录
     *
     * @param file
     * @param requestUser
     * @return
     */
    public ResponseDTO<String> batchMaintainingRecordCreate(@Valid MultipartFile file, RequestUser requestUser) {
        // 1. 数据导入
        List<MaintainingRecordCreateForm> createVOs = ExcelUtil
                .importExcelByClass(file, MaintainingRecordCreateForm.class)
                .stream()
                .peek(vo -> initCreateVO(vo, requestUser))
                .toList();
        // 2. 空数据校验（基础校验优化）
        if (CollectionUtils.isEmpty(createVOs)) {
            return ResponseDTO.userErrorParam("导入数据为空");
        }

        // 3. 数据预处理分区（使用Stream分区优化处理效率）
        Map<Boolean, List<MaintainingRecordCreateForm>> preprocessed = createVOs.stream()
                .collect(Collectors.partitioningBy(form -> StringUtils.isNotBlank(form.getSprinklerSerial())));
        List<MaintainingRecordCreateForm> validForms = preprocessed.get(true);
        List<MaintainingRecordCreateForm> invalidForms = preprocessed.get(false);

        // 4. 批量查询主表数据
        Set<String> serials = validForms.stream()
                .map(MaintainingRecordCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);
        return ResponseDTO.ok();
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

    // 辅助方法：初始化创建对象
    private void initCreateVO(MaintainingRecordCreateForm vo, RequestUser user) {
        vo.setDisabledFlag(Boolean.FALSE);
        vo.setCreateUserId(user.getUserId());
        vo.setCreateUserName(user.getUserName());
    }
}

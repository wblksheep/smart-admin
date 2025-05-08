package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AllocationRetWarehouseRecordService {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private AllocationRetWarehouseRecordRepository allocationRetWarehouseRecordRepository;

    /**
     * 批量导入维修记录
     *
     * @param file
     * @param requestUser
     * @return
     */
    public ResponseDTO<String> batchAllocationRetWarehouseRecordImport(@Valid MultipartFile file, RequestUser requestUser) {
        // 1. 数据导入
        List<AllocationRetWarehouseRecordCreateForm> createVOs = ExcelUtil
                .importExcelByClass(file, AllocationRetWarehouseRecordCreateForm.class)
                .stream()
                .peek(vo -> initCreateVO(vo, requestUser))
                .toList();
        // 2. 空数据校验（基础校验优化）
        if (CollectionUtils.isEmpty(createVOs)) {
            return ResponseDTO.userErrorParam("导入数据为空");
        }

        // 3. 数据预处理分区（使用Stream分区优化处理效率）
        Map<Boolean, List<AllocationRetWarehouseRecordCreateForm>> preprocessed = createVOs.stream()
                .collect(Collectors.partitioningBy(form -> StringUtils.isNotBlank(form.getSprinklerSerial())));
        List<AllocationRetWarehouseRecordCreateForm> validForms = preprocessed.get(true);
        List<AllocationRetWarehouseRecordCreateForm> invalidForms = preprocessed.get(false);

        // 3. 收集无效序列号（并行流优化处理大数据量场景）
        Set<String> invalidSerials = collectInvalidSerials(invalidForms);

        // 5. 批量查询主表数据
        Set<String> serials = validForms.stream()
                .map(AllocationRetWarehouseRecordCreateForm::getSprinklerSerial)
                .collect(Collectors.toSet());
        Map<String, SprinklerEntity> mainTableMap = getMainTableMap(serials);

        // 6. 主表校验及记录校验
        // 6.1 批量查询已存在的序列号（优化点：合并查询条件）
        List<AllocationRetWarehouseRecordEntity> existingEntities = getExistingSprinklerSerials(validForms);
        // 7. 实体转换（使用Bean拷贝工具优化代码简洁性）
        List<AllocationRetWarehouseRecordEntity> entities = validForms.stream()
                .map(form -> convertToWarehouseEntity(form, mainTableMap))
                .filter(entity -> entity.getSprinklerId() != null)
                .toList();
        // 8.2 批量插入维修记录数据（使用MyBatis-Plus批量操作优化）
        if (!entities.isEmpty()) {
            allocationRetWarehouseRecordRepository.saveBatch(entities);
        }
        return ResponseDTO.ok("处理成功，无效数据：" + invalidSerials);
    }

    /**
     * 实体转换方法（使用SmartBeanUtil优化属性拷贝）
     * @param form 表单对象
     * @param mainTableMap 主表数据映射
     * @return 可用仓实体
     */
    private AllocationRetWarehouseRecordEntity convertToWarehouseEntity(
            AllocationRetWarehouseRecordCreateForm form,
            Map<String, SprinklerEntity> mainTableMap
    ) {
        // 使用Bean拷贝工具优化属性复制
        AllocationRetWarehouseRecordEntity entity = SmartBeanUtil.copy(form, AllocationRetWarehouseRecordEntity.class);
        SprinklerEntity mainEntity = mainTableMap.get(form.getSprinklerSerial());
        if (mainEntity != null) {
            entity.setSprinklerId(mainEntity.getSprinklerId());
        }
        return entity;
    }

    private List<AllocationRetWarehouseRecordEntity> getExistingSprinklerSerials(List<AllocationRetWarehouseRecordCreateForm> validForms) {
        List<String> serialsToCheck = validForms.stream()
                .map(AllocationRetWarehouseRecordCreateForm::getSprinklerSerial)
                .toList();
        if (serialsToCheck.isEmpty()) {
            return Collections.emptyList();
        }
        // 使用单次查询优化数据库访问
        return allocationRetWarehouseRecordRepository.getBaseMapper()
                .selectList(new QueryWrapper<AllocationRetWarehouseRecordEntity>().in("sprinkler_serial", serialsToCheck));
    }

    /**
     * 收集无效序列号（空值过滤优化）
     * @param invalidForms 无效表单列表
     * @return 无效序列号集合
     */
    private Set<String> collectInvalidSerials(List<AllocationRetWarehouseRecordCreateForm> invalidForms) {
        return invalidForms.stream()
                .map(AllocationRetWarehouseRecordCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
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
    private void initCreateVO(AllocationRetWarehouseRecordCreateForm vo, RequestUser user) {
        vo.setDisabledFlag(Boolean.FALSE);
        vo.setCreateUserId(user.getUserId());
        vo.setCreateUserName(user.getUserName());
    }
    /**
     * 新建维修记录
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createAllocationRetWarehouseRecord(AllocationRetWarehouseRecordCreateForm createVO) {
        // 验证喷头是否存在
        List<SprinklerEntity> validateSprinkler = sprinklerRepository.getListBySprinklerSerials(Arrays.asList(createVO.getSprinklerSerial()));
        if (validateSprinkler.isEmpty()) {
            return ResponseDTO.userErrorParam("无效的喷头");
        }
        AllocationRetWarehouseRecordEntity insertAllocationRetWarehouseRecord = SmartBeanUtil.copy(createVO, AllocationRetWarehouseRecordEntity.class);
        insertAllocationRetWarehouseRecord.setSprinklerId(validateSprinkler.get(0).getSprinklerId());
        allocationRetWarehouseRecordRepository.save(insertAllocationRetWarehouseRecord);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询维修记录模块
     */
    public ResponseDTO<PageResult<AllocationRetWarehouseRecordVO>> queryByPage(AllocationRetWarehouseRecordQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AllocationRetWarehouseRecordVO> allocationRetWarehouseRecordList = allocationRetWarehouseRecordRepository.getListByQueryPage(page, queryForm);

        PageResult<AllocationRetWarehouseRecordVO> pageResult = SmartPageUtil.convert2PageResult(page, allocationRetWarehouseRecordList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询维修记录详情
     *
     */
    public AllocationRetWarehouseRecordVO getDetail(Long recordId) {
        return allocationRetWarehouseRecordRepository.getDetail(recordId, Boolean.FALSE);
    }
}

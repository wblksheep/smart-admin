package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.constant.AllocationRetWarehouseTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private AllocationRetWarehouseRepository allocationRetWarehouseRepository;


    /**
     * 新建领用与返仓记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createAllocationRetWarehouseRecord(@Valid AllocationRetWarehouseRecordCreateForm createVO) {
        List<AllocationRetWarehouseCreateForm> createVOs = createVO.getAllocationRetWarehouseCreateForm();
        //提前返回空值情况
        if (createVOs.isEmpty()) {
            return ResponseDTO.ok("数据为空");
        }
        // 1.1. 提取需要校验的领用喷头序列号
        Set<String> serialsToCheck = createVOs.stream()
                .map(AllocationRetWarehouseCreateForm::getAllocateSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());

        // 1.2. 批量查询数据库（避免N+1问题）
        Map<String, SprinklerEntity> sprinklerMap = sprinklerRepository.getListBySprinklerSerials(new ArrayList<>(serialsToCheck))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));

        // 1.3. 有效性校验
        if (serialsToCheck.size() > sprinklerMap.size()) {
            Set<String> missingSerials = new HashSet<>(serialsToCheck);
            missingSerials.removeAll(sprinklerMap.keySet());
            return ResponseDTO.userErrorParam("领用喷头不存在:" + String.join(",", missingSerials));
        }

        // 2.1. 提取需要校验的返仓喷头序列号
        Set<String> retWarehouseSerialsToCheck = createVOs.stream()
                .map(AllocationRetWarehouseCreateForm::getRetWarehouseSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        // 2.2. 批量查询数据库（避免N+1问题）
        Map<String, SprinklerEntity> retWarehouseSprinklerMap = sprinklerRepository.getListBySprinklerSerials(new ArrayList<>(retWarehouseSerialsToCheck))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));
        // 2.3. 有效性校验
        if (retWarehouseSerialsToCheck.size() > retWarehouseSprinklerMap.size()) {
            Set<String> missingSerials = new HashSet<>(retWarehouseSerialsToCheck);
            missingSerials.removeAll(retWarehouseSprinklerMap.keySet());
            return ResponseDTO.userErrorParam("返仓喷头不存在:" + String.join(",", missingSerials));
        }
        RequestUser requestUser = SmartRequestUtil.getRequestUser();

        // 使用更简洁的变量名
        AllocationRetWarehouseRecordEntity recordEntity = new AllocationRetWarehouseRecordEntity();
        recordEntity.setCreateUserId(requestUser.getUserId());
        recordEntity.setCreateUserName(requestUser.getUserName());
        recordEntity.setStatus(AllocationRetWarehouseTypeEnum.UNDER_REVIEW.getValue());
        allocationRetWarehouseRecordRepository.save(recordEntity);

        // 使用 Stream API 的 map 操作进行数据转换，并通过 collect 直接生成列表
        List<AllocationRetWarehouseEntity> insertAllocationRetWarehouseEntities = createVOs.stream()
                .map(form -> convertToEntity(form, recordEntity.getRecordId(), sprinklerMap, retWarehouseSprinklerMap))
                .collect(Collectors.toList());

        allocationRetWarehouseRepository.saveBatch(insertAllocationRetWarehouseEntities);
        return ResponseDTO.ok();
    }

    // 转换逻辑封装为独立方法
    private AllocationRetWarehouseEntity convertToEntity(AllocationRetWarehouseCreateForm form, Long recordId,
                                                         Map<String, SprinklerEntity> sprinklerMap, Map<String, SprinklerEntity> retWarehouseSprinklerMap) {
        AllocationRetWarehouseEntity entity = SmartBeanUtil.copy(form, AllocationRetWarehouseEntity.class);
        entity.setRecordId(recordId);

        // 提前获取 serial 避免重复调用
        String allocateSerial = entity.getAllocateSprinklerSerial();
        SprinklerEntity allocateSprinkler = sprinklerMap.get(allocateSerial);
        // 添加空值检查防止 NPE
        if (allocateSprinkler == null) {
            throw new IllegalStateException("Allocate Sprinkler not found for serial: " + allocateSerial);
        }
        entity.setAllocateSprinklerId(allocateSprinkler.getSprinklerId());

        String retWarehouseSerial = entity.getRetWarehouseSprinklerSerial();
        SprinklerEntity retWarehouseSprinkler = retWarehouseSprinklerMap.get(retWarehouseSerial);
        if (retWarehouseSprinkler == null) {
            throw new IllegalStateException("Ret Warehouse Sprinkler not found for serial: " + retWarehouseSerial);
        }
        entity.setRetWarehouseSprinklerId(retWarehouseSprinkler.getSprinklerId());

        return entity;
    }


    /**
     * 分页查询领用与返仓模块
     */
    public ResponseDTO<PageResult<AllocationRetWarehouseVO>> queryByPage(AllocationRetWarehouseQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AllocationRetWarehouseVO> allocationRetWarehouseList = allocationRetWarehouseRepository.getListByQueryPage(page, queryForm);

        PageResult<AllocationRetWarehouseVO> pageResult = SmartPageUtil.convert2PageResult(page, allocationRetWarehouseList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询领用与返仓记录详情
     */
    public AllocationRetWarehouseRecordVO getDetail(Long recordId) {

        AllocationRetWarehouseRecordVO allocationRetWarehouseRecordVO =allocationRetWarehouseRecordRepository.getDetail(recordId, Boolean.FALSE);
        List<AllocationRetWarehouseVO> vos = allocationRetWarehouseRepository.getDetail(recordId, Boolean.FALSE);
        allocationRetWarehouseRecordVO.setAllocationRetWarehouseVO(vos);
        return allocationRetWarehouseRecordVO;
    }

    /**
     * 编辑领用与返仓记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateAllocationRetWarehouseRecord(AllocationRetWarehouseRecordUpdateForm updateVO) {
        Long recordId = updateVO.getRecordId();
        // 校验领用与返仓记录是否存在
        AllocationRetWarehouseRecordEntity recordDetail = allocationRetWarehouseRecordRepository.getById(recordId);
        if (Objects.isNull(recordDetail) || recordDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("领用与返仓记录不存在");
        }
        List<AllocationRetWarehouseUpdateForm> updateVOs = updateVO.getAllocationRetWarehouseUpdateForm();
        // 0.1. 提取需要校验的领用与返仓信息ID
        Set<Long> idsToCheck = updateVOs.stream()
                .map(AllocationRetWarehouseUpdateForm::getAllocationRetWarehouseId)
                .collect(Collectors.toSet());
        // 0.2. 批量查询数据库（避免N+1问题）
        Map<Long, AllocationRetWarehouseEntity> allocationRetWarehouseEntityMap = allocationRetWarehouseRepository.getListByAllocationRetWarehouseId(idsToCheck)
                .stream()
                .collect(Collectors.toMap(AllocationRetWarehouseEntity::getAllocationRetWarehouseId, Function.identity()));
        // 0.3. 有效性校验
        if (idsToCheck.size() > allocationRetWarehouseEntityMap.size()) {
            Set<Long> missingIds = new HashSet<>(idsToCheck);
            missingIds.removeAll(allocationRetWarehouseEntityMap.keySet());
            return ResponseDTO.userErrorParam("领用与返仓信息不存在:" + missingIds);
        }
        // 1.1. 提取需要校验的领用喷头序列号
        Set<String> serialsToCheck = updateVOs.stream()
                .map(AllocationRetWarehouseUpdateForm::getAllocateSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        // 1.2. 批量查询数据库（避免N+1问题）
        Map<String, SprinklerEntity> sprinklerMap = sprinklerRepository.getListBySprinklerSerials(new ArrayList<>(serialsToCheck))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));

        // 1.3. 有效性校验
        if (serialsToCheck.size() > sprinklerMap.size()) {
            Set<String> missingSerials = new HashSet<>(serialsToCheck);
            missingSerials.removeAll(sprinklerMap.keySet());
            return ResponseDTO.userErrorParam("领用喷头不存在:" + String.join(",", missingSerials));
        }

        // 2.1. 提取需要校验的返仓喷头序列号
        Set<String> retWarehouseSerialsToCheck = updateVOs.stream()
                .map(AllocationRetWarehouseUpdateForm::getRetWarehouseSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        // 2.2. 批量查询数据库（避免N+1问题）
        Map<String, SprinklerEntity> retWarehouseSprinklerMap = sprinklerRepository.getListBySprinklerSerials(new ArrayList<>(retWarehouseSerialsToCheck))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));
        // 2.3. 有效性校验
        if (retWarehouseSerialsToCheck.size() > retWarehouseSprinklerMap.size()) {
            Set<String> missingSerials = new HashSet<>(retWarehouseSerialsToCheck);
            missingSerials.removeAll(retWarehouseSprinklerMap.keySet());
            return ResponseDTO.userErrorParam("返仓喷头不存在:" + String.join(",", missingSerials));
        }

        RequestUser requestUser = SmartRequestUtil.getRequestUser();

        // 数据编辑
        AllocationRetWarehouseRecordEntity updateEntity = SmartBeanUtil.copy(recordDetail, AllocationRetWarehouseRecordEntity.class);
        SmartBeanUtil.copyProperties(updateVO, updateEntity);
        allocationRetWarehouseRecordRepository.updateById(updateEntity);

        // 使用 Stream API 的 map 操作进行数据转换，并通过 collect 直接生成列表
        List<AllocationRetWarehouseEntity> updateAllocationRetWarehouseEntities = updateVOs.stream()
                .map(form -> convertToEntity(form, sprinklerMap, retWarehouseSprinklerMap))
                .collect(Collectors.toList());

        allocationRetWarehouseRepository.updateBatchById(updateAllocationRetWarehouseEntities);
        return ResponseDTO.ok();
    }

    // 转换逻辑封装为独立方法
    private AllocationRetWarehouseEntity convertToEntity(AllocationRetWarehouseUpdateForm form,
                                                         Map<String, SprinklerEntity> sprinklerMap, Map<String, SprinklerEntity> retWarehouseSprinklerMap) {
        AllocationRetWarehouseEntity entity = SmartBeanUtil.copy(form, AllocationRetWarehouseEntity.class);
        // 提前获取 serial 避免重复调用
        String allocateSerial = entity.getAllocateSprinklerSerial();
        SprinklerEntity allocateSprinkler = sprinklerMap.get(allocateSerial);
        // 添加空值检查防止 NPE
        if (allocateSprinkler == null) {
            throw new IllegalStateException("Allocate Sprinkler not found for serial: " + allocateSerial);
        }
        entity.setAllocateSprinklerId(allocateSprinkler.getSprinklerId());

        String retWarehouseSerial = entity.getRetWarehouseSprinklerSerial();
        SprinklerEntity retWarehouseSprinkler = retWarehouseSprinklerMap.get(retWarehouseSerial);
        if (retWarehouseSprinkler == null) {
            throw new IllegalStateException("Ret Warehouse Sprinkler not found for serial: " + retWarehouseSerial);
        }
        entity.setRetWarehouseSprinklerId(retWarehouseSprinkler.getSprinklerId());

        return entity;
    }

    public ResponseDTO<String> approveAllocationRetWarehouseRecord(AllocationRetWarehouseRecordApproveForm approveVO) {
        Long recordId = approveVO.getRecordId();
        Boolean isApproved = approveVO.getIsApproved();
        // 校验领用与返仓记录是否存在
        AllocationRetWarehouseRecordEntity recordDetail = allocationRetWarehouseRecordRepository.getById(recordId);
        if (Objects.isNull(recordDetail) || recordDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("领用与返仓记录不存在");
        }
        if(isApproved){
            recordDetail.setStatus(AllocationRetWarehouseTypeEnum.APPROVED.getValue());
        }else {
            recordDetail.setStatus(AllocationRetWarehouseTypeEnum.NOTAPPROVED.getValue());
        }
        allocationRetWarehouseRecordRepository.updateById(recordDetail);
        return ResponseDTO.okMsg(isApproved?"审核通过":"审核不通过");
    }



}

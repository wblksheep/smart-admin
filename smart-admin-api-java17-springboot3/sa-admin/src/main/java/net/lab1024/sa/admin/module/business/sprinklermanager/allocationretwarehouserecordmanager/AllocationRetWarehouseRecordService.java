package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordCreateForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordQueryForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
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

//    @Resource
//    private AllocationRetWarehouseRecordRepository allocationRetWarehouseRecordRepository;


    /**
     * 新建领用与返仓记录
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createAllocationRetWarehouseRecord(AllocationRetWarehouseRecordCreateForm createVO) {
        List<AllocationRetWarehouseCreateForm> createVOs = createVO.getAllocationRetWarehouseCreateForm();
        //提前返回空值情况
        if (createVOs.isEmpty()) {
            return ResponseDTO.ok("数据为空");
        }
        // 1. 提取需要校验的喷头序列号
        Set<String> serialsToCheck = createVOs.stream()
                .map(AllocationRetWarehouseCreateForm::getAllocateSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());

        // 2. 批量查询数据库（避免N+1问题）
        Map<String, SprinklerEntity> sprinklerMap = sprinklerRepository.getListBySprinklerSerials(new ArrayList<>(serialsToCheck))
                .stream()
                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));

        // 3. 有效性校验
        if (serialsToCheck.size() > sprinklerMap.size()){
            Set<String> missingSerials = new HashSet<>(serialsToCheck);
            missingSerials.removeAll(sprinklerMap.keySet());
            return ResponseDTO.userErrorParam("领用喷头不存在:"+String.join(",", missingSerials));
        }

        // 4. 过滤有效数据
        List<AllocationRetWarehouseCreateForm> validCreate = createVOs.stream()
                .filter(form->{
                    String serial = form.getAllocateSprinklerSerial();
                    return StringUtils.isNotBlank(serial) && sprinklerMap.containsKey(serial);
                })
                .toList();








//        // 1. 提取需要校验的喷头序列号
//        Set<String> serialsToCheck = createVOs.stream()
//                .map(AllocationRetWarehouseCreateForm::getAllocateSprinklerSerial)
//                .filter(StringUtils::isNotBlank)
//                .collect(Collectors.toSet());
//
//        // 2. 批量查询数据库（避免N+1问题）
//        Map<String, SprinklerEntity> sprinklerMap = sprinklerRepository.getListBySprinklerSerials(new ArrayList<>(serialsToCheck))
//                .stream()
//                .collect(Collectors.toMap(SprinklerEntity::getSprinklerSerial, Function.identity()));
//
//        // 3. 有效性校验
//        if (serialsToCheck.size() > sprinklerMap.size()) {
//            Set<String> missingSerials = new HashSet<>(serialsToCheck);
//            missingSerials.removeAll(sprinklerMap.keySet());
//            return ResponseDTO.userErrorParam("领用喷头不存在:"+String.join(",", missingSerials));
//        }
//
//        // 4. 过滤有效数据（使用并行流加速处理）
//        List<AllocationRetWarehouseCreateForm> validCreateVOs = createVOs.stream()
//                .filter(form -> {
//                    String serial = form.getAllocateSprinklerSerial();
//                    return StringUtils.isNotBlank(serial) && sprinklerMap.containsKey(serial);
//                })
//                .collect(Collectors.toList());

//        AllocationRetWarehouseRecordEntity insertAllocationRetWarehouseRecord =
//                SmartBeanUtil.copy(createVO, AllocationRetWarehouseRecordEntity.class);
//        insertAllocationRetWarehouseRecord.setSprinklerId(validateSprinkler.get(0).getSprinklerId());
//        allocationRetWarehouseRecordRepository.save(insertAllocationRetWarehouseRecord);
        return ResponseDTO.ok();
    }

//    /**
//     * 分页查询领用与返仓记录模块
//     */
//    public ResponseDTO<PageResult<AllocationRetWarehouseRecordVO>> queryByPage(AllocationRetWarehouseRecordQueryForm queryForm) {
//        queryForm.setDeletedFlag(Boolean.FALSE);
//        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
//        List<AllocationRetWarehouseRecordVO> allocationRetWarehouseRecordList = allocationRetWarehouseRecordRepository.getListByQueryPage(page, queryForm);
//
//        PageResult<AllocationRetWarehouseRecordVO> pageResult = SmartPageUtil.convert2PageResult(page, allocationRetWarehouseRecordList);
//        return ResponseDTO.ok(pageResult);
//    }

//    /**
//     * 查询领用与返仓记录详情
//     */
//    public AllocationRetWarehouseRecordVO getDetail(Long recordId) {
//        return allocationRetWarehouseRecordRepository.getDetail(recordId, Boolean.FALSE);
//    }
}

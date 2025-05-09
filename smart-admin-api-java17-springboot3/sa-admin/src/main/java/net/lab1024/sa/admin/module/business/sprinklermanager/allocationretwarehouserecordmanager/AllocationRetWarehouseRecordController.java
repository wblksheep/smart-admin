package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.AllocationRetWarehouseRecordService;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordCreateForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordQueryForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

/**
 * 领用与返仓记录
 *
 * @Author 海印: 芦苇
 */
@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER_ALLOCATIONRETWAREHOUSE)
public class AllocationRetWarehouseRecordController {

    @Resource
    private AllocationRetWarehouseRecordService allocationRetWarehouseRecordService;

    @Operation(summary = "新建领用与返仓记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationretwarehouserecord/create")
    @SaCheckPermission("sprinklermanager:allocationretwarehouserecord:add")
    public ResponseDTO<String> createAllocationRetWarehouseRecord(
            @RequestBody @Valid AllocationRetWarehouseRecordCreateForm createVO
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.getAllocationRetWarehouseCreateForm().stream().forEach(form->{
            form.setCreateUserId(requestUser.getUserId());
            form.setCreateUserName(requestUser.getUserName());
        });
        return allocationRetWarehouseRecordService.createAllocationRetWarehouseRecord(createVO);
    }

}

package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.form.EnterpriseUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.AllocationRetWarehouseRecordService;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordCreateForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordQueryForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.*;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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
        Long userId = requestUser.getUserId();
        String userName = requestUser.getUserName();
        createVO.setCreateUserId(userId);
        createVO.setCreateUserName(userName);
        createVO.getAllocationRetWarehouseCreateForm().stream().forEach(form -> {
            form.setCreateUserId(userId);
            form.setCreateUserName(userName);
        });
        return allocationRetWarehouseRecordService.createAllocationRetWarehouseRecord(createVO);
    }

    @Operation(summary = "分页查询领用与返仓记录模块 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationretwarehouserecord/page/query")
    @SaCheckPermission("sprinklermanager:allocationretwarehouserecord:query")
    public ResponseDTO<PageResult<AllocationRetWarehouseRecordVO>> queryByPage(@RequestBody @Valid AllocationRetWarehouseQueryForm queryForm) {
        return allocationRetWarehouseRecordService.queryByPage(queryForm);
    }

    @Operation(summary = "查询领用与返仓记录详情 @author 芦苇")
    @GetMapping("/sprinklermanager/allocationretwarehouserecord/get/{recordId}")
    @SaCheckPermission("sprinklermanager:allocationretwarehouserecord:detail")
    public ResponseDTO<AllocationRetWarehouseRecordVO> getDetail(@PathVariable Long recordId) {
        return ResponseDTO.ok(allocationRetWarehouseRecordService.getDetail(recordId));
    }

    @Operation(summary = "编辑领用与返仓记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationretwarehouserecord/update")
    @SaCheckPermission("sprinklermanager:allocationretwarehouserecord:update")
    public ResponseDTO<String> updateAllocationRetWarehouseRecord(@RequestBody @Valid AllocationRetWarehouseRecordUpdateForm updateVO) {
        return allocationRetWarehouseRecordService.updateAllocationRetWarehouseRecord(updateVO);
    }


    @Operation(summary = "删除领用与返仓记录 @author 芦苇")
    @GetMapping("/sprinklermanager/allocationretwarehouserecord/{recordId}")
    @SaCheckPermission("sprinklermanager:allocationretwarehouserecord:delete")
    public ResponseDTO<String> deleteAllocationRetWarehouseRecord(@PathVariable Long recordId) {
        return allocationRetWarehouseRecordService.deleteAllocationRetWarehouseRecord(recordId);
    }

    @Operation(summary = "领用与返仓记录通过与否 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationretwarehouserecord/approve")
    @SaCheckPermission("sprinklermanager:allocationretwarehouserecord:approve")
    public ResponseDTO<String> updateAllocationRetWarehouseRecord(@RequestBody @Valid AllocationRetWarehouseRecordApproveForm approveVO) {
        return allocationRetWarehouseRecordService.approveAllocationRetWarehouseRecord(approveVO);
    }

    @Operation(summary = "导出领用与返仓记录信息 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationretwarehouserecord/export")
    public void export(@RequestBody @Valid AllocationRetWarehouseQueryForm queryForm, HttpServletResponse response) throws IOException {
        List<AllocationRetWarehouseExcelVO> data = allocationRetWarehouseRecordService.getAllocationRetWarehouseRecordExcelExportData(queryForm);
        if (CollectionUtils.isEmpty(data)) {
            SmartResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
            return;
        }

        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);

        SmartExcelUtil.exportExcelWithWatermark(response, "领用与返仓记录表.xlsx", "领用与返仓记录", AllocationRetWarehouseExcelVO.class, data, watermark);

    }

}

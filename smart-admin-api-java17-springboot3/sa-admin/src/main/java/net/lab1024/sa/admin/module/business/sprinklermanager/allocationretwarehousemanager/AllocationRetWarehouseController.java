package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehousemanager;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.AllocationRetWarehouseService;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.AllocationRetWarehouseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.AllocationRetWarehouseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.AllocationRetWarehouseVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 领用与返仓记录
 *
 * @Author 海印: 芦苇
 */
@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER_MAINTAININGRECORD)
public class AllocationRetWarehouseController {

    @Resource
    private AllocationRetWarehouseService allocationRetWarehouseService;

    @Operation(summary = "分页查询领用与返仓记录模块 @author 芦苇")
        @PostMapping("/sprinklermanager/allocationretwarehouse/page/query")
    @SaCheckPermission("sprinklermanager:allocationretwarehouse:query")
    public ResponseDTO<PageResult<AllocationRetWarehouseVO>> queryByPage(@RequestBody @Valid AllocationRetWarehouseQueryForm queryForm) {
        return allocationRetWarehouseService.queryByPage(queryForm);
    }

    @Operation(summary = "新建领用与返仓记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationretwarehouse/create")
    @SaCheckPermission("sprinklermanager:allocationretwarehouse:add")
    public ResponseDTO<String> createAllocationRetWarehouse(
            @RequestBody @Valid AllocationRetWarehouseCreateForm createVO
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return allocationRetWarehouseService.createAllocationRetWarehouse(createVO);
    }

    @Operation(summary = "查询领用与返仓记录详情 @author 芦苇")
    @GetMapping("/sprinklermanager/allocationretwarehouse/get/{recordId}")
    @SaCheckPermission("sprinklermanager:allocationretwarehouse:detail")
    public ResponseDTO<AllocationRetWarehouseVO> getDetail(@PathVariable Long recordId) {
        return ResponseDTO.ok(allocationRetWarehouseService.getDetail(recordId));
    }
}

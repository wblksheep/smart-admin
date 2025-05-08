package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.vo.AllocationRecordVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**
 * 领用记录
 *
 * @Author 海印: 芦苇
 */
@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER_ALLOCATIONRECORD)
public class AllocationRecordController {

    @Resource
    private AllocationRecordService allocationRecordService;

    @Operation(summary = "批量导入领用记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationrecord/import")
    @SaCheckPermission("sprinklermanager:allocationrecord:import")
    public ResponseDTO<String> importAllocationRecord(
            @RequestPart("file") @Valid MultipartFile file
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return allocationRecordService.batchAllocationRecordImport(file, requestUser);
    }

    @Operation(summary = "分页查询领用记录模块 @author 芦苇")
        @PostMapping("/sprinklermanager/allocationrecord/page/query")
    @SaCheckPermission("sprinklermanager:allocationrecord:query")
    public ResponseDTO<PageResult<AllocationRecordVO>> queryByPage(@RequestBody @Valid AllocationRecordQueryForm queryForm) {
        return allocationRecordService.queryByPage(queryForm);
    }

    @Operation(summary = "新建领用记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationrecord/create")
    @SaCheckPermission("sprinklermanager:allocationrecord:add")
    public ResponseDTO<String> createAllocationRecord(
            @RequestBody @Valid AllocationRecordCreateForm createVO
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return allocationRecordService.createAllocationRecord(createVO);
    }

    @Operation(summary = "查询领用记录详情 @author 芦苇")
    @GetMapping("/sprinklermanager/allocationrecord/get/{recordId}")
    @SaCheckPermission("sprinklermanager:allocationrecord:detail")
    public ResponseDTO<AllocationRecordVO> getDetail(@PathVariable Long recordId) {
        return ResponseDTO.ok(allocationRecordService.getDetail(recordId));
    }
}

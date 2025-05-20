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
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.vo.AllocationRecordVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "编辑领用记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationrecord/update")
    @SaCheckPermission("sprinklermanager:allocationrecord:update")
    public ResponseDTO<String> updateAllocationRecord(@RequestBody @Valid AllocationRecordUpdateForm updateVO) {
        return allocationRecordService.updateAllocationRecord(updateVO);
    }

    @Operation(summary = "删除领用记录 @author 芦苇")
    @GetMapping("/sprinklermanager/allocationrecord/delete/{recordId}")
    @SaCheckPermission("sprinklermanager:allocationrecord:delete")
    public ResponseDTO<String> deleteAllocationRecord(@PathVariable Long recordId) {
        return allocationRecordService.deleteAllocationRecord(recordId);
    }

    @Operation(summary = "批量删除领用记录 @author 芦苇")
    @PostMapping("/sprinklermanager/allocationrecord/update/batch/delete")
    @SaCheckPermission("sprinklermanager:allocationrecord:batchdelete")
    public ResponseDTO<String> batchUpdateDeleteFlag(@RequestBody List<Long> recordIdList) {
        return allocationRecordService.batchUpdateDeleteFlag(recordIdList);
    }

}

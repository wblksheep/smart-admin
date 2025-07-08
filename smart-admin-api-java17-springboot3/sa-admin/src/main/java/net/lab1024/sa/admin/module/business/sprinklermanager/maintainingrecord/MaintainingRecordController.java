package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.*;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修记录
 *
 * @Author 海印: 芦苇
 */
@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER_MAINTAININGRECORD)
public class MaintainingRecordController {

    @Resource
    private MaintainingRecordService maintainingRecordService;

    @Operation(summary = "批量导入维修记录 @author 芦苇")
    @PostMapping("/sprinklermanager/maintainingrecord/import")
    @SaCheckPermission("sprinklermanager:maintainingrecord:import")
    public ResponseDTO<String> importMaintainingRecord(
            @RequestPart("file") @Valid MultipartFile file
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return maintainingRecordService.batchMaintainingRecordImport(file, requestUser);
    }

    @Operation(summary = "导出维修记录 @author 芦苇")
    @PostMapping("/sprinklermanager/maintainingrecord/export")
    @SaCheckPermission("sprinklermanager:maintainingrecord:export")
    public void exportMaintainingRecordExcel(
            @RequestBody @Valid MaintainingRecordQueryForm queryForm,
            HttpServletResponse response
    ) throws IOException {
        List<MaintainingRecordExcelVO> data = maintainingRecordService.getMaintainingRecordExcelExportData(queryForm);
        if (CollectionUtils.isEmpty(data)) {
            SmartResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
            return;
        }
        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);
        SmartExcelUtil.exportExcelWithWatermark(response, "维修信息记录表.xlsx", "维修信息", MaintainingRecordExcelVO.class, data, watermark);
    }

    @Operation(summary = "分页查询维修记录模块 @author 芦苇")
    @PostMapping("/sprinklermanager/maintainingrecord/page/query")
    @SaCheckPermission("sprinklermanager:maintainingrecord:query")
    public ResponseDTO<PageResult<MaintainingRecordVO>> queryByPage(@RequestBody @Valid MaintainingRecordQueryForm queryForm) {
        return maintainingRecordService.queryByPage(queryForm);
    }

    @Operation(summary = "新建维修记录 @author 芦苇")
    @PostMapping("/sprinklermanager/maintainingrecord/create")
    @SaCheckPermission("sprinklermanager:maintainingrecord:add")
    public ResponseDTO<String> createMaintainingRecord(
            @RequestBody @Valid MaintainingRecordCreateForm createVO
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return maintainingRecordService.createMaintainingRecord(createVO);
    }

    @Operation(summary = "删除维修记录 @author 芦苇")
    @GetMapping("/sprinklermanager/maintainingrecord/delete/{recordId}")
    @SaCheckPermission("sprinklermanager:maintainingrecord:delete")
    public ResponseDTO<String> delete(@PathVariable Long recordId) {
        return maintainingRecordService.update(recordId);
    }

    @Operation(summary = "编辑维修记录 @author 芦苇")
    @PostMapping("/sprinklermanager/maintainingrecord/update")
    @SaCheckPermission("sprinklermanager:maintainingrecord:update")
    public ResponseDTO<String> update(@RequestBody @Valid MaintainingRecordUpdateForm updateVO) {
        return maintainingRecordService.updateMaintainingRecord(updateVO);
    }

    @Operation(summary = "查询维修记录详情 @author 芦苇")
    @GetMapping("/sprinklermanager/maintainingrecord/get/{recordId}")
    @SaCheckPermission("sprinklermanager:maintainingrecord:detail")
    public ResponseDTO<MaintainingRecordVO> getDetail(@PathVariable Long recordId) {
        return ResponseDTO.ok(maintainingRecordService.getDetail(recordId));
    }
}

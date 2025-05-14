package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.form.EnterpriseUpdateForm;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.CombinedQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartDateFormatterEnum;
import net.lab1024.sa.base.common.util.SmartExcelUtil;
import net.lab1024.sa.base.common.util.SmartLocalDateUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@OperateLog
@Tag(name = AdminSwaggerTagConst.Business.SPRINKLER)
public class SprinklerController {
    @Resource
    private SprinklerService sprinklerService;

    @Operation(summary = "分页查询全部喷头模块 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/page/query")
    @SaCheckPermission("sprinklermanager:sprinkler:query")
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(@RequestBody @Valid SprinklerQueryForm queryForm) {
        return sprinklerService.queryByPage(queryForm);
    }

    @Operation(summary = "分页查询各仓喷头模块 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/page/query")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:query")
    public <R> ResponseDTO<PageResult<R>> repositoryQueryByPage(@RequestBody @Valid CombinedQueryForm queryForm) {
        return sprinklerService.repositoryQueryByPage(queryForm);
    }

    @Operation(summary = "查询各仓喷头详情 @author 芦苇")
    @GetMapping("/sprinklermanager/repositorysprinkler/get/{sprinklerId}")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:detail")
    public ResponseDTO<BaseSprinklerVO> getDetail(@PathVariable Long sprinklerId) {
        return ResponseDTO.ok(sprinklerService.getDetail(sprinklerId));
    }


    @Operation(summary = "批量新建所有喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/create")
    @SaCheckPermission("sprinklermanager:sprinkler:add")
    public ResponseDTO<String> createSprinkler(
            @RequestPart("file") @Valid MultipartFile file
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchSprinklerCreate(file, requestUser);
    }

    @Operation(summary = "编辑全部喷头 @author 芦苇")
    @PostMapping("/sprinklermanager//sprinkler/update")
    @SaCheckPermission("sprinklermanager:sprinkler:update")
    public ResponseDTO<String> updateSprinkler(@RequestBody @Valid SprinklerUpdateForm updateVO) {
        return sprinklerService.updateSprinkler(updateVO);
    }

    @Operation(summary = "编辑各仓喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/update")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:update")
    public ResponseDTO<String> updateSprinkler(@RequestBody @Valid SprinklerUpdateForm updateVO, Byte type) {
        return sprinklerService.updateRepositorySprinkler(updateVO, type);
    }

    @Operation(summary = "批量新建各仓喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/create")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:add")
    public ResponseDTO<String> createRepositorySprinkler(
            MultipartFile file,
            Integer type
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchRepositorySprinklerCreate(file, requestUser, type);
    }






    @Operation(summary = "导出全部喷头信息 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/exportSprinklerExcel")
    public void exportSprinklerExcel(@RequestBody @Valid SprinklerQueryForm queryForm, HttpServletResponse response) throws IOException {
        List<SprinklerExcelVO> data = sprinklerService.getSprinklerExcelExportData(queryForm);
        if (CollectionUtils.isEmpty(data)) {
            SmartResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
            return;
        }

        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);

        SmartExcelUtil.exportExcelWithWatermark(response,"喷头基本信息.xlsx","喷头信息", SprinklerExcelVO.class,data,watermark);

    }



    @Operation(summary = "导出各仓喷头信息 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/exportRepositorySprinklerExcel")
    public void exportRepositorySprinklerExcel(@RequestBody @Valid CombinedQueryForm queryForm, HttpServletResponse response) throws IOException {
        List<?> data = sprinklerService.getRepositorySprinklerExcelExportData(queryForm);
        if (CollectionUtils.isEmpty(data)) {
            SmartResponseUtil.write(response, ResponseDTO.userErrorParam("暂无数据"));
            return;
        }

        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);
        if (!data.isEmpty()) {
            Object firstElement = data.get(0);
            Class<?> elementClass = firstElement.getClass();
            System.out.println("元素类名: " + elementClass.getName());
            SmartExcelUtil.exportExcelWithWatermark(response,"喷头信息.xlsx","喷头信息", elementClass,data,watermark);
        }


    }

}

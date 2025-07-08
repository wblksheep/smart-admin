package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.bank.domain.BankCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.sorter.SprinklerSorter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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


    @Operation(summary = "批量导入全部喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/create")
    @SaCheckPermission("sprinklermanager:sprinkler:add")
    public ResponseDTO<String> importSprinkler(
            @RequestPart("file") @Valid MultipartFile file
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchSprinklerImport(file, requestUser);
    }

    @Operation(summary = "新建全部喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/createSprinkler")
    @SaCheckPermission("sprinklermanager:sprinkler:addsprinkler")
    public ResponseDTO<String> createSprinkler(@RequestBody @Valid SprinklerCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return sprinklerService.createSprinkler(createVO);
    }

    @Operation(summary = "编辑全部喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/update")
    @SaCheckPermission("sprinklermanager:sprinkler:update")
    public ResponseDTO<String> updateSprinkler(@RequestBody @Valid SprinklerUpdateForm updateVO) {
        return sprinklerService.updateSprinkler(updateVO);
    }

    @Operation(summary = "喷头转仓 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/transfer")
    @SaCheckPermission("sprinklermanager:sprinkler:transfer")
    public ResponseDTO<String> transferRepo(Long sprinklerId, @RequestParam @Valid @Min(0) @Max(4) Byte type) {
        return sprinklerService.transferRepo(sprinklerId, type);
    }

    @Operation(summary = "批量编辑全部喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/sprinkler/updateBatch")
    @SaCheckPermission("sprinklermanager:sprinkler:updateBatch")
    public ResponseDTO<String> updateBatchSprinkler(MultipartFile file) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.updateBatchSprinkler(file, requestUser);
    }

    @Operation(summary = "批量编辑各仓喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/updateBatch")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:updateBatch")
    public ResponseDTO<String> updateBatchSprinkler(MultipartFile file, Byte type) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.updateBatchSprinkler(file, type, requestUser);
    }

    @Operation(summary = "编辑各仓喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/update")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:update")
    public ResponseDTO<String> updateSprinkler(@RequestBody @Valid BaseUpdateForm updateVO, @RequestParam @Valid @Min(0) @Max(4) Byte type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        updateVO.setCreateUserId(requestUser.getUserId());
        updateVO.setCreateUserName(requestUser.getUserName());
        return sprinklerService.updateRepositorySprinkler(updateVO, type);
    }

    @Operation(summary = "批量导入各仓喷头 @author 芦苇")
    @PostMapping("/sprinklermanager/repositorysprinkler/create")
    @SaCheckPermission("sprinklermanager:repositorysprinkler:add")
    public ResponseDTO<String> createRepositorySprinkler(
            MultipartFile file,
            Integer type
    ) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return sprinklerService.batchRepositorySprinklerImport(file, requestUser, type);
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

        SmartExcelUtil.exportExcelWithWatermark(response, "喷头基本信息.xlsx", "喷头信息", SprinklerExcelVO.class, data, watermark);

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
            SmartExcelUtil.exportExcelWithWatermark(response, "喷头信息.xlsx", "喷头信息", elementClass, data, watermark);
        }


    }

}

package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;


import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.CombinedQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.form.MonthlyStatisticSheetQueryForm;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartDateFormatterEnum;
import net.lab1024.sa.base.common.util.SmartExcelUtil;
import net.lab1024.sa.base.common.util.SmartLocalDateUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 喷头管理-统计
 *
 * @Author 海印：芦苇
 */
@Slf4j
@RestController
@OperateLog
public class StatisticController {

    @Resource
    private StatisticService statisticService;

    @Operation(summary = "导出月表统计信息 @author 芦苇")
    @PostMapping("/sprinklermanager/statistic/exportMonthlySheetStatisticExcel")
    public void exportMonthlySheetStatisticExcel(@RequestBody @Valid MonthlyStatisticSheetQueryForm queryForm, HttpServletResponse response) throws IOException {
        String watermark = AdminRequestUtil.getRequestUser().getActualName();
        watermark += SmartLocalDateUtil.format(LocalDateTime.now(), SmartDateFormatterEnum.YMD_HMS);

        statisticService.getMonthlySheetStatisticExcelExportData(queryForm.getStartDate(), queryForm.getEndDate(), response, watermark);

    }
}

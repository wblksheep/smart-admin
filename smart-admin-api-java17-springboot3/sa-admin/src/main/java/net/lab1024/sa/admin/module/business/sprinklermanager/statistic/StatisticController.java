package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;


import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.CombinedQueryForm;
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
    public void exportMonthlySheetStatisticExcel( HttpServletResponse response) throws IOException {
        List<?> data = statisticService.getMonthlySheetStatisticExcelExportData();
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

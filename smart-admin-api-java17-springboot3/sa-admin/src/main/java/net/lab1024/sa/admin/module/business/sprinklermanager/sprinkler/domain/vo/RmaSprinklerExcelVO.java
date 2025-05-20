package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;

/**
 * rma喷头信息Excel
 *
 * @Author 海印: 芦苇
 */
@Data
public class RmaSprinklerExcelVO extends BaseSprinklerExcelVO{

    @ExcelProperty("返修原因")
    private String retMaintainenceReason;

    @ExcelProperty("返修日期")
    private LocalDate retMaintainenceDate;

    @ExcelProperty("返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty("返修客户")
    private String customer;

    @ExcelProperty("历史")
    private String history;

    @ExcelProperty("RMA地点")
    private String rmaPosition;

    @ExcelProperty("系统核对")
    private String warehouseCheck;

    @ExcelProperty("喷头详情")
    private String sprinklerDetail;

}

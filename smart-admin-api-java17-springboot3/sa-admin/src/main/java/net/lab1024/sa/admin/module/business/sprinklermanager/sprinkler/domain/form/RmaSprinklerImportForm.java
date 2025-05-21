package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RmaSprinklerImportForm extends BaseImportForm{


    @ExcelProperty("返修日期")
    @Schema(description = "返修日期")
    private LocalDate retMaintainenceDate;

    @ExcelProperty("返修原因")
    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @ExcelProperty("返仓日期")
    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty("返修客户")
    @Schema(description = "返修客户")
    private String customer;

    @ExcelProperty("历史")
    @Schema(description = "历史")
    private String history;

    @ExcelProperty("RMA地点")
    @Schema(description = "RMA地点")
    private String rmaPosition;

    @ExcelProperty("系统核对")
    @Schema(description = "系统核对")
    private String warehouseCheck;

    @ExcelIgnore
    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private String createUserName;
}

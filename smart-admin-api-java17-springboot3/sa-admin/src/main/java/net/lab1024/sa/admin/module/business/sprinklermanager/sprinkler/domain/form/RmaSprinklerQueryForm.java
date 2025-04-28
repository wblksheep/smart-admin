package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RmaSprinklerQueryForm extends BaseQueryForm{

    @Schema(description = "返修日期开始时间")
    private LocalDate retMaintainenceDateStartTime;

    @Schema(description = "返修日期结束时间")
    private LocalDate retMaintainenceDateEndTime;

    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @Schema(description = "返仓日期开始时间")
    private LocalDate retWarehouseDateStartTime;

    @Schema(description = "返仓日期结束时间")
    private LocalDate retWarehouseDateEndTime;

    @Schema(description = "返修客户")
    private String customer;

    @Schema(description = "RMA地点")
    private String rmaPosition;

    @Schema(description = "系统核对")
    private String warehouseCheck;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DamagedSprinklerQueryForm extends BaseQueryForm{

    @Schema(description = "返仓日期开始时间")
    private LocalDate retWarehouseDateStartTime;

    @Schema(description = "返仓日期结束时间")
    private LocalDate retWarehouseDateEndTime;

    @Schema(description = "备注1")
    private String note1;

    @Schema(description = "破损原因分类")
    private String damagedReasonType;

    @Schema(description = "具体破损原因")
    private String realDamagedReason;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}

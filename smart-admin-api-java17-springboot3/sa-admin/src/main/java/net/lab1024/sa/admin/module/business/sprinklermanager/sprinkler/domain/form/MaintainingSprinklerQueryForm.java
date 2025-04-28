package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintainingSprinklerQueryForm extends BaseQueryForm{

    @Schema(description = "返修日期开始时间")
    private LocalDate retMaintainenceDateStartTime;

    @Schema(description = "返修日期结束时间")
    private LocalDate retMaintainenceDateEndTime;

    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @Schema(description = "具体原因")
    private String realReason;

    @Schema(description = "返修客户")
    private String customer;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}

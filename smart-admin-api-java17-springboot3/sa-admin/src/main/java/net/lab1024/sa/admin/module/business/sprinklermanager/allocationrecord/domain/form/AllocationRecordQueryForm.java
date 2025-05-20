package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.time.LocalDate;

@Data
public class AllocationRecordQueryForm extends PageParam {

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "领用人")
    private String allocateUser;

    @Schema(description = "领用日期开始时间")
    private LocalDate allocateDateStartTime;

    @Schema(description = "领用日期结束时间")
    private LocalDate allocateDateEndTime;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}

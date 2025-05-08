package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class MaintainingRecordQueryForm extends PageParam {

    @Schema(description = "返修日期开始时间")
    private LocalDate retMaintainenceDateStartTime;

    @Schema(description = "返修日期结束时间")
    private LocalDate retMaintainenceDateEndTime;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @Schema(description = "具体原因")
    private String realReason;

    @Schema(description = "返修客户")
    private String customer;

    @Schema(description = "返仓日期开始时间")
    private LocalDate retWarehouseDateStartTime;

    @Schema(description = "返仓日期结束时间")
    private LocalDate retWarehouseDateEndTime;


    @Schema(description = "返仓类型")
    private String retWarehouseType;

    @Schema(description = "领用是否有限制")
    private String allocateLimitation;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}

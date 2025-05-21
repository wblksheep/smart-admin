package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintainingSprinklerUpdateForm extends BaseUpdateForm{

    @ExcelProperty("返修日期")
    @Schema(description = "返修日期")
    private LocalDate retMaintainenceDate;

    @ExcelProperty("返修原因")
    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @ExcelProperty("具体原因")
    @Schema(description = "具体原因")
    private String realReason;

    @ExcelProperty("返修客户")
    @Schema(description = "返修客户")
    private String customer;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;

}

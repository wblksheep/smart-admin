package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsableSprinklerUpdateForm extends BaseUpdateForm{

    @ExcelProperty("返仓日期")
    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty("领用是否有限制")
    @Schema(description = "领用是否有限制")
    private String allocateLimitation;

    @ExcelProperty("领用时备注1")
    @Schema(description = "领用时备注1")
    private String allocateNote1;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;

}

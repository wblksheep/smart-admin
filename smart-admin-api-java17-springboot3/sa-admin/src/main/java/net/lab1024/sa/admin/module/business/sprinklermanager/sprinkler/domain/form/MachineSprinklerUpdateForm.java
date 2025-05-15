package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MachineSprinklerUpdateForm extends BaseUpdateForm {

    @ExcelProperty("历史")
    @Schema(description = "历史")
    private String history;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;
}

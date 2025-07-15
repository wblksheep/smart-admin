package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class MachineSprinklerCreateForm extends BaseCreateForm{


    @ExcelProperty("历史")
    @Schema(description = "历史")
    private String history;

    @ExcelProperty("所在仓")
    @Schema(description = "所在仓")
    private Byte status;

}

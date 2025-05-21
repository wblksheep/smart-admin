package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MachineSprinklerImportForm extends BaseImportForm{


    @ExcelProperty("历史")
    @Schema(description = "历史")
    private String history;

}

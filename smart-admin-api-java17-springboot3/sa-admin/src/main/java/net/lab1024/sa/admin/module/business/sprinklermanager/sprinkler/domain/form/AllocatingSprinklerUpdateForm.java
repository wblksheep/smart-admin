package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AllocatingSprinklerUpdateForm extends BaseUpdateForm {

    @ExcelProperty("所在仓")
    @Schema(description = "所在仓")
    private Byte status;
}

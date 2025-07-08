package net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MachineUpdateForm extends MachineCreateForm {

    @ExcelProperty("机台ID")
    @Schema(description = "机台ID")
    @NotNull(message = "机台ID不能为空")
    private Long machineId;
}

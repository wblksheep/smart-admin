package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class MaintainingRecordUpdateForm extends MaintainingRecordCreateForm{
    @Schema(description = "维修信息记录ID")
    @NotNull(message = "维修信息记录ID不能为空")
    private Long recordId;
}

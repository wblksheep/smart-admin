package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DamagedSprinklerUpdateForm extends BaseUpdateForm {


    @ExcelProperty("返仓日期")
    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty("备注1")
    @Schema(description = "备注1")
    private String note1;

    @ExcelProperty("破损原因分类")
    @Schema(description = "破损原因分类")
    private String damagedReasonType;

    @ExcelProperty("具体破损原因")
    @Schema(description = "具体破损原因")
    private String realDamagedReason;

    @ExcelProperty("所在仓status")
    @Schema(description = "所在仓status")
    private Byte status;

    @ExcelIgnore
    @Schema(description = "禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private String createUserName;
}

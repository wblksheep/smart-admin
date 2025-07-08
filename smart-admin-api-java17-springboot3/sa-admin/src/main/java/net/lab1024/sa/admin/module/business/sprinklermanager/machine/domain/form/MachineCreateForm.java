package net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 喷头管理-机台创建
 *
 * @Author 海印: 芦苇
 */
@Data
public class MachineCreateForm {

    @ExcelProperty("机台名称")
    @Schema(description = "机台名称")
    @NotBlank(message = "机台名称不能为空")
    @Length(max = 50, message = "机台名称最多50字符")
    private String machineName;

    @ExcelProperty("机台类型")
    @Schema(description = "机台类型")
    @NotBlank(message = "机台类型不能为空")
    @Length(max = 50, message = "机台类型最多50字符")
    private String machineType;

    @ExcelIgnore
    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @ExcelIgnore
    @Schema(description = "创建人ID", hidden = true)
    private Long createUserId;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private String createUserName;
}

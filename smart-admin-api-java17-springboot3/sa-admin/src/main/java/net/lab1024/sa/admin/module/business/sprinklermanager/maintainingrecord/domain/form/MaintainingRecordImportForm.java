package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class MaintainingRecordImportForm {

    @ExcelProperty("返修日期")
    @Schema(description = "返修日期")
    private String retMaintainenceDate;

    @ExcelProperty("喷头序列号")
    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 255, message = "sprinklerSerial最多255字符")
    private String sprinklerSerial;

    @ExcelProperty("返修原因")
    @Schema(description = "返修原因")
    @Length(max = 255, message = "返修原因最多255字符")
    private String retMaintainenceReason;

    @ExcelProperty("具体原因")
    @Schema(description = "具体原因")
    @Length(max = 255, message = "具体原因最多255字符")
    private String realReason;

    @ExcelProperty("返修客户")
    @Schema(description = "返修客户")
    @Length(max = 255, message = "返修客户最多255字符")
    private String customer;

    @ExcelProperty("返仓日期")
    @Schema(description = "返仓日期")
    private String retWarehouseDate;

    @ExcelProperty("返仓类型")
    @Schema(description = "返仓类型")
    @Length(max = 255, message = "返仓类型最多255字符")
    private String retWarehouseType;

    @ExcelProperty("领用是否有限制")
    @Schema(description = "领用是否有限制")
    @Length(max = 255, message = "领用是否有限制最多255字符")
    private String allocateLimitation;

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

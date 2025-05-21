package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class BaseImportForm {

    @ExcelProperty("喷头序列号")
    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "sprinklerSerial最多20字符")
    private String sprinklerSerial;

    @ExcelProperty("所在仓")
    @Schema(description = "所在仓")
    private Byte status;

    @ExcelIgnore
    @Schema(description = "禁用状态")
//    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private Long createUserId;

    @ExcelIgnore
    @Schema(description = "创建人", hidden = true)
    private String createUserName;

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        switch (status) {
            case "可用仓":
                this.status = (byte) 0;
                break;
            case "机台":
                this.status = (byte) 1;
                break;
            case "维修仓":
                this.status = (byte) 2;
                break;
            case "破损仓":
                this.status = (byte) 3;
                break;
            case "rma":
                this.status = (byte) 4;
                break;
            default:
                throw new RuntimeException("非法的仓库状态");
        }
    }
}

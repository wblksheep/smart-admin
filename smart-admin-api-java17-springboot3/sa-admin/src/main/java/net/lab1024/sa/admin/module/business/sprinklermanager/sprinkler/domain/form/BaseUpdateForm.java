package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "sceneType") // 根据sceneType字段识别子类
@JsonSubTypes({
        @JsonSubTypes.Type(value = UsableSprinklerUpdateForm.class, name = "USABLE_REPOSITORY"),
        @JsonSubTypes.Type(value = MachineSprinklerUpdateForm.class, name = "MACHINE_REPOSITORY"),
        @JsonSubTypes.Type(value = MaintainingSprinklerUpdateForm.class, name = "MAINTAINING_REPOSITORY"),
        @JsonSubTypes.Type(value = DamagedSprinklerUpdateForm.class, name = "DAMAGED_REPOSITORY"),
        @JsonSubTypes.Type(value = RmaSprinklerUpdateForm.class, name = "RMA_REPOSITORY")
})
@Data
public abstract class BaseUpdateForm {
    @ExcelProperty("喷头ID")
    @Schema(description = "喷头ID")
    @NotNull(message = "喷头ID不能为空")
    private Long sprinklerId;

    @ExcelProperty("喷头序列号")
    @Schema(description = "喷头序列号")
    @NotBlank(message = "喷头序列号不能为空")
    @Length(max = 20, message = "sprinklerSerial最多20字符")
    private String sprinklerSerial;

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
}

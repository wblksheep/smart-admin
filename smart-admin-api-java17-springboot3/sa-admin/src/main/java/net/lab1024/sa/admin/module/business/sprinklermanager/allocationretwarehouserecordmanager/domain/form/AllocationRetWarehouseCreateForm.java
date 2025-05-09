package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AllocationRetWarehouseCreateForm {


    @Schema(description = "领用喷头序列号")
    @Length(max = 255, message = "领用喷头序列号最多255字符")
    private String allocateSprinklerSerial;

    @Schema(description = "领用喷头型号")
    @Length(max = 255, message = "领用喷头型号最多255字符")
    private String allocateSprinklerModel;

    @Schema(description = "领用用途")
    @Length(max = 255, message = "领用用途最多255字符")
    private String allocatePurpose;

    @Schema(description = "领用颜色")
    @Length(max = 255, message = "领用颜色最多255字符")
    private String allocateColor;

    @Schema(description = "领用位置",
        minimum = "0",      // Swagger 文档显示最小值
        maximum = "40",     // Swagger 文档显示最大值
        example = "10"
    )
    @Min(0)
    @Max(40)
    private Byte allocatePosition;

    @Schema(description = "领用喷头类型")
    private Boolean allocateSprinklerIsNew;

    @Schema(description = "返仓喷头序列号")
    @Length(max = 255, message = "领用喷头序列号最多255字符")
    private String retWarehouseSprinklerSerial;

    @Schema(description = "返仓喷头型号")
    @Length(max = 255, message = "返仓喷头型号最多255字符")
    private String retWarehouseSprinklerModel;

    @Schema(description = "返仓机台")
    @Length(max = 255, message = "返仓机台最多255字符")
    private String retWarehouseMachine;

    @Schema(description = "返仓颜色")
    @Length(max = 255, message = "领用颜色最多255字符")
    private String retWarehouseColor;

    @Schema(description = "返仓位置",
            minimum = "0",      // Swagger 文档显示最小值
            maximum = "40",     // Swagger 文档显示最大值
            example = "10"
    )
    @Min(0)
    @Max(40)
    private Byte retWarehousePosition;

    @Schema(description = "返仓喷头类型")
    private Boolean retWarehouseSprinklerIsNew;

    @Schema(description = "返仓原因")
    @Length(max = 255, message = "返仓原因最多255字符")
    private String retWarehouseReason;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID", hidden = true)
    private Long createUserId;

    @Schema(description = "创建人", hidden = true)
    private String createUserName;

}

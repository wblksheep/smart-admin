package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 领用与返仓记录
 * @Author 海印: 芦苇
 */
@Data
public class AllocationRetWarehouseVO {

    @Schema(description = "领用与返仓信息ID")
    private Long allocationRetWarehouseId;

    @Schema(description = "领用与返仓信息记录ID")
    private Long recordId;

    @Schema(description = "领用喷头ID")
    private Long allocateSprinklerId;

    @Schema(description = "领用喷头序列号")
    private String allocateSprinklerSerial;

    @Schema(description = "领用喷头型号")
    private String allocateSprinklerModel;

    @Schema(description = "领用用途")
    private String allocatePurpose;

    @Schema(description = "领用颜色")
    private String allocateColor;

    @Schema(description = "领用位置")
    private Byte allocatePosition;

    @Schema(description = "领用喷头类型")
    private Boolean allocateSprinklerIsNew;

    @Schema(description = "返仓喷头ID")
    private Long retWarehouseSprinklerId;

    @Schema(description = "返仓喷头序列号")
    private String retWarehouseSprinklerSerial;

    @Schema(description = "返仓喷头型号")
    private String retWarehouseSprinklerModel;

    @Schema(description = "返仓机台")
    private String retWarehouseMachine;

    @Schema(description = "返仓颜色")
    private String retWarehouseColor;

    @Schema(description = "返仓位置")
    private Byte retWarehousePosition;

    @Schema(description = "返仓喷头类型")
    private Boolean retWarehouseSprinklerIsNew;

    @Schema(description = "返仓原因")
    private String retWarehouseReason;

    @Schema(description = "具体原因")
    private String retWarehouseRealReason;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "审核状态")
    private Byte status;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}

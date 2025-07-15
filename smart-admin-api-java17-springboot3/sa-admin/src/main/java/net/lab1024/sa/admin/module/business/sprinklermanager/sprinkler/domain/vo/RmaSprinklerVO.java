package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * rma喷头所有信息
 * @Author 海印: 芦苇
 */
@Data
public class RmaSprinklerVO extends BaseSprinklerVO{

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "返修日期")
    private LocalDate retMaintainenceDate;

    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @Schema(description = "返修客户")
    private String customer;

    @Schema(description = "所在仓")
    private Byte status;

    @Schema(description = "RMA地点")
    private String rmaPosition;

    @Schema(description = "系统核对")
    private String warehouseCheck;

    @Schema(description = "喷头详情")
    private String sprinklerDetail;

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

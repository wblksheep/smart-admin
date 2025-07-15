package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 破损仓喷头所有信息
 * @Author 海印: 芦苇
 */
@Data
public class DamagedSprinklerVO extends BaseSprinklerVO{

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @Schema(description = "所在仓")
    private Byte status;

    @Schema(description = "备注1")
    private String note1;

    @Schema(description = "破损原因分类")
    private String damagedReasonType;

    @Schema(description = "具体破损原因")
    private String realDamagedReason;

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

package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 维修记录
 * @Author 海印: 芦苇
 */
@Data
public class MaintainingRecordVO {

    @Schema(description = "维修信息记录ID")
    private Long recordId;

    @Schema(description = "返修日期")
    private LocalDate retMaintainenceDate;

    @Schema(description = "喷头ID")
    private String sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "返修原因")
    private String retMaintainenceReason;

    @Schema(description = "具体原因")
    private String realReason;


    @Schema(description = "返修客户")
    private String customer;

    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @Schema(description = "返仓类型")
    private String retWarehouseType;

    @Schema(description = "领用是否有限制")
    private String allocateLimitation;

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

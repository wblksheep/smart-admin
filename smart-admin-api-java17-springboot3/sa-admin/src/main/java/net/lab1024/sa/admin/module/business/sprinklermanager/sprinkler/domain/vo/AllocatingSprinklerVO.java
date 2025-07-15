package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 可用仓喷头所有信息
 *
 * @Author 海印: 芦苇
 */
@Data
public class AllocatingSprinklerVO extends BaseSprinklerVO {

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "喷头型号")
    private String sprinklerModel;

    @Schema(description = "领用日期")
    private LocalDate allocateDate;

    @Schema(description = "领用人")
    private String allocateUser;

    @Schema(description = "新旧喷头")
    private Boolean isNew;

    @Schema(description = "所在仓")
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

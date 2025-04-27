package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 可用仓喷头所有信息
 * @Author 海印: 芦苇
 */
@Data
public class UsableSprinklerVO {

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "喷头型号")
    private String sprinklerModel;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @Schema(description = "入仓日期")
    private LocalDate warehouseDate;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "新旧喷头")
    private Boolean isNew;

    @Schema(description = "返仓日期")
    private LocalDate retWarehouseDate;

    @Schema(description = "领用是否有限制")
    private String allocateLimitation;

    @Schema(description = "领用时备注1")
    private String allocateNote1;

    @DataTracerFieldLabel("喷头详情")
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

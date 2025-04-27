package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 喷头信息
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerVO {

    @Schema(description = "喷头ID")
    private Long sprinklerId;

    @Schema(description = "购入日期（合同编号）")
    private String purchaseDateContractNumber;

    @Schema(description = "喷头型号")
    private String sprinklerModel;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @Schema(description = "入仓日期")
    private LocalDate warehouseDate;

    @Schema(description = "领用日期")
    private LocalDate allocateDate;

    @Schema(description = "领用人")
    private String allocateUser;

    @Schema(description = "领用用途")
    private String allocatePurpose;

    @Schema(description = "位置")
    private String allocatePosition;

    @Schema(description = "电压")
    private Float voltage;

    @Schema(description = "jetsout")
    private Byte jetsout;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "所在仓status")
    private Byte status;

    @Schema(description = "新旧喷头")
    private Boolean isNew;

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

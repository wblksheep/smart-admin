package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SprinklerQueryForm extends PageParam {


    @Schema(description = "购入日期（合同编号）")
    private String purchaseDateContractNumber;

    @Schema(description = "喷头型号")
    private String sprinklerModel;

    @Schema(description = "喷头序列号")
    private String sprinklerSerial;

    @Schema(description = "发货日期开始时间")
    private LocalDate shippingDateStartTime;

    @Schema(description = "发货日期结束时间")
    private LocalDate shippingDateEndTime;

    @Schema(description = "入仓日期开始时间")
    private LocalDate warehouseDateStartTime;

    @Schema(description = "入仓日期结束时间")
    private LocalDate warehouseDateEndTime;

    @Schema(description = "领用日期开始时间")
    private LocalDate allocateDateStartTime;

    @Schema(description = "领用日期结束时间")
    private LocalDate allocateDateEndTime;

    @Schema(description = "领用人")
    private String allocateUser;

    @Schema(description = "领用用途")
    private String allocatePurpose;

    @Schema(description = "位置")
    private String allocatePosition;

//    @Schema(description = "电压")
//    private Float voltage;
//
//    @Schema(description = "jetsout")
//    private Byte jetsout;

    @Schema(description = "历史")
    private String history;

    @Schema(description = "所在仓status")
    private Byte status;

    @Schema(description = "新旧喷头")
    private Boolean isNew;

    @Schema(description = "喷头详情")
    private String sprinklerDetail;

//    @Schema(description = "开始时间")
//    private LocalDate startTime;
//
//    @Schema(description = "结束时间")
//    private LocalDate endTime;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}



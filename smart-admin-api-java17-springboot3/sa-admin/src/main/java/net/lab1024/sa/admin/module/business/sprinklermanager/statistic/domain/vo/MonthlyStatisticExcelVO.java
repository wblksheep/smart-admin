package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo;

import lombok.Data;

@Data
public class MonthlyStatisticExcelVO {

    private String retMaintainenceReason;

    private Integer monthBegin;

    private Integer monthIn;

    private Integer monthMultiIn;

    private Integer monthCleaningFromDamaged;

    private Integer retUsable;

    private Integer retDamaged;

    private Integer retRma;

    private Integer injectedCount;

    private Integer remainInMaintaining;

    private Integer monthEnd;


}

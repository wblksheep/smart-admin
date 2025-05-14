package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MonthlyDamagedSprinklerUsingDaysVO {

    private String sprinklerSerial;

    private LocalDate allocateDate;

    private LocalDate retWarehouseDate;

    private Integer usingDays;
}

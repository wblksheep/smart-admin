package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MonthlyStatisticSheetQueryForm {
    private LocalDate startDate;
    private LocalDate endDate;
    private String machineType;
}

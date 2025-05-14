package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExcelGeneratorFactory {
    @Autowired
    private StatisticRepository statisticRepository;

    public MonthlyStatisticSheet createMonthlyStatisticSheet(){
        return new MonthlyStatisticSheet(statisticRepository);
    }

    public MonthlyMachineRetWarehouseSprinklerStatisticSheet createMonthlyMachineRetWarehouseSprinklerStatisticSheet() {
        return new MonthlyMachineRetWarehouseSprinklerStatisticSheet(statisticRepository);
    }

    public MonthlyDamagedSprinklerSheet createMonthlyDamagedSprinklerSheet() {
        return new MonthlyDamagedSprinklerSheet(statisticRepository);
    }

    public MonthlyMachineSprinklerMaintainingDetailSheet createMonthlyMachineSprinklerMaintainingDetailSheet() {
        return new MonthlyMachineSprinklerMaintainingDetailSheet(statisticRepository);
    }

    public MonthlyRetUsableSprinklerSheet createMonthlyRetUsableSprinklerSheet() {
        return new MonthlyRetUsableSprinklerSheet(statisticRepository);
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.dao.StatisticDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.entity.StatisticEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StatisticRepository extends IService<StatisticEntity> {
    List<MaintainingRecordEntity> batchQueryMachineData(LocalDate startDate,
                                                        LocalDate endDate,
                                                        String[] machines,
                                                        String[] reasons);

    List<MaintainingRecordEntity> batchQueryMachineData2(LocalDate startDate,
                                                        LocalDate endDate,
                                                        String[] machines,
                                                        String[] reasons);

    List<MaintainingSprinklerEntity> batchQuerySprinklerData(LocalDate startDate,
                                                            LocalDate endDate,
                                                            String[] machines,
                                                            String[] reasons);

    List<MaintainingRecordEntity> batchQueryMachineData3(LocalDate startDate,
                                                        LocalDate endDate,
                                                        String machine,
                                                        String reason);

    List<MaintainingSprinklerEntity> batchQuerySprinklerData2(LocalDate startDate,
                                                LocalDate endDate,
                                                String monthlymachineretwarehousesprinklerheader,
                                                String reason);

    List<MaintainingRecordEntity> batchQueryMachineData4(LocalDate startDate, LocalDate endDate);

    List<MaintainingRecordEntity> batchQueryMachineData5(LocalDate startDate, LocalDate endDate, String machine, String type);

    List<MaintainingRecordEntity> batchQueryMachineData6(LocalDate startDate, LocalDate endDate, String machine);

    List<MaintainingSprinklerEntity> batchQuerySprinklerData3(LocalDate startDate, LocalDate endDate, String machine);
}

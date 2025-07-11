package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.entity.StatisticEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyDamagedSprinklerUsingDaysVO;

import java.time.LocalDate;
import java.util.List;

public interface StatisticRepository extends IService<StatisticEntity> {
    List<MaintainingRecordEntity> batchQueryMachineData(LocalDate startDate,
                                                        LocalDate endDate,
                                                        String[] machines,
                                                        String[] reasons, String machineType);

    List<MaintainingRecordEntity> batchQueryMachineData2(LocalDate startDate,
                                                         LocalDate endDate,
                                                         String[] machines,
                                                         String[] reasons,
                                                         String machineType
    );

    List<MaintainingSprinklerEntity> batchQuerySprinklerData(LocalDate startDate,
                                                             LocalDate endDate,
                                                             String[] machines,
                                                             String[] reasons, String machineType, Boolean deletedFlag);

    List<MaintainingRecordEntity> batchQueryMachineData3(LocalDate startDate,
                                                         LocalDate endDate,
                                                         String machine,
                                                         String reason,
                                                         String machineType,
                                                         Boolean deletedFlag
    );

    List<MaintainingSprinklerEntity> batchQuerySprinklerData2(LocalDate startDate,
                                                              LocalDate endDate,
                                                              String machine,
                                                              String reason,
                                                              String machineType,
                                                              Boolean deletedFlag);

    List<MaintainingRecordEntity> batchQueryMachineData4(LocalDate startDate, LocalDate endDate, String machineType, Boolean deletedFlag);

    List<MaintainingRecordEntity> batchQueryMachineData5(LocalDate startDate, LocalDate endDate, String machine, String type);

    List<MaintainingRecordEntity> batchQueryMachineData6(LocalDate startDate, LocalDate endDate, String machine, String machineType, Boolean deletedFlag);

    List<MaintainingSprinklerEntity> batchQuerySprinklerData3(LocalDate startDate, LocalDate endDate, String machine, String machineType, Boolean deletedFlag);

    List<DamagedSprinklerEntity> batchQueryDamagedSprinklerData(LocalDate startDate, LocalDate endDate, String machineType, Boolean deletedFlag);

    List<MaintainingRecordEntity> batchQueryMachineData7(LocalDate startDate, LocalDate endDate, String limit, String machineType, Boolean deletedFlag);

    List<MonthlyDamagedSprinklerUsingDaysVO> listByRetDamagedAndDate(LocalDate startDate, LocalDate endDate, String machineType, Boolean deletedFlag);

    List<MachineEntity> batchQueryMachineName(String samba);
}

package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.repository.MachineRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.DamagedSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.dao.StatisticDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.entity.StatisticEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyDamagedSprinklerUsingDaysVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticRepositoryImpl extends ServiceImpl<StatisticDao, StatisticEntity> implements StatisticRepository {
    @Resource
    private MaintainingRecordRepository maintainingRecordRepository;

    @Resource
    private MaintainingSprinklerRepository maintainingSprinklerRepository;

    @Resource
    private MachineRepository machineRepository;

    @Resource
    private DamagedSprinklerRepository damagedSprinklerRepository;


    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData(LocalDate startDate, LocalDate endDate, String[] machines, String[] reasons, String machineType) {
        return maintainingRecordRepository.list(
                buildMachineQuery(startDate, endDate, machines, reasons, machineType)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData2(LocalDate startDate, LocalDate endDate, String[] machines, String[] reasons, String machineType) {
        return maintainingRecordRepository.list(
                buildMachineQuery2(startDate, endDate, machines, reasons, machineType)
        );
    }

    @Override
    public List<MaintainingSprinklerEntity> batchQuerySprinklerData(LocalDate startDate, LocalDate endDate, String[] machines, String[] reasons, String machineType, Boolean deletedFlag) {
        return maintainingSprinklerRepository.list(
                buildSprinklerQuery(startDate, endDate, machines, reasons, machineType, deletedFlag)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData3(LocalDate startDate, LocalDate endDate, String machine, String reason, String machineType, Boolean deletedFlag) {
        return maintainingRecordRepository.list(
                buildMachineQuery3(startDate, endDate, machine, reason, machineType, deletedFlag)
        );
    }

    @Override
    public List<MaintainingSprinklerEntity> batchQuerySprinklerData2(LocalDate startDate, LocalDate endDate, String machine, String reason, String machineType, Boolean deletedFlag) {
        return maintainingSprinklerRepository.list(
                buildSprinklerQuery2(startDate, endDate, machine, reason, machineType, deletedFlag)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData4(LocalDate startDate, LocalDate endDate, String machineType, Boolean deletedFlag) {
        return maintainingRecordRepository.list(
                buildMachineQuery4(startDate, endDate, machineType, deletedFlag)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData5(LocalDate startDate, LocalDate endDate, String machine, String type) {
        return maintainingRecordRepository.list(
                buildMachineQuery5(startDate, endDate, machine, type)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData6(LocalDate startDate, LocalDate endDate, String machine, String machineType, Boolean deletedFlag) {
        return maintainingRecordRepository.list(
                buildMachineQuery6(startDate, endDate, machine, machineType, deletedFlag)
        );
    }

    @Override
    public List<MaintainingSprinklerEntity> batchQuerySprinklerData3(LocalDate startDate, LocalDate endDate, String machine, String machineType, Boolean deletedFlag) {
        return maintainingSprinklerRepository.list(
                buildSprinklerQuery3(startDate, endDate, machine, machineType, deletedFlag)
        );
    }

    @Override
    public List<DamagedSprinklerEntity> batchQueryDamagedSprinklerData(LocalDate startDate, LocalDate endDate, String machineType, Boolean deletedFlag) {
        return damagedSprinklerRepository.list(
                buildDamagedSprinklerQuery(startDate, endDate, machineType, deletedFlag)
        );
    }

    private LambdaQueryWrapper<DamagedSprinklerEntity> buildDamagedSprinklerQuery(LocalDate start, LocalDate end, String machineType, Boolean deletedFlag) {
        LambdaQueryWrapper<DamagedSprinklerEntity> lqw = new LambdaQueryWrapper<DamagedSprinklerEntity>()
                .ge(DamagedSprinklerEntity::getRetWarehouseDate, start)
                .le(DamagedSprinklerEntity::getRetWarehouseDate, end);
        if (machineType.equals("samba")) {
            lqw.like(DamagedSprinklerEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(DamagedSprinklerEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(DamagedSprinklerEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData7(LocalDate startDate, LocalDate endDate, String limit, String machineType, Boolean deletedFlag) {
        return maintainingRecordRepository.list(
                buildMachinequery7(startDate, endDate, limit, machineType, deletedFlag)
        );
    }

    @Override
    public List<MonthlyDamagedSprinklerUsingDaysVO> listByRetDamagedAndDate(LocalDate startDate, LocalDate endDate, String machineType, Boolean deletedFlag) {
        return this.getBaseMapper().queryByRetDamagedAndDate(startDate, endDate, machineType, deletedFlag);
    }

    @Override
    public List<MachineEntity> batchQueryMachineName(String type) {
        return machineRepository.getByMachineNames(type);
    }

    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachinequery7(LocalDate start, LocalDate end, String limit, String machineType, Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingRecordEntity> lqw = new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .eq(MaintainingRecordEntity::getAllocateLimitation, limit);
        if (machineType.equals("samba")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingRecordEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }


    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery6(LocalDate start, LocalDate end, String machine, String machineType, Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingRecordEntity> lqw = new LambdaQueryWrapper<MaintainingRecordEntity>()
                .gt(MaintainingRecordEntity::getRetWarehouseDate, end)
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .like(MaintainingRecordEntity::getCustomer, machine);
        if (machineType.equals("samba")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingRecordEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }

    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery5(LocalDate start, LocalDate end, String machine, String type) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .like(MaintainingRecordEntity::getCustomer, machine)
                .eq(MaintainingRecordEntity::getRetWarehouseType, type);
    }

    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery4(LocalDate start, LocalDate end, String machineType, Boolean deletedFlag) {

        LambdaQueryWrapper<MaintainingRecordEntity> lqw = new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .eq(MaintainingRecordEntity::getRetWarehouseType, "破损仓");
        if (machineType.equals("samba")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingRecordEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery(LocalDate start,
                                                                          LocalDate end,
                                                                          String[] machines,
                                                                          String[] reasons, String machineType) {
        LambdaQueryWrapper<MaintainingRecordEntity> lqw = new LambdaQueryWrapper<MaintainingRecordEntity>()
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .in(MaintainingRecordEntity::getRetMaintainenceReason, reasons);
        if (machineType.equals("samba")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "-");
        } else {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "66L");
        }
        return lqw;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery2(LocalDate start,
                                                                           LocalDate end,
                                                                           String[] machines,
                                                                           String[] reasons, String machineType) {
        LambdaQueryWrapper<MaintainingRecordEntity> lqw = new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .in(MaintainingRecordEntity::getRetMaintainenceReason, reasons);
        if (machineType.equals("samba")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "66L");
        }
        return lqw;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery3(LocalDate start,
                                                                           LocalDate end,
                                                                           String machine,
                                                                           String reason,
                                                                           String machineType,
                                                                           Boolean deletedFlag
    ) {

        LambdaQueryWrapper<MaintainingRecordEntity> lqw = new LambdaQueryWrapper<MaintainingRecordEntity>()
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .like(MaintainingRecordEntity::getCustomer, machine)
                .eq(MaintainingRecordEntity::getRetMaintainenceReason, reason);


        if (machineType.equals("samba")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingRecordEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingRecordEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingSprinklerEntity> buildSprinklerQuery(LocalDate start,
                                                                               LocalDate end,
                                                                               String[] machines,
                                                                               String[] reasons, String machineType, Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingSprinklerEntity> lqw = new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, start)
                .le(MaintainingSprinklerEntity::getRetMaintainenceDate, end)
                .in(MaintainingSprinklerEntity::getRetMaintainenceReason, reasons);

        if (machineType.equals("samba")) {
            lqw.like(MaintainingSprinklerEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingSprinklerEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingSprinklerEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingSprinklerEntity> buildSprinklerQuery2(LocalDate start,
                                                                                LocalDate end,
                                                                                String machine,
                                                                                String reason,
                                                                                String machineType,
                                                                                Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingSprinklerEntity> lqw = new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, start)
                .le(MaintainingSprinklerEntity::getRetMaintainenceDate, end)
                .like(MaintainingSprinklerEntity::getCustomer, machine)
                .eq(MaintainingSprinklerEntity::getRetMaintainenceReason, reason);

        if (machineType.equals("samba")) {
            lqw.like(MaintainingSprinklerEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingSprinklerEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingSprinklerEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;

    }

    private LambdaQueryWrapper<MaintainingSprinklerEntity> buildSprinklerQuery3(LocalDate start, LocalDate end, String machine, String machineType, Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingSprinklerEntity> lqw = new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, start)
                .le(MaintainingSprinklerEntity::getRetMaintainenceDate, end)
                .like(MaintainingSprinklerEntity::getCustomer, machine);

        if (machineType.equals("samba")) {
            lqw.like(MaintainingSprinklerEntity::getSprinklerSerial, "-");
        } else if (machineType.equals("se")) {
            lqw.like(MaintainingSprinklerEntity::getSprinklerSerial, "66L");
        }
        if (deletedFlag != null) {
            lqw.eq(MaintainingSprinklerEntity::getDeletedFlag, deletedFlag);
        }
        return lqw;
    }


}

package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.repository.MachineRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.dao.StatisticDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.entity.StatisticEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyDamagedSprinklerUsingDaysVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class StatisticRepositoryImpl extends ServiceImpl<StatisticDao, StatisticEntity> implements StatisticRepository {
    @Resource
    private MaintainingRecordRepository maintainingRecordRepository;

    @Resource
    private MaintainingSprinklerRepository maintainingSprinklerRepository;

    @Resource
    private MachineRepository machineRepository;


    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData(LocalDate startDate, LocalDate endDate, String[] machines, String[] reasons) {
        return maintainingRecordRepository.list(
                buildMachineQuery(startDate, endDate, machines, reasons)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData2(LocalDate startDate, LocalDate endDate, String[] machines, String[] reasons) {
        return maintainingRecordRepository.list(
                buildMachineQuery2(startDate, endDate, machines, reasons)
        );
    }

    @Override
    public List<MaintainingSprinklerEntity> batchQuerySprinklerData(LocalDate startDate, LocalDate endDate, String[] machines, String[] reasons) {
        return maintainingSprinklerRepository.list(
                buildSprinklerQuery(startDate, endDate, machines, reasons)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData3(LocalDate startDate, LocalDate endDate, String machine, String reason) {
        return maintainingRecordRepository.list(
                buildMachineQuery3(startDate, endDate, machine, reason)
        );
    }

    @Override
    public List<MaintainingSprinklerEntity> batchQuerySprinklerData2(LocalDate startDate, LocalDate endDate, String machine, String reason) {
        return maintainingSprinklerRepository.list(
                buildSprinklerQuery2(startDate, endDate, machine, reason)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData4(LocalDate startDate, LocalDate endDate) {
        return maintainingRecordRepository.list(
                buildMachineQuery4(startDate, endDate)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData5(LocalDate startDate, LocalDate endDate, String machine, String type) {
        return maintainingRecordRepository.list(
                buildMachineQuery5(startDate, endDate, machine, type)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData6(LocalDate startDate, LocalDate endDate, String machine) {
        return maintainingRecordRepository.list(
                buildMachineQuery6(startDate, endDate, machine)
        );
    }

    @Override
    public List<MaintainingSprinklerEntity> batchQuerySprinklerData3(LocalDate startDate, LocalDate endDate, String machine) {
        return maintainingSprinklerRepository.list(
                buildSprinklerQuery3(startDate, endDate, machine)
        );
    }

    @Override
    public List<MaintainingRecordEntity> batchQueryMachineData7(LocalDate startDate, LocalDate endDate, String limit) {
        return maintainingRecordRepository.list(
                buildMachinequery7(startDate, endDate, limit)
        );
    }

    @Override
    public List<MonthlyDamagedSprinklerUsingDaysVO> listByRetDamagedAndDate(LocalDate startDate, LocalDate endDate) {
        return this.getBaseMapper().queryByRetDamagedAndDate(startDate, endDate);
    }

    @Override
    public List<MachineEntity> batchQueryMachineName(String type) {
        return machineRepository.getByMachineNames(type);
    }

    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachinequery7(LocalDate start, LocalDate end, String limit) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .eq(MaintainingRecordEntity::getAllocateLimitation, limit);
    }

    private LambdaQueryWrapper<MaintainingSprinklerEntity> buildSprinklerQuery3(LocalDate start, LocalDate end, String machine) {
        return new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, start)
                .le(MaintainingSprinklerEntity::getRetMaintainenceDate, end)
                .like(MaintainingSprinklerEntity::getCustomer, machine);
    }

    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery6(LocalDate start, LocalDate end, String machine) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetWarehouseDate, end)
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .like(MaintainingRecordEntity::getCustomer, machine);
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

    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery4(LocalDate start, LocalDate end) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .eq(MaintainingRecordEntity::getRetWarehouseType, "破损仓");

    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery(LocalDate start,
                                                                          LocalDate end,
                                                                          String[] machines,
                                                                          String[] reasons) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .le(MaintainingRecordEntity::getRetWarehouseDate, end)
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .in(MaintainingRecordEntity::getRetMaintainenceReason, reasons);
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery2(LocalDate start,
                                                                           LocalDate end,
                                                                           String[] machines,
                                                                           String[] reasons) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .in(MaintainingRecordEntity::getRetMaintainenceReason, reasons)
                .like(MaintainingRecordEntity::getSprinklerSerial, "-")
                ;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingRecordEntity> buildMachineQuery3(LocalDate start,
                                                                           LocalDate end,
                                                                           String machine,
                                                                           String reason) {
        return new LambdaQueryWrapper<MaintainingRecordEntity>()
                .le(MaintainingRecordEntity::getRetMaintainenceDate, end)
                .ge(MaintainingRecordEntity::getRetMaintainenceDate, start)
                .ge(MaintainingRecordEntity::getRetWarehouseDate, start)
                .like(MaintainingRecordEntity::getCustomer, machine)
                .eq(MaintainingRecordEntity::getRetMaintainenceReason, reason);

    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingSprinklerEntity> buildSprinklerQuery(LocalDate start,
                                                                               LocalDate end,
                                                                               String[] machines,
                                                                               String[] reasons) {
        return new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, start)
                .le(MaintainingSprinklerEntity::getRetMaintainenceDate, end)
                .in(MaintainingSprinklerEntity::getRetMaintainenceReason, reasons)
                .like(MaintainingSprinklerEntity::getSprinklerSerial, "-")
                ;
    }

    // 通用查询条件构建器
    private LambdaQueryWrapper<MaintainingSprinklerEntity> buildSprinklerQuery2(LocalDate start,
                                                                                LocalDate end,
                                                                                String machine,
                                                                                String reason) {
        return new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, start)
                .le(MaintainingSprinklerEntity::getRetMaintainenceDate, end)
                .like(MaintainingSprinklerEntity::getCustomer, machine)
                .eq(MaintainingSprinklerEntity::getRetMaintainenceReason, reason);

    }


}

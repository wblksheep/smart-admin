package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import lombok.Getter;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MonthlyDamagedSprinklerSheet extends SheetGenerator {

    private static final String[] MONTHLYDAMAGEDSPRINKLERHEADERSVO = {"年份月份", "喷头序列号", "破损机台", "破损原因", "破损日期"};
    private StatisticRepository statisticRepository;

    public MonthlyDamagedSprinklerSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate, String machineType) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("当月破损仓喷头信息").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate, machineType), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate, String machineType) {
        Month month = startDate.getMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter); // 输出格式如"2023-10"
        List<List<Object>> data = new ArrayList<>();
        // 1.1 批量预加载破损仓记录数据（避免循环内多次查询）返仓记录相关
        List<MaintainingRecordEntity> reasonRecords = statisticRepository.batchQueryMachineData4(startDate, endDate, machineType, Boolean.FALSE);
        List<DamagedSprinklerEntity> damagedSprinklers = statisticRepository.batchQueryDamagedSprinklerData(startDate, endDate, machineType, Boolean.FALSE);

        // 3. 定义分组容器类
        @Getter
        class SprinklerGroup {
            private MaintainingRecordEntity record;
            private DamagedSprinklerEntity damagedSprinkler;

            public void setRecord(MaintainingRecordEntity record) {
                this.record = record;
            }

            public void setDamaged(DamagedSprinklerEntity damaged) {
                this.damagedSprinkler = damaged;
            }
        }

        // 定义分组容器
        Map<String, SprinklerGroup> sprinklerGroupMap = new HashMap<>();

        // 1. 处理维护记录
        for (MaintainingRecordEntity record : reasonRecords) {
            String serial = record.getSprinklerSerial();
            sprinklerGroupMap
                    .computeIfAbsent(serial, k -> new SprinklerGroup())
                    .setRecord(record);
        }

        // 2. 处理破损记录
        for (DamagedSprinklerEntity damaged : damagedSprinklers) {
            String serial = damaged.getSprinklerSerial();
            sprinklerGroupMap
                    .computeIfAbsent(serial, k -> new SprinklerGroup())
                    .setDamaged(damaged);
        }

        for (String serial : sprinklerGroupMap.keySet()) {
            SprinklerGroup sprinklerGroup = sprinklerGroupMap.get(serial);
            MaintainingRecordEntity record = sprinklerGroup.getRecord();
            DamagedSprinklerEntity damagedSprinkler = sprinklerGroup.getDamagedSprinkler();
            List<Object> row = new ArrayList<>();
            row.add(formattedDate);
            row.add(serial);
            row.add(record.getCustomer());
            row.add(damagedSprinkler.getDamagedReasonType());
            row.add(damagedSprinkler.getRetWarehouseDate());
            data.add(row);
        }
        return data;
    }


    private List<List<String>> buildComplexHeader() {
        return Arrays.stream(MONTHLYDAMAGEDSPRINKLERHEADERSVO)
                .map(Collections::singletonList) // 单列转List
                .collect(Collectors.toList());
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonthlyDamagedSprinklerSheet extends SheetGenerator{

    private static final String[] MONTHLYDAMAGEDSPRINKLERHEADERSVO = {"年份月份", "喷头序列号", "破损机台", "破损原因", "破损日期"};
    private StatisticRepository statisticRepository;

    public MonthlyDamagedSprinklerSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("当月破损仓喷头信息").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate) {
        Month month = startDate.getMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter); // 输出格式如"2023-10"
        List<List<Object>> data = new ArrayList<>();
        // 1.1 批量预加载破损仓记录数据（避免循环内多次查询）返仓记录相关
        List<MaintainingRecordEntity> reasonRecords = statisticRepository.batchQueryMachineData4(startDate, endDate);
        for(MaintainingRecordEntity maintainingRecordEntity : reasonRecords) {
            List<Object> row = new ArrayList<>();
            row.add(formattedDate);
            row.add(maintainingRecordEntity.getSprinklerSerial());
            row.add(maintainingRecordEntity.getCustomer());
            row.add(maintainingRecordEntity.getRetMaintainenceReason());
            row.add(maintainingRecordEntity.getRetWarehouseDate());
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

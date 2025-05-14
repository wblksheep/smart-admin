package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MonthlyRetUsableSprinklerSheet extends SheetGenerator{

    private static final String[] MONTHLYRETUSABLESPRINKLERHEADERSVO = {"年份月份", "有限制数量", "正常使用数量", "合计"};
    private static final String[] MONTHLYRETUSABLESPRINKLERHEADERS = {"有", "无"};

    private StatisticRepository statisticRepository;

    public MonthlyRetUsableSprinklerSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("每月返可用喷头统计").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate) {
        List<List<Object>> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter); // 输出格式如"2023-10"
        row.add(formattedDate);
        Long count1 = (long) statisticRepository.batchQueryMachineData7(startDate, endDate, MONTHLYRETUSABLESPRINKLERHEADERS[0])
                .size();
        row.add(count1);
        Long count2 = (long) statisticRepository.batchQueryMachineData7(startDate, endDate, MONTHLYRETUSABLESPRINKLERHEADERS[1])
                .size();
        row.add(count2);
        row.add(count1 + count2);
        data.add(row);
        return data;
    }

    private List<List<String>> buildComplexHeader() {
        return Arrays.stream(MONTHLYRETUSABLESPRINKLERHEADERSVO)
                .map(Collections::singletonList) // 单列转List
                .collect(Collectors.toList());
    }
}

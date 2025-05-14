package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyDamagedSprinklerUsingDaysVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MonthlyDamagedSprinklerUsingDaysSheet extends SheetGenerator {

    private StatisticRepository statisticRepository;

    private static final String[] MONTHLYDAMAGEDSPRINKLERUSINGDAYSHEADERSVO = {"年份月份","喷头序列号", "领用日期", "破损日期", "使用时间"};


    public MonthlyDamagedSprinklerUsingDaysSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("每月报废喷头使用时间").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter);
        List<List<Object>> data = new ArrayList<>();
        List<MonthlyDamagedSprinklerUsingDaysVO> queryVOs = statisticRepository.listByRetDamagedAndDate(startDate, endDate);
        for(int i=0;i<queryVOs.size();i++){
            List<Object> row = new ArrayList<>();
            row.add(formattedDate);
            row.add(queryVOs.get(i).getSprinklerSerial());
            row.add(queryVOs.get(i).getAllocateDate());
            row.add(queryVOs.get(i).getRetWarehouseDate());
            row.add(queryVOs.get(i).getUsingDays());
            data.add(row);
        }
        return data;


    }

    private List<List<String>> buildComplexHeader() {
        return Arrays.stream(MONTHLYDAMAGEDSPRINKLERUSINGDAYSHEADERSVO).map(Collections::singletonList) // 单列转List
                .collect(Collectors.toList());
    }
}

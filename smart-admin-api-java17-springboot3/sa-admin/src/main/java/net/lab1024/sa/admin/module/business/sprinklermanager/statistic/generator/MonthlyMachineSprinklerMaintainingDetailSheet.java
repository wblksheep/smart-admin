package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MonthlyMachineSprinklerMaintainingDetailSheet extends SheetGenerator {

    private StatisticRepository statisticRepository;

    //    private static final String[] MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO = {"年份月份", "维护结果分类", "大昌德1#", "大昌德2#", "宇华1#", "宇华2#", "宇华3#", "华都1#", "华都2#", "鸿大大昌祥1#", "鸿大大昌祥2#", "鸿大大昌祥扫描机", "鸿大北海1#", "鸿大北海2#", "鸿大北海3#", "吉盛祥1#", "吉盛祥2#", "绍肖1#", "绍肖2#", "绍肖3#", "绍肖4#", "沙印1#", "沙印2#", "宏强1#", "宏强2#", "宏强3#", "稽山1#", "稽山2#", "盛兴1#", "盛兴2#", "超超1#", "超超2#", "宜滨1#", "宜滨2#", "恒晨1#", "恒晨3C1#", "洁彩坊一号车间1#大机", "洁彩坊二号车间1#大机", "金楚1#大机", "鸿大北海1#小机", "鸿大北海2#小机", "鸿大北海3#小机", "鸿大北海5#小机", "轮转机", "其他", "共计"};
//    private static final String[] MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS = {"大昌德1#", "大昌德2#", "宇华1#", "宇华2#", "宇华3#", "华都1#", "华都2#", "大昌祥1#", "大昌祥2#", "大昌祥扫描机", "鸿大北海1#大机", "鸿大北海2#大机", "鸿大北海3#大机", "吉盛祥1#", "吉盛祥2#", "绍肖1#", "绍肖2#", "绍肖3#", "绍肖4#", "沙印1#", "沙印2#", "宏强1#", "宏强2#", "宏强3#", "稽山1#", "稽山2#", "盛兴1#", "盛兴2#", "超超1#", "超超2#", "宜滨1#", "宜滨2#", "恒晨1#", "恒晨3C1#", "洁彩纺一号车间", "洁彩纺二号车间", "金楚1#大机", "鸿大北海1#小机", "鸿大北海2#小机", "鸿大北海3#小机", "鸿大北海5#小机", "轮转机", "其他", "共计"};
    private static final String[] MONTHLYMAINTAININGRESULTTYPESVO = {"清洗好可用", "破损", "RMA", "维护中", "小计"};
    private static final String[] MAINTAININGRESULTTYPES = {"可用仓", "破损仓", "RMA", "维护中"};

    public MonthlyMachineSprinklerMaintainingDetailSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate, String machineType) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("每月各机台喷头维护明细").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate, machineType), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate, String machineType) {
        List<List<Object>> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter); // 输出格式如"2023-10"
        List<Object> rowSummary = new ArrayList<>();
        rowSummary.add(formattedDate);
        rowSummary.add(MONTHLYMAINTAININGRESULTTYPESVO[MONTHLYMAINTAININGRESULTTYPESVO.length - 1]);

        List<MachineEntity> machines = statisticRepository.batchQueryMachineName("samba");

        String[] MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS = new String[machines.size() + 2];
        int cnt = 0;
        for (; cnt < machines.size(); cnt++) {
            MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS[cnt] = machines.get(cnt).getMachineName();
        }
        MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS[cnt++] = "其他";
        MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS[cnt++] = "共计";


        Long[] summaryTotal = new Long[MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS.length];
        Arrays.fill(summaryTotal, 0L);
        for (int i = 0; i < MONTHLYMAINTAININGRESULTTYPESVO.length - 2; i++) {
            List<Object> row = new ArrayList<>();
            row.add(formattedDate);
            row.add(MONTHLYMAINTAININGRESULTTYPESVO[i]);
            Long total = 0L;
            for (int j = 0; j < MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS.length - 1; j++) {
                Long value = statisticRepository.batchQueryMachineData5(
                        startDate, endDate, MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS[j], MAINTAININGRESULTTYPES[i]
                ).stream().count();
                row.add(value);
                summaryTotal[j] += value;
                total += value;
            }
            // 共计列自动计算总和
            row.add(total);
            summaryTotal[summaryTotal.length - 1] += total;
            data.add(row);
        }
        List<Object> row = new ArrayList<>();
        row.add(formattedDate);
        row.add(MONTHLYMAINTAININGRESULTTYPESVO[MONTHLYMAINTAININGRESULTTYPESVO.length - 2]);
        Long total = 0L;
        for (int j = 0; j < MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS.length - 1; j++) {
            Long count1 = statisticRepository.batchQueryMachineData6(
                    startDate, endDate, MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS[j]
            ).stream().count();
            Long count2 = statisticRepository.batchQuerySprinklerData3(
                    startDate, endDate, MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERS[j]
            ).stream().count();
            Long value = count1 + count2;
            row.add(value);
            summaryTotal[j] += value;
            total += value;
        }
        // 共计列自动计算总和
        row.add(total);
        summaryTotal[summaryTotal.length - 1] += total;
        data.add(row);
        for (int i = 0; i < summaryTotal.length; i++) {
            rowSummary.add(summaryTotal[i]);
        }
        data.add(rowSummary);
        return data;
    }

    private List<List<String>> buildComplexHeader() {
        List<MachineEntity> machines = statisticRepository.batchQueryMachineName("samba");

        String[] MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO = new String[2 + machines.size() + 2];
        MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO[0] = "年份月份";
        MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO[1] = "维护结果分类";
        // 使用索引直接填充机台名称
        for (int i = 0; i < machines.size(); i++) {
            MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO[2 + i] = machines.get(i).getMachineName();
        }
        // 直接计算末尾位置
        int lastIndex = 2 + machines.size();
        MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO[lastIndex] = "其他";
        MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO[lastIndex + 1] = "共计";

        return Arrays.stream(MONTHLYMACHINESPRINKLERMAINTAININGDETAILHEADERSVO)
                .map(Collections::singletonList) // 单列转List
                .collect(Collectors.toList());
    }
}

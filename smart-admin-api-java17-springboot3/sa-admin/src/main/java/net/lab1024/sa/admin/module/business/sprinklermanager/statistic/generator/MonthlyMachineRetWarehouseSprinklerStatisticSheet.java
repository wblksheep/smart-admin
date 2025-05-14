package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 每月机台服务返仓喷头统计Sheet实现
public class MonthlyMachineRetWarehouseSprinklerStatisticSheet extends SheetGenerator {

    private StatisticRepository statisticRepository;

    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERSVO = {"年份月份", "分类", "大昌德1#", "大昌德2#", "宇华1#", "宇华2#", "宇华3#", "华都1#", "华都2#", "鸿大大昌祥1#", "鸿大大昌祥2#", "鸿大大昌祥扫描机", "鸿大北海1#", "鸿大北海2#", "鸿大北海3#", "吉盛祥1#", "吉盛祥2#", "绍肖1#", "绍肖2#", "绍肖3#", "绍肖4#", "沙印1#", "沙印2#", "宏强1#", "宏强2#", "宏强3#", "稽山1#", "稽山2#", "盛兴1#", "盛兴2#", "超超1#", "超超2#", "宜滨1#", "宜滨2#", "恒晨1#", "恒晨3C1#", "洁彩坊一号车间1#大机", "洁彩坊二号车间1#大机", "金楚1#大机", "鸿大北海1#小机", "鸿大北海2#小机", "鸿大北海3#小机", "鸿大北海5#小机", "轮转机", "其他", "共计"};
    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机喷头", "其他原因说明", "小计"};
    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS = {"大昌德1#", "大昌德2#", "宇华1#", "宇华2#", "宇华3#", "华都1#", "华都2#", "大昌祥1#", "大昌祥2#", "大昌祥扫描机", "鸿大北海1#大机", "鸿大北海2#大机", "鸿大北海3#大机", "吉盛祥1#", "吉盛祥2#", "绍肖1#", "绍肖2#", "绍肖3#", "绍肖4#", "沙印1#", "沙印2#", "宏强1#", "宏强2#", "宏强3#", "稽山1#", "稽山2#", "盛兴1#", "盛兴2#", "超超1#", "超超2#", "宜滨1#", "宜滨2#", "恒晨1#", "恒晨3C1#", "洁彩纺一号车间", "洁彩纺二号车间", "金楚1#大机", "鸿大北海1#小机", "鸿大北海2#小机", "鸿大北海3#小机", "鸿大北海5#小机", "轮转机", "其他", "共计"};
    private static final String[] RETMAINTAINENCEREASONTYPES = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损", "漏气", "内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};

    public MonthlyMachineRetWarehouseSprinklerStatisticSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("每月机台服务返仓喷头统计").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate) {
        List<List<Object>> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter); // 输出格式如"2023-10"
        List<Object> rowSummary = new ArrayList<>();
        rowSummary.add(formattedDate);
        rowSummary.add(MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO[MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO.length - 1]);
        Long[] summaryTotal = new Long[MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS.length];
        Arrays.fill(summaryTotal, 0L);

        // 生成12个月数据（可调整range参数修改数据量）
        IntStream.range(0, MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO.length - 1).forEach(i -> {

            String reason = RETMAINTAINENCEREASONTYPES[i];
            List<Object> row = new ArrayList<>();

            // 年份月份列："2023-01"格式
            row.add(formattedDate);

            // 分类列：交替显示两种分类
            row.add(MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO[i]);

            Long total = 0L;
            for (int j = 0; j < MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS.length - 1; j++) {
                Long count1 = statisticRepository.batchQueryMachineData3(
                        startDate, endDate, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS[j], reason
                ).stream().count();

                Long count2 = statisticRepository.batchQuerySprinklerData2(
                        startDate, endDate, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS[j], reason
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
        });
        for (int i = 0; i < summaryTotal.length; i++) {
            rowSummary.add(summaryTotal[i]);
        }
        data.add(rowSummary);
        return data;
    }

    private List<List<String>> buildComplexHeader() {
        return Arrays.stream(MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERSVO)
                .map(Collections::singletonList) // 单列转List
                .collect(Collectors.toList());
    }
}

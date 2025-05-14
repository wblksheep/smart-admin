package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyStatisticExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.repository.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 月度统计Sheet实现
public class MonthlyStatisticSheet extends SheetGenerator {


    private static final String[] RETMAINTAINENCEREASONTYPES = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损", "漏气", "内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final String[] RETMAINTAINENCEREASONTYPESVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};

    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS = {"大昌德1#", "大昌德2#", "宇华1#", "宇华2#", "宇华3#", "华都1#", "华都2#", "大昌祥1#", "大昌祥2#", "大昌祥扫描机", "鸿大北海1#大机", "鸿大北海2#大机", "鸿大北海3#大机", "吉盛祥1#", "吉盛祥2#", "绍肖1#", "绍肖2#", "绍肖3#", "绍肖4#", "沙印1#", "沙印2#", "宏强1#", "宏强2#", "宏强3#", "稽山1#", "稽山2#", "盛兴1#", "盛兴2#", "超超1#", "超超2#", "宜滨1#", "宜滨2#", "恒晨1#", "恒晨3C1#", "洁彩纺一号车间1#大机", "洁彩纺二号车间1#大机", "金楚1#大机", "鸿大北海1#小机", "鸿大北海2#小机", "鸿大北海3#小机", "鸿大北海5#小机", "轮转机", "其他", "共计"};

    // 暴力生成分类数据池
    private static final Integer[][] MONTHBEGIN = {
            {13, 9, 20, 2, 9, 2, 10, 0, 8, 1, 0, 8, 20, 102},
            {5, 3, 8, 7, 9, 0, 6, 0, 3, 0, 0, 4, 20, 65},
            {8, 5, 22, 1, 8, 1, 7, 0, 6, 0, 0, 4, 20, 82}
    };

    // 定义仓库类型常量（可抽离到常量类）
    private static final String USABLE = "可用仓";
    private static final String DAMAGED = "破损仓";
    private static final String RMA = "RMA";

    private StatisticRepository statisticRepository;

    public MonthlyStatisticSheet(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }


    @Override
    public void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate) throws ExcelGenerateException {
        WriteSheet sheet = FastExcel.writerSheet("每月统计").head(buildComplexHeader()).build();
        excelWriter.write(calculateMonthlyData(startDate, endDate), sheet);
    }

    private Collection<?> calculateMonthlyData(LocalDate startDate, LocalDate endDate) {
        Integer month = startDate.getMonthValue();

        // 1.1 批量预加载维修记录数据（避免循环内多次查询）返仓记录相关
        Map<String, List<MaintainingRecordEntity>> reasonRecordsMap1 = statisticRepository.batchQueryMachineData(startDate, endDate, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS, RETMAINTAINENCEREASONTYPES).stream().collect(Collectors.groupingBy(MaintainingRecordEntity::getRetMaintainenceReason));

        // 1.2 批量预加载维修记录数据（避免循环内多次查询）维修时间记录相关
        Map<String, List<MaintainingRecordEntity>> reasonRecordsMap2 = statisticRepository.batchQueryMachineData2(startDate, endDate, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS, RETMAINTAINENCEREASONTYPES).stream().collect(Collectors.groupingBy(MaintainingRecordEntity::getRetMaintainenceReason));

        // 1.3 批量预加载维修仓数据（避免循环内多次查询）
        Map<String, List<MaintainingSprinklerEntity>> sprinklerMap = statisticRepository.batchQuerySprinklerData(startDate, endDate, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS, RETMAINTAINENCEREASONTYPES).stream().collect(Collectors.groupingBy(MaintainingSprinklerEntity::getRetMaintainenceReason));
        // 2. 使用并行流优化统计（利用多核CPU）
        Map<String, Long> warehouseCountMap = reasonRecordsMap1.values().stream().flatMap(List::stream).collect(Collectors.groupingByConcurrent(MaintainingRecordEntity::getRetWarehouseType, Collectors.counting()));
        Map<String, Map<String, Long>> retWarehouseTypeMap = new HashMap<>();
        for (String k : reasonRecordsMap1.keySet()) {
            retWarehouseTypeMap.put(k, reasonRecordsMap1.get(k).stream().collect(Collectors.groupingByConcurrent(MaintainingRecordEntity::getRetWarehouseType, Collectors.counting())));
        }
        List<MonthlyStatisticExcelVO> data = new ArrayList<>();
        for (int i = 0; i < RETMAINTAINENCEREASONTYPES.length - 1; i++) {
            String reason = RETMAINTAINENCEREASONTYPES[i];
            List<MaintainingRecordEntity> records = reasonRecordsMap1.getOrDefault(reason, Collections.emptyList());
            List<MaintainingRecordEntity> records2 = reasonRecordsMap2.getOrDefault(reason, Collections.emptyList());
            List<MaintainingSprinklerEntity> sprinklers = sprinklerMap.getOrDefault(reason, Collections.emptyList());

            MonthlyStatisticExcelVO excelVO = new MonthlyStatisticExcelVO();
            excelVO.setRetMaintainenceReason(reason);
            excelVO.setMonthBegin(MONTHBEGIN[month-1][i]);
            excelVO.setMonthIn(records2.size() + sprinklers.size());
            // 4. 通过预计算Map直接获取统计值（O(1)复杂度）
            // 改造调用方式
            excelVO.setRetUsable(getWarehouseCount(retWarehouseTypeMap, reason, USABLE));
            excelVO.setRetDamaged(getWarehouseCount(retWarehouseTypeMap, reason, DAMAGED));
            excelVO.setRetRma(getWarehouseCount(retWarehouseTypeMap, reason, RMA));
            excelVO.setMonthEnd(excelVO.getMonthBegin() + excelVO.getMonthIn() - excelVO.getRetUsable() - excelVO.getRetDamaged() - excelVO.getRetRma());
            data.add(excelVO);
        }

        String reason = RETMAINTAINENCEREASONTYPES[RETMAINTAINENCEREASONTYPES.length - 1];

        MonthlyStatisticExcelVO excelVO = new MonthlyStatisticExcelVO();
        excelVO.setRetMaintainenceReason(reason);
        excelVO.setMonthBegin(MONTHBEGIN[month-1][MONTHBEGIN.length - 1]);
        excelVO.setMonthIn(data.stream().mapToInt(v -> v.getMonthIn()).sum());
        // 4. 通过预计算Map直接获取统计值（O(1)复杂度）
        // 改造调用方式
        excelVO.setRetUsable(data.stream().mapToInt(v -> v.getRetUsable()).sum());
        excelVO.setRetDamaged(data.stream().mapToInt(v -> v.getRetDamaged()).sum());
        excelVO.setRetRma(data.stream().mapToInt(v -> v.getRetRma()).sum());
        excelVO.setMonthEnd(data.stream().mapToInt(v -> v.getMonthEnd()).sum());
        data.add(excelVO);
        return data;
    }

    // 优化取值逻辑
    private int getWarehouseCount(Map<String, Map<String, Long>> reasonMap, String reason, String warehouseType) {
        return reasonMap.getOrDefault(reason, Collections.emptyMap()).getOrDefault(warehouseType, 0L).intValue();
    }

    private ArrayList buildComplexHeader() {
        return new ArrayList<>() {{
            add(Arrays.asList("统计日期: 2025.3.31", "分类"));
            IntStream.range(0, 4).forEach(i -> add(Arrays.asList(new String[]{"月初", "当月收入", "当月多次收入", "当月破损仓取出清洗"}[i], new String[]{"月初", "当月收入", "当月多次收入", "当月破损仓取出清洗"}[i])));
            Arrays.asList("入可用仓", "入破损仓", "RMA", "注射测试液", "留在维修部").forEach(s -> add(Arrays.asList("当月维修部完成", s)));
            add(Arrays.asList("月末", ""));
        }};
    }
}

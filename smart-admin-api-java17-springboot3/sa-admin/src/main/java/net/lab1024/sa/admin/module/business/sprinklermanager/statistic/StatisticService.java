package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyStatisticExcelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 喷头管理-统计服务
 *
 * @Author 海印：芦苇
 */
@Service
@Slf4j
public class StatisticService {

    @Resource
    private MaintainingRecordRepository maintainingRecordRepository;
    @Resource
    private MaintainingSprinklerRepository mainingSprinklerRepository;

    // 暴力生成分类数据池
    private static final String[] RETMAINTAINENCEREASONTYPES = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损", "漏气", "内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final String[] RETMAINTAINENCEREASONTYPESVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final Integer[] MONTHBEGIN = {5, 3, 8, 7, 9, 0, 6, 0, 3, 0, 0, 4, 20, 65};

    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERSVO = {"年份月份","分类","大昌德1#","大昌德2#","宇华1#","宇华2#","宇华3#","华都1#","华都2#","鸿大大昌祥1#","鸿大大昌祥2#","鸿大大昌祥扫描机","鸿大北海1#","鸿大北海2#","鸿大北海3#","吉盛祥1#","吉盛祥2#","绍肖1#","绍肖2#","绍肖3#","绍肖4#","沙印1#","沙印2#","宏强1#","宏强2#","宏强3#","稽山1#","稽山2#","盛兴1#","盛兴2#","超超1#","超超2#","宜滨1#","宜滨2#","恒晨1#","恒晨3C1#","洁彩坊一号车间1#大机","洁彩坊二号车间1#大机","金楚1#大机","鸿大北海1#小机","鸿大北海2#小机","鸿大北海3#小机","鸿大北海5#小机","轮转机","其他","共计"};
    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS = {"大昌德1#","大昌德2#","宇华1#","宇华2#","宇华3#","华都1#","华都2#","大昌祥1#","大昌祥2#","大昌祥扫描机","鸿大北海1#大机","鸿大北海2#大机","鸿大北海3#大机","吉盛祥1#","吉盛祥2#","绍肖1#","绍肖2#","绍肖3#","绍肖4#","沙印1#","沙印2#","宏强1#","宏强2#","宏强3#","稽山1#","稽山2#","盛兴1#","盛兴2#","超超1#","超超2#","宜滨1#","宜滨2#","恒晨1#","恒晨3C1#","洁彩纺一号车间1#大机","洁彩纺二号车间1#大机","金楚1#大机","鸿大北海1#小机","鸿大北海2#小机","鸿大北海3#小机","鸿大北海5#小机","轮转机","其他","共计"};

    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机喷头", "其他原因说明", "小计"};

    // 定义仓库类型常量（可抽离到常量类）
    private static final String USABLE = "可用仓";
    private static final String DAMAGED = "破损仓";
    private static final String RMA = "RMA";
    @Autowired
    private MaintainingSprinklerRepository maintainingSprinklerRepository;


    public List<?> getMonthlySheetStatisticExcelExportData() {
        // 1. 暴力表头生成
        List<List<String>> header = new ArrayList<>() {{
            add(Arrays.asList("统计日期: 2025.3.31", "分类"));
            IntStream.range(0, 4).forEach(i -> add(Arrays.asList(
                    new String[]{"月初", "当月收入", "当月多次收入", "当月破损仓取出清洗"}[i],
                    new String[]{"月初", "当月收入", "当月多次收入", "当月破损仓取出清洗"}[i]
            )));
            Arrays.asList("入可用仓", "入破损仓", "RMA", "注射测试液", "留在维修部")
                    .forEach(s -> add(Arrays.asList("当月维修部完成", s)));
            add(Arrays.asList("月末", ""));
        }};


        // 1.1 批量预加载维修记录数据（避免循环内多次查询）返仓记录相关
        Map<String, List<MaintainingRecordEntity>> reasonRecordsMap1 = maintainingRecordRepository.list(
                new LambdaQueryWrapper<MaintainingRecordEntity>()
                        .lt(MaintainingRecordEntity::getRetWarehouseDate, "2025-03-01")
                        .ge(MaintainingRecordEntity::getRetWarehouseDate, "2025-02-01")
                        .in(MaintainingRecordEntity::getRetMaintainenceReason, RETMAINTAINENCEREASONTYPES)
        ).stream().collect(Collectors.groupingBy(MaintainingRecordEntity::getRetMaintainenceReason));
        // 1.2 批量预加载维修记录数据（避免循环内多次查询）维修时间记录相关
        Map<String, List<MaintainingRecordEntity>> reasonRecordsMap2 = maintainingRecordRepository.list(
                new LambdaQueryWrapper<MaintainingRecordEntity>()
                        .lt(MaintainingRecordEntity::getRetMaintainenceDate, "2025-03-01")
                        .ge(MaintainingRecordEntity::getRetMaintainenceDate, "2025-02-01")
                        .in(MaintainingRecordEntity::getRetMaintainenceReason, RETMAINTAINENCEREASONTYPES)
        ).stream().collect(Collectors.groupingBy(MaintainingRecordEntity::getRetMaintainenceReason));
        // 1.3 批量预加载维修仓数据（避免循环内多次查询）
        Map<String, List<MaintainingSprinklerEntity>> sprinklerMap = mainingSprinklerRepository.list(
                new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                        .lt(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-03-01")
                        .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-02-01")
                        .in(MaintainingSprinklerEntity::getRetMaintainenceReason, RETMAINTAINENCEREASONTYPES)
        ).stream().collect(Collectors.groupingBy(MaintainingSprinklerEntity::getRetMaintainenceReason));
        // 2. 使用并行流优化统计（利用多核CPU）
        Map<String, Long> warehouseCountMap = reasonRecordsMap1.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingByConcurrent(
                        MaintainingRecordEntity::getRetWarehouseType,
                        Collectors.counting()
                ));
        Map<String, Map<String, Long>> aaaMap = new HashMap<>();
        for(String k : reasonRecordsMap1.keySet()) {
            aaaMap.put(k, reasonRecordsMap1.get(k).stream()
                    .collect(Collectors.groupingByConcurrent(
                            MaintainingRecordEntity::getRetWarehouseType,
                            Collectors.counting()
                    )));
        }
        List<MonthlyStatisticExcelVO> data = new ArrayList<>();
        for (int i = 0; i < RETMAINTAINENCEREASONTYPES.length-1; i++) {
            String reason = RETMAINTAINENCEREASONTYPES[i];
            List<MaintainingRecordEntity> records = reasonRecordsMap1.getOrDefault(reason, Collections.emptyList());
            List<MaintainingRecordEntity> records2 = reasonRecordsMap2.getOrDefault(reason, Collections.emptyList());
            List<MaintainingSprinklerEntity> sprinklers = sprinklerMap.getOrDefault(reason, Collections.emptyList());

            MonthlyStatisticExcelVO excelVO = new MonthlyStatisticExcelVO();
            excelVO.setRetMaintainenceReason(reason);
            excelVO.setMonthBegin(MONTHBEGIN[i]);
            excelVO.setMonthIn(records2.size()+sprinklers.size());
            // 4. 通过预计算Map直接获取统计值（O(1)复杂度）
            // 改造调用方式
            excelVO.setRetUsable(getWarehouseCount(aaaMap, reason, USABLE));
            excelVO.setRetDamaged(getWarehouseCount(aaaMap, reason, DAMAGED));
            excelVO.setRetRma(getWarehouseCount(aaaMap, reason, RMA));
            excelVO.setMonthEnd(excelVO.getMonthBegin() + excelVO.getMonthIn()-excelVO.getRetUsable()-excelVO.getRetDamaged()-excelVO.getRetRma());
            data.add(excelVO);
        }

        String reason = RETMAINTAINENCEREASONTYPES[RETMAINTAINENCEREASONTYPES.length-1];

        MonthlyStatisticExcelVO excelVO = new MonthlyStatisticExcelVO();
        excelVO.setRetMaintainenceReason(reason);
        excelVO.setMonthBegin(MONTHBEGIN[MONTHBEGIN.length-1]);
        excelVO.setMonthIn(data.stream().mapToInt(v->v.getMonthIn()).sum());
        // 4. 通过预计算Map直接获取统计值（O(1)复杂度）
        // 改造调用方式
        excelVO.setRetUsable(data.stream().mapToInt(v->v.getRetUsable()).sum());
        excelVO.setRetDamaged(data.stream().mapToInt(v->v.getRetDamaged()).sum());
        excelVO.setRetRma(data.stream().mapToInt(v->v.getRetRma()).sum());
        excelVO.setMonthEnd(data.stream().mapToInt(v->v.getMonthEnd()).sum());
        data.add(excelVO);

        ExcelWriter excelWriter = FastExcel.write("月度报表.xlsx").build();

        WriteSheet writeSheet1 = FastExcel.writerSheet("每月统计").head(header).build();
        excelWriter.write(data, writeSheet1);

        // 1. 暴力表头生成
        List<List<String>> header2 = Arrays.stream(MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERSVO)
                .map(Arrays::asList)
                .collect(Collectors.toList());


        List<List<Object>> data1 = new ArrayList<>();

        List<Object> rowSummary = new ArrayList<>();
        rowSummary.add(String.format("2025-%02d", MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO.length-1));
        rowSummary.add(MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO[MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO.length-1]);
        Long[] summaryTotal = new Long[MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS.length];
        Arrays.fill(summaryTotal, 0L);

        // 生成12个月数据（可调整range参数修改数据量）
        IntStream.range(0, MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO.length-1).forEach(i -> {

            String reason1 = RETMAINTAINENCEREASONTYPES[i];
            List<Object> row = new ArrayList<>();

            // 年份月份列："2023-01"格式
            row.add(String.format("2025-%02d", i));

            // 分类列：交替显示两种分类
            row.add(MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO[i]);

            Long total = 0L;
            for(int j = 0; j < MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS.length - 1; j++) {
                Long count1 = maintainingRecordRepository.list(
                        new LambdaQueryWrapper<MaintainingRecordEntity>()
                                .ge(MaintainingRecordEntity::getRetWarehouseDate, "2025-02-01")
                                .ge(MaintainingRecordEntity::getRetMaintainenceDate, "2025-02-01")
                                .lt(MaintainingRecordEntity::getRetMaintainenceDate, "2025-03-01")
                                .like(MaintainingRecordEntity::getCustomer, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS[j])
                                .eq(MaintainingRecordEntity::getRetMaintainenceReason, reason1)
                ).stream().count();
                Long count2 = maintainingSprinklerRepository.list(
                        new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                                .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-02-01")
                                .lt(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-03-01")
                                .like(MaintainingSprinklerEntity::getCustomer, MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERS[j])
                                .eq(MaintainingSprinklerEntity::getRetMaintainenceReason, reason1)
                ).stream().count();
                Long value = count1 + count2;

                row.add(value);
                summaryTotal[j]+=value;
                total += value;
            }

            // 共计列自动计算总和
            row.add(total);
            summaryTotal[summaryTotal.length-1]+=total;
            data1.add(row);
        });
        for(int i=0;i<summaryTotal.length;i++) {
            rowSummary.add(summaryTotal[i]);
        }
        data1.add(rowSummary);



        WriteSheet writeSheet2 = FastExcel.writerSheet("每月统计2").head(header2).build();
        excelWriter.write(data1, writeSheet2);

//        // 3. 暴力合并策略（合并前两列）
//        FastExcel.write("月度报表.xlsx")
//                .head(header)
////                .registerWriteHandler(new LoopMergeStrategy(2, 0))  // 合并分类列
////                .registerWriteHandler(new LoopMergeStrategy(2, 1))  // 合并统计日期列
//                .sheet("每月统计")
//                .doWrite(data)
//        ;

//        return resultList;
        excelWriter.close();
        return List.of();
    }

    // 优化取值逻辑
    private int getWarehouseCount(Map<String, Map<String, Long>> reasonMap,
                                  String reason, String warehouseType) {
        return reasonMap.getOrDefault(reason, Collections.emptyMap())
                .getOrDefault(warehouseType, 0L)
                .intValue();
    }
}

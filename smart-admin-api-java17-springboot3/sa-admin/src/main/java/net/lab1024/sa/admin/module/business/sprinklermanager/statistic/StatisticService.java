package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import cn.idev.excel.FastExcel;
import cn.idev.excel.write.merge.LoopMergeStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.MaintainingRecordService;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.CombinedQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyStatisticExcelVO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
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
    private static final String[] RETMAINTAINENCEREASON = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损", "漏气", "内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final String[] RETMAINTAINENCEREASONVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final Integer[] MONTHBEGIN = {5, 3, 8, 7, 9, 0, 6, 0, 3, 0, 0, 4, 20, 65};

    // 定义仓库类型常量（可抽离到常量类）
    private static final String USABLE = "可用仓";
    private static final String DAMAGED = "破损仓";
    private static final String RMA = "RMA";


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
                        .in(MaintainingRecordEntity::getRetMaintainenceReason, RETMAINTAINENCEREASON)
        ).stream().collect(Collectors.groupingBy(MaintainingRecordEntity::getRetMaintainenceReason));
        // 1.2 批量预加载维修记录数据（避免循环内多次查询）维修时间记录相关
        Map<String, List<MaintainingRecordEntity>> reasonRecordsMap2 = maintainingRecordRepository.list(
                new LambdaQueryWrapper<MaintainingRecordEntity>()
                        .lt(MaintainingRecordEntity::getRetMaintainenceDate, "2025-03-01")
                        .ge(MaintainingRecordEntity::getRetMaintainenceDate, "2025-02-01")
                        .in(MaintainingRecordEntity::getRetMaintainenceReason, RETMAINTAINENCEREASON)
        ).stream().collect(Collectors.groupingBy(MaintainingRecordEntity::getRetMaintainenceReason));
        // 1.3 批量预加载维修仓数据（避免循环内多次查询）
        Map<String, List<MaintainingSprinklerEntity>> sprinklerMap = mainingSprinklerRepository.list(
                new LambdaQueryWrapper<MaintainingSprinklerEntity>()
                        .lt(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-03-01")
                        .ge(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-02-01")
                        .in(MaintainingSprinklerEntity::getRetMaintainenceReason, RETMAINTAINENCEREASON)
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
        for (int i = 0; i < RETMAINTAINENCEREASON.length-1; i++) {
            String reason = RETMAINTAINENCEREASON[i];
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

        String reason = RETMAINTAINENCEREASON[RETMAINTAINENCEREASON.length-1];

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




        // 3. 暴力合并策略（合并前两列）
        FastExcel.write("暴力报表.xlsx")
                .head(header)
//                .registerWriteHandler(new LoopMergeStrategy(2, 0))  // 合并分类列
//                .registerWriteHandler(new LoopMergeStrategy(2, 1))  // 合并统计日期列
                .sheet("暴力测试数据")
                .doWrite(data);

//        return resultList;
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

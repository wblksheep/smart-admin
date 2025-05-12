package net.lab1024.sa.admin.module.business.multiheadersheet;

import cn.idev.excel.FastExcel;
import cn.idev.excel.write.merge.LoopMergeStrategy;
import net.lab1024.sa.admin.module.business.multiheadersheet.domain.vo.CrossHeaderData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ExcelExport {
    // 暴力生成分类数据池
    private static final String[] CATEGORIES = {"电子产品", "医疗器械", "工业零件", "实验耗材"};

    public static void main(String[] args) {
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

        // 2. 暴力数据生成（生成50条测试数据）
        List<List<Object>> data = new ArrayList<>();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        IntStream.range(0, 50).forEach(i -> data.add(Arrays.asList(
                "2025-03-31", // 固定日期
                CATEGORIES[rand.nextInt(CATEGORIES.length)], // 随机分类
                rand.nextInt(1000, 5000),    // 月初金额
                rand.nextInt(50000, 200000), // 当月收入
                rand.nextInt(0, 15),         // 当月多次收入次数
                rand.nextInt(0, 30),         // 破损仓清洗次数
                rand.nextBoolean() ? "是" : "否",     // 入可用仓
                rand.nextBoolean() ? "需维修" : "正常", // 入破损仓状态
                "RMA-" + (1000 + i),         // RMA编号
                rand.nextInt(10) + "批次",    // 注射测试液批次
                rand.nextInt(1, 30) + "天",   // 维修部停留时长
                "/"                          // 月末占位
        )));

        // 3. 暴力合并策略（合并前两列）
        FastExcel.write("暴力报表.xlsx")
                .head(header)
                .registerWriteHandler(new LoopMergeStrategy(2, 0))  // 合并分类列
                .registerWriteHandler(new LoopMergeStrategy(2, 1))  // 合并统计日期列
                .sheet("暴力测试数据")
                .doWrite(data);
    }
}

package net.lab1024.sa.admin.module.business.multiheadersheet;

import cn.idev.excel.FastExcel;
import cn.idev.excel.write.merge.LoopMergeStrategy;
import net.lab1024.sa.admin.module.business.multiheadersheet.domain.vo.CrossHeaderData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelExport {
    public static void main(String[] args) {
        // 创建表头数据（两行）
        List<List<String>> header = Arrays.asList(
                Arrays.asList("统计日期: 2025.3.31", "分类"),
                Arrays.asList("月初", "月初"),
                Arrays.asList("当月收入", "当月收入"),
                Arrays.asList("当月多次收入", "当月多次收入"),
                Arrays.asList("当月破损仓取出清洗", "当月破损仓取出清洗"),
                Arrays.asList("当月维修部完成", "入可用仓"),
                Arrays.asList("当月维修部完成", "入破损仓"),
                Arrays.asList("当月维修部完成", "RMA"),
                Arrays.asList("当月维修部完成", "注射测试液"),
                Arrays.asList("当月维修部完成", "留在维修部"),
                Arrays.asList("月末", "")
        );

        // 配置合并策略（纵向合并两行）
        LoopMergeStrategy mergeStrategy = new LoopMergeStrategy(2, 0);

        // 生成Excel
        FastExcel.write("报表.xlsx")
                .head(header)
                .registerWriteHandler(mergeStrategy) // 注册合并策略
                .sheet("数据表")
                .doWrite(new ArrayList<>()); // 写入空数据
    }
}

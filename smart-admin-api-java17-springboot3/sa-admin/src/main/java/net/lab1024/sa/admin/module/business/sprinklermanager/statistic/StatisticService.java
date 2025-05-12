package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import cn.idev.excel.FastExcel;
import cn.idev.excel.write.merge.LoopMergeStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.MaintainingRecordService;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.CombinedQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyStatisticExcelVO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    // 暴力生成分类数据池
    private static final String[] RETMAINTAINENCEREASON = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损", "漏气", "内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final String[] RETMAINTAINENCEREASONVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机", "共计"};
    private static final Integer[] MONTHBEGIN = {8, 5, 22, 1, 8, 1, 7, 0, 6, 0, 0, 4, 20, 82};


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

        List<MonthlyStatisticExcelVO> data = new ArrayList<>();

        MaintainingRecordQueryForm queryForm = new MaintainingRecordQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MaintainingRecordVO> maintainingRecordList = maintainingRecordRepository.getListByQueryPage(page, queryForm);


        for (int i = 0; i < RETMAINTAINENCEREASON.length; i++) {
            MonthlyStatisticExcelVO excelVO = new MonthlyStatisticExcelVO();
            excelVO.setRetMaintainenceReason(RETMAINTAINENCEREASONVO[i]);
            excelVO.setMonthBegin(MONTHBEGIN[i]);
//            excelVO.setMonthIn();
        }


        // 3. 暴力合并策略（合并前两列）
        FastExcel.write("暴力报表.xlsx")
                .head(header)
                .registerWriteHandler(new LoopMergeStrategy(2, 0))  // 合并分类列
                .registerWriteHandler(new LoopMergeStrategy(2, 1))  // 合并统计日期列
                .sheet("暴力测试数据")
                .doWrite(data);

//        return resultList;
        return List.of();
    }
}

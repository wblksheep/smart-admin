package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.factory.ExcelGeneratorFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator.SheetGenerator;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartExcelUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

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



    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERHEADERSVO = {"年份月份","分类","大昌德1#","大昌德2#","宇华1#","宇华2#","宇华3#","华都1#","华都2#","鸿大大昌祥1#","鸿大大昌祥2#","鸿大大昌祥扫描机","鸿大北海1#","鸿大北海2#","鸿大北海3#","吉盛祥1#","吉盛祥2#","绍肖1#","绍肖2#","绍肖3#","绍肖4#","沙印1#","沙印2#","宏强1#","宏强2#","宏强3#","稽山1#","稽山2#","盛兴1#","盛兴2#","超超1#","超超2#","宜滨1#","宜滨2#","恒晨1#","恒晨3C1#","洁彩坊一号车间1#大机","洁彩坊二号车间1#大机","金楚1#大机","鸿大北海1#小机","鸿大北海2#小机","鸿大北海3#小机","鸿大北海5#小机","轮转机","其他","共计"};


    private static final String[] MONTHLYMACHINERETWAREHOUSESPRINKLERTYPESVO = {"活性堵嘴歪针", "分散堵嘴歪针", "活性湿浆堵嘴歪针", "分散湿浆堵嘴歪针", "物理破损", "报错驱动过流", "电路受损：白条、常喷、断喷、接触不良、不喷、不打印", "漏气", "喷头内色差或持续性差", "金手指损坏", "测试", "其他", "轮转机喷头", "其他原因说明", "小计"};


    @Autowired
    private MaintainingSprinklerRepository maintainingSprinklerRepository;

    @Autowired
    private ExcelGeneratorFactory factory;

    public ResponseDTO<String> getMonthlySheetStatisticExcelExportData(LocalDate startDate, LocalDate endDate, HttpServletResponse response, String watermarkString) throws IOException {
        String fileName = "月度报表.xlsx";
        // 设置下载消息头
        SmartResponseUtil.setDownloadFileHeader(response, fileName, null);
        try(ExcelWriter excelWriter = FastExcel.write(response.getOutputStream()).inMemory(true).build()){
            // 使用责任链模式添加Sheet生成器
            List<SheetGenerator> generators = Arrays.asList(
                    factory.createMonthlyStatisticSheet(),
                    factory.createMonthlyMachineRetWarehouseSprinklerStatisticSheet(),
                    factory.createMonthlyDamagedSprinklerSheet(),
                    factory.createMonthlyMachineSprinklerMaintainingDetailSheet(),
                    factory.createMonthlyRetUsableSprinklerSheet(),
                    factory.createMonthlyDamagedSprinklerUsingDaysSheet()
            );
            generators.forEach(generator -> {
                try {
                    generator.generateSheet(excelWriter, startDate, endDate);
                }catch (ExcelGenerateException e){
                    log.error("Sheet生成失败", e);
                }
            });
        }
        return ResponseDTO.ok();
    }


}

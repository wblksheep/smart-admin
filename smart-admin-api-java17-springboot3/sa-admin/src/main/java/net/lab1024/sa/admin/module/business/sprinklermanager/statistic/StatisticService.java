package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.exception.ExcelGenerateException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyStatisticExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyStatisticSheetVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.factory.ExcelGeneratorFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator.MonthlyStatisticSheet;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator.SheetGenerator;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartExcelUtil;
import net.lab1024.sa.base.common.util.SmartResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static net.lab1024.sa.admin.module.business.sprinklermanager.statistic.MachineConfigUpdater.updateMachineType;

/**
 * 喷头管理-统计服务
 *
 * @Author 海印：芦苇
 */
@Service
@Slf4j
public class StatisticService {

    @Resource
    private ExcelGeneratorFactory factory;

    public ResponseDTO<String> getMonthlySheetStatisticExcelExportData(LocalDate startDate, LocalDate endDate, String machineType, HttpServletResponse response, String watermarkString) throws IOException {
        updateMachineType(machineType);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月");
        String formattedDate = startDate.format(formatter); // 输出格式如"2023-10"
        String fileName = formattedDate + machineType + "喷头月度报表.xlsx";
        // 设置下载消息头
        SmartResponseUtil.setDownloadFileHeader(response, fileName, null);
        try (ExcelWriter excelWriter = FastExcel.write(response.getOutputStream()).inMemory(true).build()) {
            // 使用责任链模式添加Sheet生成器
            List<SheetGenerator> generators = Arrays.asList(factory.createMonthlyStatisticSheet(), factory.createMonthlyMachineRetWarehouseSprinklerStatisticSheet(), factory.createMonthlyDamagedSprinklerSheet(), factory.createMonthlyMachineSprinklerMaintainingDetailSheet(), factory.createMonthlyRetUsableSprinklerSheet(), factory.createMonthlyDamagedSprinklerUsingDaysSheet());
            generators.forEach(generator -> {
                try {
                    generator.generateSheet(excelWriter, startDate, endDate, machineType);
                } catch (ExcelGenerateException e) {
                    log.error("Sheet生成失败", e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return ResponseDTO.ok();
    }


    /**
     * 查询每月统计模块
     */
    public ResponseDTO<List<MonthlyStatisticSheetVO>> getMonthlyStatisticSheet(LocalDate startDate, LocalDate endDate, String machineType) throws IOException {
        updateMachineType(machineType);
        MonthlyStatisticSheet statisticSheet = factory.createMonthlyStatisticSheet();
        try {
            List<MonthlyStatisticExcelVO> datas = statisticSheet.calculateMonthlyData(startDate, endDate, machineType, Boolean.FALSE);
            return ResponseDTO.ok(datas.stream().map(data -> {
                MonthlyStatisticSheetVO vo = new MonthlyStatisticSheetVO();
                SmartBeanUtil.copyProperties(data, vo);
                return vo;
            }).collect(Collectors.toList()));
        } catch (ArrayIndexOutOfBoundsException e) {
            return ResponseDTO.userErrorParam("没有提供月初统计数据");
        }
    }


}

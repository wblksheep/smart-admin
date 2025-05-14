package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;


import cn.idev.excel.ExcelWriter;
import cn.idev.excel.exception.ExcelGenerateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Sheet生成器抽象类
public abstract class SheetGenerator {
    protected final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");


    public abstract void generateSheet(ExcelWriter excelWriter, LocalDate startDate, LocalDate endDate) throws ExcelGenerateException;
}

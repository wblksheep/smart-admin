package net.lab1024.sa.admin.module.business.multiheadersheet.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CrossHeaderData {
    @ExcelProperty({"竖向表头", "序号"}) // 竖向表头固定在第一列
    private Integer rowNumber;

    @ExcelProperty({"横向表头", "基本信息", "姓名"})
    private String name;

    @ExcelProperty({"横向表头", "基本信息", "年龄"})
    private Integer age;
}

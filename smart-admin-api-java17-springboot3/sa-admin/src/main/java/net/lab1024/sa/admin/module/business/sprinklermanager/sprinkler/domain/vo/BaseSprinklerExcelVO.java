package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class BaseSprinklerExcelVO {

    @ExcelProperty(index = 0, value = "喷头ID")
    private Long sprinklerId;

    @ExcelProperty(index = 1, value = "喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("所在仓status")
    private Byte status;
}

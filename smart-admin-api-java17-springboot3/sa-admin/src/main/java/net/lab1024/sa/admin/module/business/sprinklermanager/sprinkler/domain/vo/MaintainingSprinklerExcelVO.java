package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;

/**
 * 维修仓喷头信息Excel
 * @Author 海印: 芦苇
 */
@Data
public class MaintainingSprinklerExcelVO extends BaseSprinklerExcelVO{
     @ExcelProperty("喷头序列号")
    private String sprinklerSerial;

     @ExcelProperty("返修日期")
    private LocalDate retMaintainenceDate;

     @ExcelProperty("返修原因")
    private String retMaintainenceReason;

     @ExcelProperty("具体原因")
    private String realReason;

     @ExcelProperty("返修客户")
    private String customer;

     @ExcelProperty("历史")
    private String history;


}

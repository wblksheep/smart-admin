package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;

/**
 * 破损仓喷头信息Excel
 *
 * @Author 海印: 芦苇
 */
@Data
public class DamagedSprinklerExcelVO {


    @ExcelProperty("喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty("历史")
    private String history;

    @ExcelProperty("备注1")
    private String note1;

    @ExcelProperty("破损原因分类")
    private String damagedReasonType;

    @ExcelProperty("具体破损原因")
    private String realDamagedReason;

}

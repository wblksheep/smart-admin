package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;

/**
 * 机台喷头信息Excel
 *
 * @Author 海印: 芦苇
 */
@Data
public class MachineSprinklerExcelVO extends BaseSprinklerExcelVO{

    @ExcelProperty("喷头型号")
    private String sprinklerModel;

    @ExcelProperty("入仓日期")
    private LocalDate warehouseDate;

    @ExcelProperty("领用日期")
    private LocalDate allocateDate;

    @ExcelProperty("领用用途")
    private String allocatePurpose;

    @ExcelProperty("位置")
    private String allocatePosition;

}

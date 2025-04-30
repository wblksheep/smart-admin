package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 可用仓喷头信息Excel
 *
 * @Author 海印: 芦苇
 */
@Data
public class UsableSprinklerExcelVO extends BaseSprinklerExcelVO{

    @ExcelProperty("喷头型号")
    private String sprinklerModel;

    @ExcelProperty("喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("发货日期")
    private LocalDate shippingDate;

    @ExcelProperty("入仓日期")
    private LocalDate warehouseDate;

    @ExcelProperty("电压")
    private Float voltage;

    @ExcelProperty("jetsout")
    private Byte jetsout;

    @ExcelProperty("历史")
    private String history;

    @ExcelProperty("新旧喷头")
    private Boolean isNew;

    @ExcelProperty("返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty("领用是否有限制")
    private String allocateLimitation;

    @ExcelProperty("领用时备注1")
    private String allocateNote1;

    @ExcelProperty("喷头详情")
    private String sprinklerDetail;

}

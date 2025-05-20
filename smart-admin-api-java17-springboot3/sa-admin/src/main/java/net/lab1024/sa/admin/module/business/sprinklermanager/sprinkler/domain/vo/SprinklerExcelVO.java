package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 喷头信息
 *
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerExcelVO {
    @ExcelProperty("喷头ID")
    private Long sprinklerId;


    @ExcelProperty("购入日期（合同编号）")
    private String purchaseDateContractNumber;

    @ExcelProperty("喷头型号")
    private String sprinklerModel;

    @ExcelProperty("喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("发货日期")
    private LocalDate shippingDate;

    @ExcelProperty("入仓日期")
    private LocalDate warehouseDate;

    @ExcelProperty("领用日期")
    private LocalDate allocateDate;

    @ExcelProperty("领用人")
    private String allocateUser;

    @ExcelProperty("领用用途")
    private String allocatePurpose;

    @ExcelProperty("位置")
    private String allocatePosition;

    @ExcelProperty("电压")
    private Float voltage;

    @ExcelProperty("jetsout")
    private Byte jetsout;

    @ExcelProperty("历史")
    private String history;

    @ExcelProperty("所在仓status")
    private Byte status;

    @ExcelProperty("新旧喷头")
    private Boolean isNew;

    @ExcelProperty("喷头详情")
    private String sprinklerDetail;
}

package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 维修记录
 * @Author 海印: 芦苇
 */
@Data
public class MaintainingRecordExcelVO {

    @ExcelProperty( "返修日期")
    private LocalDate retMaintainenceDate;

    @ExcelProperty( "喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty( "返修原因")
    private String retMaintainenceReason;

    @ExcelProperty( "具体原因")
    private String realReason;

    @ExcelProperty( "返修客户")
    private String customer;

    @ExcelProperty( "返仓日期")
    private LocalDate retWarehouseDate;

    @ExcelProperty( "返仓类型")
    private String retWarehouseType;

    @ExcelProperty( "领用是否有限制")
    private String allocateLimitation;

}

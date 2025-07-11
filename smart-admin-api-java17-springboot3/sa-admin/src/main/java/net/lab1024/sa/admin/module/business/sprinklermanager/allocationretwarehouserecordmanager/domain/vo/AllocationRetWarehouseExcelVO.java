package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领用与返仓记录
 *
 * @Author 海印: 芦苇
 */
@Data
public class AllocationRetWarehouseExcelVO {

    @ExcelProperty(value = "领用人", index = 0)
    private String createUserName;

    @ExcelProperty("领用喷头序列号")
    private String allocateSprinklerSerial;

    @ExcelProperty("领用喷头型号")
    private String allocateSprinklerModel;

    @ExcelProperty("领用用途")
    private String allocatePurpose;

    @ExcelProperty("领用颜色")
    private String allocateColor;

    @ExcelProperty("领用位置")
    private Byte allocatePosition;

    @ExcelProperty("领用喷头类型")
    private String allocateSprinklerIsNew;

    @ExcelProperty("返仓喷头序列号")
    private String retWarehouseSprinklerSerial;

    @ExcelProperty("返仓喷头型号")
    private String retWarehouseSprinklerModel;

    @ExcelProperty("返仓机台")
    private String retWarehouseMachine;

    @ExcelProperty("返仓颜色")
    private String retWarehouseColor;

    @ExcelProperty("返仓位置")
    private Byte retWarehousePosition;

    @ExcelProperty("返仓喷头类型")
    private String retWarehouseSprinklerIsNew;

    @ExcelProperty("返仓原因")
    private String retWarehouseReason;

    @ExcelProperty("具体原因")
    private String retWarehouseRealReason;

    @ExcelProperty("备注")
    private String note;


    public String getRetWarehouseSprinklerIsNew() {
        return this.retWarehouseSprinklerIsNew;
    }

    public void setRetWarehouseSprinklerIsNew(Boolean isNew) {
        this.retWarehouseSprinklerIsNew = isNew ? "新喷头" : "旧喷头";
    }

    public String getAllocateSprinklerIsNew() {
        return this.allocateSprinklerIsNew;
    }

    public void setAllocateSprinklerIsNew(Boolean isNew) {
        this.allocateSprinklerIsNew = isNew ? "新喷头" : "旧喷头";
    }
}

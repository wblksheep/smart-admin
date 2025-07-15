package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class BaseSprinklerExcelVO {

    @ExcelProperty(index = 0, value = "喷头序列号")
    private String sprinklerSerial;

    @ExcelProperty("所在仓")
    private String status;

    @ExcelProperty("历史")
    private String history;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        switch (status) {
            case (byte) 0:
                this.status = "可用仓";
                break;
            case (byte) 1:
                this.status = "机台";
                break;
            case (byte) 2:
                this.status = "维修仓";
                break;
            case (byte) 3:
                this.status = "破损仓";
                break;
            case (byte) 4:
                this.status = "rma";
                break;
            case (byte) 5:
                this.status = "领用中";
                break;
            default:
                throw new RuntimeException("非法的仓库状态");
        }
    }

}

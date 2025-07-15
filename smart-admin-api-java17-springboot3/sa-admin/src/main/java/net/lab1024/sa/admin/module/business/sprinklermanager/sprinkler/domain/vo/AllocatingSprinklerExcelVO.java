package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 机台喷头信息Excel
 *
 * @Author 海印: 芦苇
 */
@Data
public class AllocatingSprinklerExcelVO extends BaseSprinklerExcelVO{

    @ExcelProperty("喷头型号")
    private String sprinklerModel;

    @ExcelProperty("领用日期")
    private LocalDate allocateDate;

    @ExcelProperty("领用人")
    private String allocateUser;

    @ExcelProperty("新旧喷头")
    private String isNew;

    public String getIsNew() {
        return this.isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew ? "新喷头" : "旧喷头";
    }

}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 全部喷头信息编辑
 *
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerUpdateForm extends BaseCreateForm {
    @ExcelProperty("喷头ID")
    @Schema(description = "喷头ID")
    @NotNull(message = "喷头ID不能为空")
    private Long sprinklerId;


    @ExcelProperty("购入日期（合同编号）")
    @Schema(description = "购入日期（合同编号）")
    @Length(max = 50, message = "购入日期（合同编号）最多50字符")
    private String purchaseDateContractNumber;

    @ExcelProperty("喷头型号")
    @Schema(description = "喷头型号")
    @Length(max = 50, message = "喷头型号最多50字符")
    private String sprinklerModel;

    @ExcelProperty("发货日期")
    @Schema(description = "发货日期")
    private LocalDate shippingDate;

    @ExcelProperty("入仓日期")
    @Schema(description = "入仓日期")
    private LocalDate warehouseDate;

    @ExcelProperty("领用日期")
    @Schema(description = "领用日期")
    private LocalDate allocateDate;

    @ExcelProperty("领用人")
    @Schema(description = "领用人")
    private String allocateUser;

    @ExcelProperty("领用用途")
    @Schema(description = "领用用途")
    private String allocatePurpose;

    @ExcelProperty("位置")
    @Schema(description = "位置")
    private String allocatePosition;

    @ExcelProperty("电压")
    @Schema(description = "电压")
    private Float voltage;

    @ExcelProperty("jetsout")
    @Schema(description = "jetsout")
    private Byte jetsout;

    @ExcelProperty("jetsout")
    @Schema(description = "jetsout")
    private Float jetsoutNew;

    @ExcelProperty("历史")
    @Schema(description = "history")
    private String history;


    @ExcelProperty("所在仓")
    @Schema(description = "所在仓")
    private Byte status;

    @ExcelProperty("新旧喷头")
    @Schema(description = "新旧喷头")
    private Boolean isNew;

    @ExcelProperty("喷头详情")
    @Schema(description = "喷头详情")
    private String sprinklerDetail;
}

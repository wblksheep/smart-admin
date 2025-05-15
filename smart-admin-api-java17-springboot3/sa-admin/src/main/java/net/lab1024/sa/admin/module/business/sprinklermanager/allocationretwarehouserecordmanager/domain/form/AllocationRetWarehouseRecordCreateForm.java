package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class AllocationRetWarehouseRecordCreateForm extends AllocationRetWarehouseRecordBaseForm{

    @Schema(description = "领用与返仓表单")
    @Valid
    private List<AllocationRetWarehouseCreateForm> allocationRetWarehouseCreateForm;

}

package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AllocationRetWarehouseRecordCreateForm {

    @Schema(description = "领用与返仓表单")
    private List<AllocationRetWarehouseCreateForm> allocationRetWarehouseCreateForm;

}

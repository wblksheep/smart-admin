package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AllocationRetWarehouseRecordUpdateForm extends AllocationRetWarehouseRecordBaseForm{

    @Schema(description = "记录ID")
    @NotNull(message = "记录ID不能为空")
    private Long recordId;

    @Schema(description = "领用与返仓表单")
    private List<AllocationRetWarehouseUpdateForm> allocationRetWarehouseUpdateForm;

}

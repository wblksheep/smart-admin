package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class AllocationRetWarehouseRecordQueryForm {

    @Schema(description = "领用与返仓表单")
    private List<AllocationRetWarehouseCreateForm> allocationRetWarehouseCreateForm;

    @Schema(description = "标记状态")
    private Boolean markedFlag;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;
}

package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AllocationRetWarehouseRecordBaseForm {
    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID", hidden = true)
    private Long createUserId;

    @Schema(description = "创建人", hidden = true)
    private String createUserName;
}

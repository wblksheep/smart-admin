package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AllocationRetWarehouseRecordApproveForm {

    @Schema(description = "记录ID")
    @NotNull(message = "记录ID不能为空")
    private Long recordId;

    @Schema(description = "是否通过")
    @NotNull(message = "是否通过不能为空")
    private Boolean isApproved;

}

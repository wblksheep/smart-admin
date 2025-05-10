package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AllocationRetWarehouseUpdateForm extends AllocationRetWarehouseBaseForm{

    @Schema(description = "领用与返仓ID")
    @NotNull(message = "领用与返仓ID不能为空")
    private Long allocationRetWarehouseId;
}

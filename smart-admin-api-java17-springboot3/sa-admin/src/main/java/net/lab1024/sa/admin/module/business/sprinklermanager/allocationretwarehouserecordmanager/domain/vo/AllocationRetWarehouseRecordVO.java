package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordBaseForm;

import java.util.List;

@Data
public class AllocationRetWarehouseRecordVO extends AllocationRetWarehouseRecordBaseForm {
    @Schema(description = "领用与返仓记录Id")
    private Long recordId;

    @Schema(description = "领用与返仓表单")
    @Valid
    private List<AllocationRetWarehouseVO> allocationRetWarehouseVO;

    @Schema(description = "审核状态")
    private Byte status;

}

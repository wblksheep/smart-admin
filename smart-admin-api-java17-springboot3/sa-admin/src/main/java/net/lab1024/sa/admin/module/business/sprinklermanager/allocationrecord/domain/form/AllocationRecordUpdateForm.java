package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AllocationRecordUpdateForm extends AllocationRecordCreateForm{
    @Schema(description = "领用信息记录ID")
    private Long recordId;

    @Schema(description = "喷头ID")
    private Long sprinklerId;

}

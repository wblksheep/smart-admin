package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AllocatingSprinklerQueryForm extends BaseQueryForm{

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

}

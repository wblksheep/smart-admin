package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import java.util.HashMap;
import java.util.List;

@Data
public class SprinklerQueryFormList extends PageParam {

    @Schema(description = "查询条件列表")
    List<SprinklerQueryForm> queryFormList;

}



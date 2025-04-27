package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.builder.JoinConditionBuilder;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
public class CombinedQueryForm extends PageParam {
    private SprinklerQueryForm queryForm;
    private BaseQueryForm joinQueryForm;
    private String sceneType;
}

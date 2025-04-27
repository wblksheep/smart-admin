package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.builder.JoinConditionBuilder;

@Data
public class CombinedQueryForm {
    private SprinklerQueryForm queryForm;
    private BaseQueryForm joinQueryForm;
}

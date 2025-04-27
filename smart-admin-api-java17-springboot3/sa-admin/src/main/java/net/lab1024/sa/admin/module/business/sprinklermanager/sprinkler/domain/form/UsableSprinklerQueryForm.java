package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelProperty;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.annotation.ConditionField;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.annotation.JoinConditionConfig;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.ConditionType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.JoinConditionProcessor;

import java.time.LocalDate;

@Data
@JoinConditionConfig(tableClass = UsableSprinklerEntity.class)
public class UsableSprinklerQueryForm extends BaseQueryForm{

    @Schema(description = "返仓日期开始时间")
    private LocalDate retWarehouseDateStartTime;

    @Schema(description = "返仓日期结束时间")
    private LocalDate retWarehouseDateEndTime;

    @Schema(description = "领用是否有限制")
    @ConditionField(column = "allocate_limitation", type = ConditionType.EQUAL)
    private String allocateLimitation;

    @Schema(description = "领用时备注1")
    @ConditionField(column = "allocate_note1", type = ConditionType.EQUAL)
    private String allocateNote1;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @Override
    public void buildJoinConditions(MPJLambdaWrapper<?> wrapper) {
        // 通过注解处理器自动生成条件
        JoinConditionProcessor.process(this, wrapper);
    }
}

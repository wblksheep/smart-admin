package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.builder.JoinConditionBuilder;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "sceneType") // 根据sceneType字段识别子类
@JsonSubTypes({
        @JsonSubTypes.Type(value = UsableSprinklerQueryForm.class, name = "USABLE_REPOSITORY"),
        @JsonSubTypes.Type(value = MachineSprinklerQueryForm.class, name = "MACHINE_REPOSITORY"),
//        @JsonSubTypes.Type(value = MaintainingSprinklerQueryForm.class, name = "MAINTAINING_REPOSITORY"),
//        @JsonSubTypes.Type(value = DamagedSprinklerQueryForm.class, name = "DAMAGED_REPOSITORY"),
//        @JsonSubTypes.Type(value = RmaSprinklerQueryForm.class, name = "RMA_REPOSITORY")
})
public abstract class BaseQueryForm extends PageParam {
}

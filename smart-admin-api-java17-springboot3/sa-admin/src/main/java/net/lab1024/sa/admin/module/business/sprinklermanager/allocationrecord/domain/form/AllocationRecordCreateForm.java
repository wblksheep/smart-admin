package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form;

import cn.idev.excel.annotation.ExcelIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

@Data
public class AllocationRecordCreateForm {


    @Schema(description = "喷头序列号")
    @Length(max = 255, message = "sprinklerSerial最多255字符")
    private String sprinklerSerial;

    @Schema(description = "领用人")
    @Length(max = 255, message = "领用人最多255字符")
    private String allocateUser;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID", hidden = true)
    private Long createUserId;

    @Schema(description = "创建人", hidden = true)
    private String createUserName;

}

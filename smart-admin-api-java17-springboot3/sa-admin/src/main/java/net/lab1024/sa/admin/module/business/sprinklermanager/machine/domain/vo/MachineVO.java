package net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机台信息
 *
 * @Author 海印: 芦苇
 */
@Data
public class MachineVO {
    @Schema(description = "机台ID")
    private Long machineId;

    @Schema(description = "机台名称")
    private String machineName;

    @Schema(description = "机台类型")
    private String machineType;

    @Schema(description = "禁用状态")
    private Boolean disabledFlag;

    @Schema(description = "创建人ID")
    private Long createUserId;

    @Schema(description = "创建人名称")
    private String createUserName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}

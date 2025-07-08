package net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity;


import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * 机台
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_machine")
public class MachineEntity {
    /**
     * 机台ID
     */
    @TableId(type = IdType.AUTO)
    private Long machineId;

    /**
     * 机台名称
     */
    @DataTracerFieldLabel("机台名称")
    private String machineName;

    /**
     * 机台类型
     */
    @DataTracerFieldLabel("机台类型")
    private String machineType;

    /**
     * 禁用状态
     */
    @DataTracerFieldLabel("禁用状态")
    private Boolean disabledFlag;

    /**
     * 删除状态
     */
    @DataTracerFieldLabel("删除状态")
    private Boolean deletedFlag;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

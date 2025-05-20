package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 领用信息记录表
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_allocation_record")
public class AllocationRecordEntity {
    /**
     * 领用信息记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 喷头ID
     */
    private Long sprinklerId;

    /**
     * 喷头序列号
     */
    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    /**
     * 领用人
     */
    @DataTracerFieldLabel("领用人")
    private String allocateUser;

    /**
     * 领用日期
     */
    @DataTracerFieldLabel("领用日期")
    private LocalDate allocateDate;

    /**
     * 备注
     */
    @DataTracerFieldLabel("备注")
    private String note;

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

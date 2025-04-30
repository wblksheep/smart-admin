package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 维修仓喷头
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_maintaining_sprinkler")
public class MaintainingSprinklerEntity {
    /**
     * 喷头ID
     */
    @TableId
    private Long sprinklerId;

    /**
     * 喷头序列号
     */
    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    /**
     * 返修日期
     */
    @DataTracerFieldLabel("返修日期")
    private LocalDate retMaintainenceDate;

    /**
     * 返修原因
     */
    @DataTracerFieldLabel("返修原因")
    private String retMaintainenceReason;

    /**
     * 具体原因
     */
    @DataTracerFieldLabel("具体原因")
    private String realReason;

    /**
     * 返修客户
     */
    @DataTracerFieldLabel("返修客户")
    private String customer;

    /**
     * 历史
     */
    @DataTracerFieldLabel("历史")
    private String history;


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

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 破损仓喷头
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_damaged_sprinkler")
public class DamagedSprinklerEntity {
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
     * 返仓日期
     */
    @DataTracerFieldLabel("返仓日期")
    private LocalDate retWarehouseDate;

    /**
     * 备注1
     */
    @DataTracerFieldLabel("备注1")
    private String note1;

    /**
     * 破损原因分类
     */
    @DataTracerFieldLabel("破损原因分类")
    private String damagedReasonType;

    /**
     * 具体破损原因
     */
    @DataTracerFieldLabel("具体破损原因")
    private String realDamagedReason;

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

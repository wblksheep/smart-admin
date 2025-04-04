package net.lab1024.sa.admin.module.business.sprinkler.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 喷头
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_sprinkler")
public class SprinklerEntity {

    /**
     * 喷头ID
     */
    @TableId(type = IdType.AUTO)
    private Long sprinklerId;

    /**
     * 喷头序列号
     */
    @DataTracerFieldLabel("喷头序列号")
    private String sprinklerSerial;

    /**
     * 入仓日期
     */
    @DataTracerFieldLabel("入仓日期")
    private LocalDate warehouseDate;

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

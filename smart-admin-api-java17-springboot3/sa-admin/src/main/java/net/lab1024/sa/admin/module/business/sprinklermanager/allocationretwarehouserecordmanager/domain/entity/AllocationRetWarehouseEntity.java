package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;
import org.apache.ibatis.annotations.UpdateProvider;

import java.time.LocalDateTime;

/**
 * 领用与返仓信息记录表
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_allocation_ret_warehouse")
public class AllocationRetWarehouseEntity {
    /**
     * 领用与返仓信息ID
     */
    @TableId(type = IdType.AUTO)
    private Long allocationRetWarehouseId;

    /**
     * 领用与返仓信息记录ID
     */
    private Long recordId;

    /**
     * 领用喷头ID
     */
    private Long allocateSprinklerId;

    /**
     * 领用喷头序列号
     */
    @DataTracerFieldLabel("领用喷头序列号")
    private String allocateSprinklerSerial;

    /**
     * 领用喷头型号
     */
    @DataTracerFieldLabel("领用喷头型号")
    private String allocateSprinklerModel;

    /**
     * 领用用途
     */
    @DataTracerFieldLabel("领用用途")
    private String allocatePurpose;

    /**
     * 领用颜色
     */
    @DataTracerFieldLabel("领用颜色")
    private String allocateColor;

    /**
     * 领用位置
     */
    @DataTracerFieldLabel("领用位置")
    private Byte allocatePosition;

    /**
     * 领用喷头类型
     */
    @DataTracerFieldLabel("领用喷头类型")
    private Boolean allocateSprinklerIsNew;

    /**
     * 返仓喷头ID
     */
    private Long retWarehouseSprinklerId;

    /**
     * 返仓喷头序列号
     */
    @DataTracerFieldLabel("返仓喷头序列号")
    private String retWarehouseSprinklerSerial;
    /**
     * 返仓喷头型号
     */
    @DataTracerFieldLabel("返仓喷头型号")
    private String retWarehouseSprinklerModel;
    /**
     * 返仓机台
     */
    @DataTracerFieldLabel("返仓机台")
    private String retWarehouseMachine;
    /**
     * 返仓颜色
     */
    @DataTracerFieldLabel("返仓颜色")
    private String retWarehouseColor;
    /**
     * 返仓位置
     */
    @DataTracerFieldLabel("返仓位置")
    private Byte retWarehousePosition;
    /**
     * 返仓喷头类型
     */
    @DataTracerFieldLabel("返仓喷头类型")
    private Boolean retWarehouseSprinklerIsNew;
    /**
     * 返仓原因
     */
    @DataTracerFieldLabel("返仓原因")
    private String retWarehouseReason;

    /**
     * 具体原因
     */
    @DataTracerFieldLabel("具体原因")
    private String retWarehouseRealReason;

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

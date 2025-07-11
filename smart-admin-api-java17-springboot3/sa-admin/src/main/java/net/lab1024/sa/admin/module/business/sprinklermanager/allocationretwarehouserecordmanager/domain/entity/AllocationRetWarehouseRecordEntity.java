package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.sa.base.module.support.datatracer.annoation.DataTracerFieldLabel;

import java.time.LocalDateTime;

/**
 * 领用与返仓信息记录表
 *
 * @Author 海印: 芦苇
 */
@Data
@TableName("t_allocation_ret_warehouse_record")
public class AllocationRetWarehouseRecordEntity {
    /**
     * 领用与返仓信息记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 审核状态
     */
    @DataTracerFieldLabel("审核状态")
    private Byte status;

    /**
     * 标记状态
     */
    @DataTracerFieldLabel("标记状态")
    private Boolean markedFlag;

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

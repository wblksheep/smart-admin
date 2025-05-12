package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.constant;

import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 领用与返仓审核类型
 *
 * @Author 海印：芦苇
 */
@Getter
public enum AllocationRetWarehouseTypeEnum implements BaseEnum {
    /**
     * 审核中
     */
    UNDER_REVIEW((byte)0, "待审核"),

    /**
     * 已通过
     */
    APPROVED((byte)1, "已通过"),

    /**
     * 未通过
     */
    NOTAPPROVED((byte)2, "未通过"),
    ;

    private Byte value;

    private String desc;

    AllocationRetWarehouseTypeEnum(Byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

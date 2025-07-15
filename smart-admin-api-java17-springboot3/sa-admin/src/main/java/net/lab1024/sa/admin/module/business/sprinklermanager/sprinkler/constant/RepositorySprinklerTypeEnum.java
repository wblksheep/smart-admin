package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant;


import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 喷头仓库类型
 *
 * @Author 海印：芦苇
 */
@Getter
public enum RepositorySprinklerTypeEnum implements BaseEnum {

    /**
     * 可用仓
     */
    USABLE_REPOSITORY((byte)0, "usable"),

    /**
     * 机台
     */
    MACHINE_REPOSITORY((byte)1, "machine"),

    /**
     * 维修仓
     */
    MAINTAINING_REPOSITORY((byte)2, "maintaining"),

    /**
     * 破损仓
     */
    DAMAGED_REPOSITORY((byte)3, "damaged"),

    /**
     * RMA
     */
    RMA_REPOSITORY((byte)4, "rma"),

    /**
     * 领用中
     */
    ALLOCATING_REPOSITORY((byte)5, "allocating"),
    ;

    // 状态值映射缓存（优化查询性能）
    private static final Map<Byte, RepositorySprinklerTypeEnum> STATUS_MAP = new HashMap<>();

    static {
        for (RepositorySprinklerTypeEnum type : values()) {
            STATUS_MAP.put(type.value, type);
        }
    }

    private Byte value;
    private String desc;

    RepositorySprinklerTypeEnum(Byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    /**
     * 根据状态值获取枚举（优化点：缓存加速/Optional安全返回）
     */
    public static Optional<RepositorySprinklerTypeEnum> fromStatus(byte status) {
        return Optional.ofNullable(STATUS_MAP.get(status));
    }
}

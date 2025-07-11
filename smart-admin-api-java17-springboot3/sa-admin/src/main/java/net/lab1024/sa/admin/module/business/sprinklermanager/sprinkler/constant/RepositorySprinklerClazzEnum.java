package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.*;

public enum RepositorySprinklerClazzEnum {
    USABLE(0, UsableSprinklerImportForm.class),
//    MACHINE(1, MachineSprinklerImportForm.class),
//    MAINTAINING(2, MaintainingSprinklerImportForm.class),
//    DAMAGED(3, DamagedSprinklerImportForm.class),
//    RMA(4, RmaSprinklerImportForm.class),
    ;

    private final int code;
    private final Class<? extends BaseImportForm> clazz;

    RepositorySprinklerClazzEnum(int code, Class<? extends BaseImportForm> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    // 根据整型code获取枚举对象（关键转换）
    public static RepositorySprinklerClazzEnum fromCode(int code) {
        for (RepositorySprinklerClazzEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    // 获取关联的Class对象
    public Class<? extends BaseImportForm> getSprinklerClass() {
        return this.clazz;
    }
}

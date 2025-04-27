package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.annotation;


import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.ConditionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 条件字段注解
 * 标注在表单字段上，声明查询条件类型
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionField {
    /**
     * 数据库列名（支持联表字段如 t1.column）
     */
    String column();

    /**
     * 条件类型
     */
    ConditionType type() default ConditionType.EQUAL;
}

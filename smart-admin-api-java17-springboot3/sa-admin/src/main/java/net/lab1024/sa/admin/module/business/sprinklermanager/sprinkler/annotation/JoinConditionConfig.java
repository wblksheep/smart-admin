package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinConditionConfig {
    Class<?> tableClass();
}

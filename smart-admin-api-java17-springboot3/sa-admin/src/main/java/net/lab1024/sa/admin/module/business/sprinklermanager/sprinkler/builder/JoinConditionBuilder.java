package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.builder;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * 联表条件构建器接口（策略模式）
 * 通过接口规范条件构建行为
 */
public interface JoinConditionBuilder {
    void buildJoinConditions(MPJLambdaWrapper<?> wrapper);
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RepositorySprinklerTransferStrategyFactory {
    private Map<Byte, RepositorySprinklerTransferStrategy<?>> strategyMap = new HashMap<>();

    @Autowired
    public RepositorySprinklerTransferStrategyFactory(List<RepositorySprinklerTransferStrategy<?>> strategies) {
        strategies.forEach(strategy -> {
            // 假设不同类型通过注解或枚举绑定Byte值
            strategyMap.put(getTypeFromStrategy(strategy), strategy);
        });


    }

    public RepositorySprinklerTransferStrategy<?> getStrategy(Byte type) {
        return strategyMap.get(type);
    }

    // 实现获取策略类型的方法（可通过自定义注解）
    private Byte getTypeFromStrategy(RepositorySprinklerTransferStrategy<?> strategy) {
        Class<?> clazz = strategy.getEntityClass();
        switch (clazz.getSimpleName()) {
            case "UsableSprinklerEntity":
                return (byte) 0;
            case "MachineSprinklerEntity":
                return (byte) 1;
            case "MaintainingSprinklerEntity":
                return (byte) 2;
            case "DamagedSprinklerEntity":
                return (byte) 3;
            case "RmaSprinklerEntity":
                return (byte) 4;
            case "AllocatingSprinklerEntity":
                return (byte) 5;
            default:
                throw new IllegalArgumentException("未知的类：" + clazz.getSimpleName());
        }
    }
}

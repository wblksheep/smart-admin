package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RepositorySprinklerStrategyFactory {
    private final Map<Class<?>, RepositorySprinklerQueryStrategy<?, ?>> strategyMap = new HashMap<>();

    // 自动注入所有策略实现
    @Autowired
    public void initStrategies(List<RepositorySprinklerQueryStrategy<?, ?>> strategies) {
        for (RepositorySprinklerQueryStrategy<?, ?> strategy : strategies) {
            Type type = ((ParameterizedType) strategy.getClass()
                    .getGenericInterfaces()[0]).getActualTypeArguments()[0];
            strategyMap.put((Class<?>) type, strategy);
        }
    }

    @SuppressWarnings("unchecked")
    public <T, R> RepositorySprinklerQueryStrategy<T, R> getStrategy(Class<R> voType) {
        return (RepositorySprinklerQueryStrategy<T, R>) strategyMap.get(voType);
    }
}

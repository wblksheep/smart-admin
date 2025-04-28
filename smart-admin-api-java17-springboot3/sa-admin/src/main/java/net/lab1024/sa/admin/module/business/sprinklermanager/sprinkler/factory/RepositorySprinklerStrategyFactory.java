package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RepositorySprinklerStrategyFactory {
    private final Map<Class<? extends BaseQueryForm>, RepositorySprinklerQueryStrategy<?, ?, ?>> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public void registerStrategies(List<RepositorySprinklerQueryStrategy<?, ?, ?>> strategies) {
        strategies.forEach(strategy -> {
            // 通过反射获取泛型参数
            Type[] genericInterfaces = strategy.getClass().getGenericInterfaces();
            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    if (pType.getRawType().equals(RepositorySprinklerQueryStrategy.class)) {
                        Type[] actualTypeArgs = pType.getActualTypeArguments();
                        if (actualTypeArgs.length >= 1) {
                            Class<? extends BaseQueryForm> formType = (Class<? extends BaseQueryForm>) actualTypeArgs[0];
                            strategyMap.put(formType, strategy);
                        }
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseQueryForm, R, EXCEL> RepositorySprinklerQueryStrategy<T, R, EXCEL> getStrategy(Class<T> formType) {
        return (RepositorySprinklerQueryStrategy<T, R, EXCEL>) strategyMap.get(formType);
    }
}

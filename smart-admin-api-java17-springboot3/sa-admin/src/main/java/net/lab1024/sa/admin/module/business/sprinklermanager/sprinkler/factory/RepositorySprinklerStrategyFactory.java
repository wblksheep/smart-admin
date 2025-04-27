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

@Component
public class RepositorySprinklerStrategyFactory {
    private final Map<Class<?>, RepositorySprinklerQueryStrategy<?, ?>> strategyMap = new HashMap<>();

    @Autowired
    public void registerStrategies(List<RepositorySprinklerQueryStrategy<?, ?>> strategies) {
        strategies.forEach(strategy -> {
            // 通过泛型接口类型参数获取表单类型
            Type[] types = strategy.getClass().getGenericInterfaces();
            ParameterizedType type = (ParameterizedType) types[0];
            Class<?> formType = (Class<?>) type.getActualTypeArguments()[0];
            strategyMap.put(formType, strategy);
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseQueryForm, R> RepositorySprinklerQueryStrategy<T, R> getStrategy(Class<T> formType) {
        return (RepositorySprinklerQueryStrategy<T, R>) strategyMap.get(formType);
    }
}

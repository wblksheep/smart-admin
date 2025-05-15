package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SprinklerRepositoryFactory implements ApplicationContextAware {
    private final Map<RepositorySprinklerTypeEnum, BaseServiceImpl<?, ?>> serviceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext context) {
        // 获取所有BaseServiceImpl子类并过滤实现RepositorySprinklerType接口的Bean
        context.getBeansOfType(BaseServiceImpl.class).values().stream()
                .filter(service -> service instanceof RepositorySprinklerType)
                .forEach(service -> {
                    RepositorySprinklerTypeEnum type = ((RepositorySprinklerType) service).getSprinklerType();
                    serviceMap.put(type, service);
                });
    }

//    // 方案1：安全类型转换
//    public <T extends BaseServiceImpl<?, ?>> T getRepository(
//            RepositorySprinklerTypeEnum type,
//            Class<T> clazz
//    ) {
//        return Optional.ofNullable(serviceMap.get(type))
//                .map(clazz::cast)
//                .orElseThrow(() -> new RuntimeException("未找到类型[" + type + "]的仓储实现"));
//    }

    // 方案2：泛型类型推导
    @SuppressWarnings("unchecked")
    public <T extends BaseServiceImpl<?, ?>> T getRepository(
            RepositorySprinklerTypeEnum type
    ) {
        return (T) Optional.ofNullable(serviceMap.get(type))
                .orElseThrow(() -> new RuntimeException("未找到类型[" + type + "]的仓储实现"));
    }
}
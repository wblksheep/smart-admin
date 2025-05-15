package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.service;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl.UsableSprinklerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TypeService {

    private static final Map<Byte, Class<?>> ENTITY_CLASS_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, BaseIService<?>> REPOSITORY_CACHE = new ConcurrentHashMap<>();

    @Resource
    private UsableSprinklerRepository usableSprinklerRepository;
    @Resource
    private MachineSprinklerRepository machineSprinklerRepository;
    @Resource
    private MaintainingSprinklerRepository maintainingSprinklerRepository;
    @Resource
    private DamagedSprinklerRepository damagedSprinklerRepository;
    @Resource
    private RmaSprinklerRepository rmaSprinklerRepository;


    public BaseIService<?> getCachedRepository(Byte type) {
        // 先获取或缓存实体类
        Class<?> entityClass = ENTITY_CLASS_CACHE.computeIfAbsent(
                type,
                t -> this.getEntityClass(t)
        );
        // 再通过实体类获取或缓存仓库服务
        return REPOSITORY_CACHE.computeIfAbsent(
                entityClass,
                clazz -> this.getRepository(clazz)
        );
    }

    public Class<?> getCachedEntity(Byte type) {
        // 先获取或缓存实体类
        return ENTITY_CLASS_CACHE.computeIfAbsent(
                type,
                t -> this.getEntityClass(t)
        );
    }

    private BaseIService<?> getRepository(Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "UsableSprinklerEntity":
                return usableSprinklerRepository;
            case "MachineSprinklerEntity":
                return machineSprinklerRepository;
            case "MaintainingSprinklerEntity":
                return maintainingSprinklerRepository;
            case "DamagedSprinklerEntity":
                return damagedSprinklerRepository;
            case "RmaSprinklerEntity":
                return rmaSprinklerRepository;
            default:
                throw new IllegalArgumentException("未知的类：" + clazz.getSimpleName());
        }
    }

    private Class<?> getEntityClass(Byte type) {
        switch (type) {
            case 0:
                return UsableSprinklerEntity.class;
            case 1:
                return MachineSprinklerEntity.class;
            case 2:
                return MaintainingSprinklerEntity.class;
            case 3:
                return DamagedSprinklerEntity.class;
            case 4:
                return RmaSprinklerEntity.class;
            default:
                throw new IllegalArgumentException("未知的类型：" + type);
        }
    }
}

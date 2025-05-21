package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;

public interface RepositorySprinklerTransferStrategy<T> {
    BaseIService<T> getRepository();
    Class<T> getEntityClass();
    void updateDeletedFlag(SprinklerEntity sprinklerDetail);
    void updateRepository(SprinklerEntity sprinklerDetail);
}

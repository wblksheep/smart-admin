package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.transfer.impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.TransferRepositoryException;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.DamagedSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DamagedSprinklerTransferStrategy implements RepositorySprinklerTransferStrategy<DamagedSprinklerEntity> {

    @Resource
    private DamagedSprinklerRepository damagedSprinklerRepository;

    @Override
    public BaseIService<DamagedSprinklerEntity> getRepository() {
        return damagedSprinklerRepository;
    }

    @Override
    public Class<DamagedSprinklerEntity> getEntityClass() {
        return DamagedSprinklerEntity.class;
    }

    @Override
    public void updateDeletedFlag(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        DamagedSprinklerEntity curEntity = damagedSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(curEntity) || curEntity.getDeletedFlag()) {
            throw new TransferRepositoryException("原仓喷头不存在");
        }
        curEntity.setDeletedFlag(Boolean.TRUE);
        damagedSprinklerRepository.updateById(curEntity);
    }

    @Override
    public void updateRepository(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        DamagedSprinklerEntity nextEntity = damagedSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(nextEntity)) {
            // 如果转仓喷头不存在，则insert
            DamagedSprinklerEntity updateEntity = SmartBeanUtil.copy(sprinklerDetail, DamagedSprinklerEntity.class);
            damagedSprinklerRepository.save(updateEntity);
            return;
        }
        if (nextEntity.getDeletedFlag()) {
            // 如果转仓喷头存在，则update
            nextEntity.setDeletedFlag(Boolean.FALSE);
            damagedSprinklerRepository.updateById(nextEntity);
            return;
        }
    }
}

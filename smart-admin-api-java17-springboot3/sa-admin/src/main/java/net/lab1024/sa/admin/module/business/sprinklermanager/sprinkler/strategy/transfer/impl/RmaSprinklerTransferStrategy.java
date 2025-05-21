package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.transfer.impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.TransferRepositoryException;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.RmaSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RmaSprinklerTransferStrategy implements RepositorySprinklerTransferStrategy<RmaSprinklerEntity> {

    @Resource
    private RmaSprinklerRepository rmaSprinklerRepository;

    @Override
    public BaseIService<RmaSprinklerEntity> getRepository() {
        return rmaSprinklerRepository;
    }

    @Override
    public Class<RmaSprinklerEntity> getEntityClass() {
        return RmaSprinklerEntity.class;
    }

    @Override
    public void updateDeletedFlag(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        RmaSprinklerEntity curEntity = rmaSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(curEntity) || curEntity.getDeletedFlag()) {
            throw new TransferRepositoryException("原仓喷头不存在");
        }
        curEntity.setDeletedFlag(Boolean.TRUE);
        rmaSprinklerRepository.updateById(curEntity);
    }

    @Override
    public void updateRepository(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        RmaSprinklerEntity nextEntity = rmaSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(nextEntity)) {
            // 如果转仓喷头不存在，则insert
            RmaSprinklerEntity updateEntity = SmartBeanUtil.copy(sprinklerDetail, RmaSprinklerEntity.class);
            rmaSprinklerRepository.save(updateEntity);
            return;
        }
        if (nextEntity.getDeletedFlag()) {
            // 如果转仓喷头存在，则update
            nextEntity.setDeletedFlag(Boolean.FALSE);
            rmaSprinklerRepository.updateById(nextEntity);
            return;
        }
    }
}

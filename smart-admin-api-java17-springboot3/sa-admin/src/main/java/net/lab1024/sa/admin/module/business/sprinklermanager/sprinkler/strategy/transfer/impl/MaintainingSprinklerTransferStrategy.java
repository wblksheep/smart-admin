package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.transfer.impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.TransferRepositoryException;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MaintainingSprinklerTransferStrategy implements RepositorySprinklerTransferStrategy<MaintainingSprinklerEntity> {

    @Resource
    private MaintainingSprinklerRepository maintainingSprinklerRepository;

    @Override
    public BaseIService<MaintainingSprinklerEntity> getRepository() {
        return maintainingSprinklerRepository;
    }

    @Override
    public Class<MaintainingSprinklerEntity> getEntityClass() {
        return MaintainingSprinklerEntity.class;
    }

    @Override
    public void updateDeletedFlag(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        MaintainingSprinklerEntity curEntity = maintainingSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(curEntity) || curEntity.getDeletedFlag()) {
            throw new TransferRepositoryException("原仓喷头不存在");
        }
        curEntity.setDeletedFlag(Boolean.TRUE);
        maintainingSprinklerRepository.updateById(curEntity);
    }

    @Override
    public void updateRepository(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        MaintainingSprinklerEntity nextEntity = maintainingSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(nextEntity)) {
            // 如果转仓喷头不存在，则insert
            MaintainingSprinklerEntity updateEntity = SmartBeanUtil.copy(sprinklerDetail, MaintainingSprinklerEntity.class);
            maintainingSprinklerRepository.save(updateEntity);
            return;
        }
        if (nextEntity.getDeletedFlag()) {
            // 如果转仓喷头存在，则update
            nextEntity.setDeletedFlag(Boolean.FALSE);
            maintainingSprinklerRepository.updateById(nextEntity);
            return;
        }
    }
}

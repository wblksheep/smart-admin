package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.transfer.impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.AllocatingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.TransferRepositoryException;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.AllocatingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AllocatingSprinklerTransferStrategy implements RepositorySprinklerTransferStrategy<AllocatingSprinklerEntity> {

    @Resource
    private AllocatingSprinklerRepository allocatingSprinklerRepository;

    @Override
    public BaseIService<AllocatingSprinklerEntity> getRepository() {
        return allocatingSprinklerRepository;
    }

    @Override
    public Class<AllocatingSprinklerEntity> getEntityClass() {
        return AllocatingSprinklerEntity.class;
    }

    @Override
    public void updateDeletedFlag(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        AllocatingSprinklerEntity curEntity = allocatingSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(curEntity) || curEntity.getDeletedFlag()) {
            throw new TransferRepositoryException("原仓喷头不存在");
        }
        curEntity.setDeletedFlag(Boolean.TRUE);
        allocatingSprinklerRepository.updateById(curEntity);
    }

    @Override
    public void updateRepository(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        AllocatingSprinklerEntity nextEntity = allocatingSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(nextEntity)) {
            // 如果转仓喷头不存在，则insert
            AllocatingSprinklerEntity updateEntity = SmartBeanUtil.copy(sprinklerDetail, AllocatingSprinklerEntity.class);
            allocatingSprinklerRepository.save(updateEntity);
            return;
        }
        if (nextEntity.getDeletedFlag()) {
            // 如果转仓喷头存在，则update
            nextEntity.setDeletedFlag(Boolean.FALSE);
            allocatingSprinklerRepository.updateById(nextEntity);
            return;
        }
    }
}

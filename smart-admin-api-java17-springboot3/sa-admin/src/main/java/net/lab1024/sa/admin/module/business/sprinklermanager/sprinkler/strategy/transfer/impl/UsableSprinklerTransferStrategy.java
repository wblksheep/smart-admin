package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.transfer.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.TransferRepositoryException;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UsableSprinklerTransferStrategy implements RepositorySprinklerTransferStrategy<UsableSprinklerEntity> {

    @Resource
    private UsableSprinklerRepository usableSprinklerRepository;

    @Override
    public BaseIService<UsableSprinklerEntity> getRepository() {
        return usableSprinklerRepository;
    }

    @Override
    public Class<UsableSprinklerEntity> getEntityClass() {
        return UsableSprinklerEntity.class;
    }

    @Override
    public void updateDeletedFlag(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        UsableSprinklerEntity curEntity = usableSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(curEntity) || curEntity.getDeletedFlag()) {
            throw new TransferRepositoryException("原仓喷头不存在");
        }
        curEntity.setDeletedFlag(Boolean.TRUE);
        usableSprinklerRepository.updateById(curEntity);
        LambdaUpdateWrapper<UsableSprinklerEntity> uw = new LambdaUpdateWrapper<>();
        uw.eq(UsableSprinklerEntity::getSprinklerId, curEntity.getSprinklerId())
                .set(UsableSprinklerEntity::getRetWarehouseDate, null)
                .set(UsableSprinklerEntity::getAllocateLimitation, null)
                .set(UsableSprinklerEntity::getAllocateNote1, null)
                .set(UsableSprinklerEntity::getDeletedFlag, Boolean.TRUE); // 非null字段也可在此设置
        usableSprinklerRepository.update(null, uw);
    }

    @Override
    public void updateRepository(SprinklerEntity sprinklerDetail) {
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        UsableSprinklerEntity nextEntity = usableSprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(nextEntity)) {
            // 如果转仓喷头不存在，则insert
            UsableSprinklerEntity updateEntity = SmartBeanUtil.copy(sprinklerDetail, UsableSprinklerEntity.class);
            usableSprinklerRepository.save(updateEntity);
            return;
        }
        if (nextEntity.getDeletedFlag()) {
            // 如果转仓喷头存在，则update
            nextEntity.setDeletedFlag(Boolean.FALSE);
            usableSprinklerRepository.updateById(nextEntity);
            return;
        }
    }
}

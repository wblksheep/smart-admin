package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.UsableSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsableSprinklerRepositoryImpl extends ServiceImpl<UsableSprinklerDao, UsableSprinklerEntity> implements UsableSprinklerRepository {
    @Override
    public boolean existsBySprinklerSerial(String sprinklerSerial) {
        LambdaQueryWrapper<UsableSprinklerEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UsableSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        return this.exists(queryWrapper);
    }
}

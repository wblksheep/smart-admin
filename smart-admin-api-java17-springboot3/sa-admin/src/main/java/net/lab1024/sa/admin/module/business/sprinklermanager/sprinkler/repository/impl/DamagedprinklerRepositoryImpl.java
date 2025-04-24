package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.DamagedSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.DamagedSprinklerRepository;
import org.springframework.stereotype.Service;

@Service
public class DamagedprinklerRepositoryImpl extends ServiceImpl<DamagedSprinklerDao, DamagedSprinklerEntity> implements DamagedSprinklerRepository {
}

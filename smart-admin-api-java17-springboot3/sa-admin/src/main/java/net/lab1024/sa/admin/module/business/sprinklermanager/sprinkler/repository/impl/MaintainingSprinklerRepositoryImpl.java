package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MachineSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MaintainingSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import org.springframework.stereotype.Service;

@Service
public class MaintainingSprinklerRepositoryImpl extends ServiceImpl<MaintainingSprinklerDao, MaintainingSprinklerEntity> implements MaintainingSprinklerRepository {
}

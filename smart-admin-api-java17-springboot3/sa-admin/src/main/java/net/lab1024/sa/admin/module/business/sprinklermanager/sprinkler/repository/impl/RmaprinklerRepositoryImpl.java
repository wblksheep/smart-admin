package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.RmaSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.RmaSprinklerRepository;
import org.springframework.stereotype.Service;

@Service
public class RmaprinklerRepositoryImpl extends ServiceImpl<RmaSprinklerDao, RmaSprinklerEntity> implements RmaSprinklerRepository {
}

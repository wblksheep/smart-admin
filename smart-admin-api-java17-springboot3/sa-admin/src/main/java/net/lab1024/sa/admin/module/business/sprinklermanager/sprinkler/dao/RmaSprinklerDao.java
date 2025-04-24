package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * rma喷头
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface RmaSprinklerDao extends BaseMapper<RmaSprinklerEntity> {
}

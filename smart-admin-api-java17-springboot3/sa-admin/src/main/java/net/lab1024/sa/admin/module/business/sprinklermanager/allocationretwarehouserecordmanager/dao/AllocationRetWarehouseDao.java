package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AllocationRetWarehouseDao extends BaseMapper<AllocationRetWarehouseEntity> {
}

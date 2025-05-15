package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface AllocationRetWarehouseRecordDao extends BaseMapper<AllocationRetWarehouseRecordEntity> {
    AllocationRetWarehouseRecordVO getDetail(Long recordId, Boolean deletedFlag);
}

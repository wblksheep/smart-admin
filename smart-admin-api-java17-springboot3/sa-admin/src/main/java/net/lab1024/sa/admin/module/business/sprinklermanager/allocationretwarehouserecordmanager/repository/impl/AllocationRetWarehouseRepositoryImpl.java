package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao.AllocationRetWarehouseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class AllocationRetWarehouseRepositoryImpl extends ServiceImpl<AllocationRetWarehouseDao, AllocationRetWarehouseEntity> implements AllocationRetWarehouseRepository {
//    @Override
//    public List<AllocationRetWarehouseVO> getListByQueryPage(Page<?> page, AllocationRetWarehouseQueryForm queryForm) {
//        return this.getBaseMapper().queryPage(page, queryForm);
//    }
//
//    @Override
//    public AllocationRetWarehouseVO getDetail(Long recordId, Boolean deletedFlag) {
//        return this.getBaseMapper().getDetail(recordId, deletedFlag);
//    }
}

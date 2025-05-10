package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao.AllocationRetWarehouseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocationRetWarehouseRepositoryImpl extends ServiceImpl<AllocationRetWarehouseDao, AllocationRetWarehouseEntity> implements AllocationRetWarehouseRepository {
    @Override
    public List<AllocationRetWarehouseVO> getListByQueryPage(Page<?> page, AllocationRetWarehouseQueryForm queryForm) {
        return this.getBaseMapper().queryPage(page, queryForm);
    }

    @Override
    public List<AllocationRetWarehouseVO> getDetail(Long recordId, Boolean deletedFlag) {
        return this.getBaseMapper().getDetail(recordId, deletedFlag);
    }

    @Override
    public List<AllocationRetWarehouseEntity> getListByRecordId(Long recordId) {
        LambdaQueryWrapper<AllocationRetWarehouseEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AllocationRetWarehouseEntity::getRecordId, recordId);
        return this.list(lqw);
    }
}

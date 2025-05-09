package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.dao.AllocationRetWarehouseRecordDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.AllocationRetWarehouseRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.AllocationRetWarehouseRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.AllocationRetWarehouseRecordVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocationRetWarehouseRecordRepositoryImpl extends ServiceImpl<AllocationRetWarehouseRecordDao, AllocationRetWarehouseRecordEntity> implements AllocationRetWarehouseRecordRepository {
    @Override
    public List<AllocationRetWarehouseRecordVO> getListByQueryPage(Page<?> page, AllocationRetWarehouseRecordQueryForm queryForm) {
        return this.getBaseMapper().queryPage(page, queryForm);
    }

    @Override
    public AllocationRetWarehouseRecordVO getDetail(Long recordId, Boolean deletedFlag) {
        return this.getBaseMapper().getDetail(recordId, deletedFlag);
    }
}

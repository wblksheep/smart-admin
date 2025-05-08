package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.repository.AllocationRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.dao.AllocationRecordDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.entity.AllocationRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.vo.AllocationRecordVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocationRecordRepositoryImpl extends ServiceImpl<AllocationRecordDao, AllocationRecordEntity> implements AllocationRecordRepository {
    @Override
    public List<AllocationRecordVO> getListByQueryPage(Page<?> page, AllocationRecordQueryForm queryForm) {
        return this.getBaseMapper().queryPage(page, queryForm);
    }

    @Override
    public AllocationRecordVO getDetail(Long recordId, Boolean deletedFlag) {
        return this.getBaseMapper().getDetail(recordId, deletedFlag);
    }
}

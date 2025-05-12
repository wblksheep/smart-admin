package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.dao.MaintainingRecordDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository.MaintainingRecordRepository;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintainingRecordRepositoryImpl extends ServiceImpl<MaintainingRecordDao, MaintainingRecordEntity> implements MaintainingRecordRepository {
    @Override
    public List<MaintainingRecordVO> getListByQueryPage(Page<?> page, MaintainingRecordQueryForm queryForm) {
        return this.getBaseMapper().queryPage(page, queryForm);
    }

    @Override
    public List<MaintainingRecordVO> getListByQueryPage(MaintainingRecordQueryForm queryForm) {
        return List.of();
    }

    @Override
    public MaintainingRecordVO getDetail(Long recordId, Boolean deletedFlag) {
        return this.getBaseMapper().getDetail(recordId, deletedFlag);
    }
}

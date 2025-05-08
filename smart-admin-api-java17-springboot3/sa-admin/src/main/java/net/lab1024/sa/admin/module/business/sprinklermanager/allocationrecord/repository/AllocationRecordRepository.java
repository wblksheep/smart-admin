package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.entity.AllocationRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.vo.AllocationRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllocationRecordRepository extends IService<AllocationRecordEntity> {
    List<AllocationRecordVO> getListByQueryPage(Page<?> page,@Param("queryForm") AllocationRecordQueryForm queryForm);

    AllocationRecordVO getDetail(@Param("recordId") Long recordId, @Param("deletedFlag") Boolean deletedFlag);
}

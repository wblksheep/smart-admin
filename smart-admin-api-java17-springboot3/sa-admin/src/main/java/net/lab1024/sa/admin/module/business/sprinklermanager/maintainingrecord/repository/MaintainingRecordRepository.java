package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaintainingRecordRepository extends IService<MaintainingRecordEntity> {
    List<MaintainingRecordVO> getListByQueryPage(Page<?> page, @Param("queryForm") MaintainingRecordQueryForm queryForm);

    List<MaintainingRecordVO> getListByQueryPage(@Param("queryForm") MaintainingRecordQueryForm queryForm);

    MaintainingRecordVO getDetail(@Param("recordId") Long recordId, @Param("deletedFlag") Boolean deletedFlag);

    List<MaintainingRecordEntity> getListByCondition();

    List<MaintainingRecordExcelVO> selectMaintainingRecordExcelExportData(@Param("queryForm") MaintainingRecordQueryForm queryForm);
}

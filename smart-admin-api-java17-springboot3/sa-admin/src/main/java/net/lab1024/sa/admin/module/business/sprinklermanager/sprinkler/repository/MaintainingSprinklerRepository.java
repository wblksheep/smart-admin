package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaintainingSprinklerRepository extends BaseIService<MaintainingSprinklerEntity> {
    List<MaintainingSprinklerVO> getListByQueryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") MaintainingSprinklerQueryForm joinForm);

    List<MaintainingSprinklerExcelVO> getListByQueryPage(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") MaintainingSprinklerQueryForm joinForm);

    List<MaintainingSprinklerEntity> getListByCondition();

    List<MaintainingSprinklerEntity> getListBySprinklerSerials(@Param("sprinklerSerials") List<String> sprinklerSerials, @Param("sprinklerIds") List<Long> sprinklerIds, @Param("deletedFlag") Boolean deletedFlag);
}

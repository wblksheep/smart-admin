package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.AllocatingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.AllocatingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.AllocatingSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.AllocatingSprinklerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllocatingSprinklerRepository extends BaseIService<AllocatingSprinklerEntity> {
    List<AllocatingSprinklerVO> getListByQueryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") AllocatingSprinklerQueryForm joinForm);

    List<AllocatingSprinklerExcelVO> getListByQueryPage(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") AllocatingSprinklerQueryForm joinForm);
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.RmaSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RmaSprinklerRepository extends BaseIService<RmaSprinklerEntity> {
    List<RmaSprinklerVO> getListByQueryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") RmaSprinklerQueryForm joinForm);

    List<RmaSprinklerExcelVO> getListByQueryPage(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") RmaSprinklerQueryForm joinForm);
}

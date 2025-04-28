package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.RmaSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * rma喷头
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface RmaSprinklerDao extends BaseMapper<RmaSprinklerEntity> {
    /**
     * rma喷头分页查询
     *
     */
    List<RmaSprinklerVO> queryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") RmaSprinklerQueryForm joinForm);

    List<RmaSprinklerExcelVO> queryExcel(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") RmaSprinklerQueryForm joinForm);
}

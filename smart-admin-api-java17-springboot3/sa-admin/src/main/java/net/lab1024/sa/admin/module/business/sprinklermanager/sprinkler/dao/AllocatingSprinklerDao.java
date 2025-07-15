package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.AllocatingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.AllocatingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.AllocatingSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.AllocatingSprinklerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 领用中喷头
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface AllocatingSprinklerDao extends BaseMapper<AllocatingSprinklerEntity> {
    /**
     * 领用中喷头分页查询
     *
     */
    List<AllocatingSprinklerVO> queryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") AllocatingSprinklerQueryForm joinForm);

    List<AllocatingSprinklerExcelVO> queryExcel(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") AllocatingSprinklerQueryForm joinForm);
}

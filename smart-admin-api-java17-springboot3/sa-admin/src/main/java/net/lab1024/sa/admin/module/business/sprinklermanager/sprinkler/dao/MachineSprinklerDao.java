package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 可用仓喷头
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface MachineSprinklerDao extends BaseMapper<MachineSprinklerEntity> {
    /**
     * 机台喷头分页查询
     *
     */
    List<MachineSprinklerVO> queryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm,@Param("joinForm") MachineSprinklerQueryForm joinForm);

    List<MachineSprinklerExcelVO> queryExcel(@Param("queryForm") SprinklerQueryForm queryForm,@Param("joinForm") MachineSprinklerQueryForm joinForm);
}

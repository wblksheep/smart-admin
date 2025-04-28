package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
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
public interface UsableSprinklerDao extends BaseMapper<UsableSprinklerEntity> {
    /**
     * 可用仓喷头分页查询
     *
     */
    List<UsableSprinklerVO> queryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") UsableSprinklerQueryForm joinForm);

    List<UsableSprinklerExcelVO> queryExcel(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") UsableSprinklerQueryForm joinForm);
}

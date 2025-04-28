package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerVO;
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
public interface MaintainingSprinklerDao extends BaseMapper<MaintainingSprinklerEntity> {
    /**
     * 维修仓喷头分页查询
     *
     */
    List<MaintainingSprinklerVO> queryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") MaintainingSprinklerQueryForm joinForm);
}

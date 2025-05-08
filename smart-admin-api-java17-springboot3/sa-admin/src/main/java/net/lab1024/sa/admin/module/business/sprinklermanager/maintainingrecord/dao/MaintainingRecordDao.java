package net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.form.MaintainingRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.vo.MaintainingRecordVO;
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
 * 维修记录
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface MaintainingRecordDao extends BaseMapper<MaintainingRecordEntity> {

    /**
     * 维修记录分页查询
     *
     */
    List<MaintainingRecordVO> queryPage(Page<?> page, @Param("queryForm") MaintainingRecordQueryForm queryForm);

    MaintainingRecordVO getDetail(@Param("recordId") Long recordId,@Param("deletedFlag") Boolean deletedFlag);

//    List<UsableSprinklerExcelVO> queryExcel(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") UsableSprinklerQueryForm joinForm);
}

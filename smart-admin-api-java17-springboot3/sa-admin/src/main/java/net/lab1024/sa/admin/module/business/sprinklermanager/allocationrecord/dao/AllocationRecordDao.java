package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.entity.AllocationRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.vo.AllocationRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 领用记录
 *
 * @Author 海印: 芦苇
 */
@Mapper
@Component
public interface AllocationRecordDao extends BaseMapper<AllocationRecordEntity> {
    /**
     * 领用记录分页查询
     *
     */
    List<AllocationRecordVO> queryPage(Page<?> page, @Param("queryForm") AllocationRecordQueryForm queryForm);

    AllocationRecordVO getDetail(@Param("recordId") Long recordId,@Param("deletedFlag") Boolean deletedFlag);

//    List<UsableSprinklerExcelVO> queryExcel(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") UsableSprinklerQueryForm joinForm);
}

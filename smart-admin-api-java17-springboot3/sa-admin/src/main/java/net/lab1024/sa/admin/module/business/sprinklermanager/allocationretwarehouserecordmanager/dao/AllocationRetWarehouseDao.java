package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AllocationRetWarehouseDao extends BaseMapper<AllocationRetWarehouseEntity> {
    /**
     * 领用与返仓分页查询
     */
    List<AllocationRetWarehouseVO> queryPage(Page<?> page, @Param("queryForm") AllocationRetWarehouseQueryForm queryForm);

    /**
     * 领用与返仓详情
     */
    List<AllocationRetWarehouseVO> getDetail(@Param("recordId") Long recordId, @Param("deletedFlag") Boolean deletedFlag);

    List<AllocationRetWarehouseExcelVO> query(@Param("queryForm") AllocationRetWarehouseQueryForm queryForm);
}

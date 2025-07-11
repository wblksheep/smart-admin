package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseEntity;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseRecordQueryForm;
//import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AllocationRetWarehouseRepository extends IService<AllocationRetWarehouseEntity> {
    List<AllocationRetWarehouseVO> getListByQueryPage(Page<?> page, @Param("queryForm") AllocationRetWarehouseQueryForm queryForm);

    List<AllocationRetWarehouseVO> getDetail(@Param("recordId") Long recordId, @Param("deletedFlag") Boolean deletedFlag);

    List<AllocationRetWarehouseEntity> getListByRecordId(@Param("recordId") Long recordId);

    List<AllocationRetWarehouseEntity> getListByAllocationRetWarehouseId(@Param("allocationRetWarehouseIds") Set<Long> allocationRetWarehouseIds);

    List<AllocationRetWarehouseExcelVO> getListRetMaintainenceDesc(@Param("queryForm") AllocationRetWarehouseQueryForm queryForm);
}

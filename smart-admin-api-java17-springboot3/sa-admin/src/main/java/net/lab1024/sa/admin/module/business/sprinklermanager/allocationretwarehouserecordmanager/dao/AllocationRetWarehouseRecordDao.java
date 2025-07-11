package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.system.datascope.DataScope;
import net.lab1024.sa.admin.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.sa.admin.module.system.datascope.constant.DataScopeWhereInTypeEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AllocationRetWarehouseRecordDao extends BaseMapper<AllocationRetWarehouseRecordEntity> {
    AllocationRetWarehouseRecordVO getDetail(Long recordId, Boolean deletedFlag);

    /**
     * 领用与返仓记录无分页查询
     */
    List<AllocationRetWarehouseRecordVO> queryPageWithoutPage(@Param("queryForm") AllocationRetWarehouseQueryForm queryForm);

    @DataScope(dataScopeType = DataScopeTypeEnum.ALLOCATIONRETWAREHOUSERECORD, whereInType = DataScopeWhereInTypeEnum.DEPARTMENT, joinSql = "create_user_id in (#employeeIds)")
    List<AllocationRetWarehouseRecordVO> queryPage(Page<?> page, @Param("distinctIds") List<Long> distinctIds, Boolean markedFlag);
}

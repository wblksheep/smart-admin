package net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.dao.AllocationRetWarehouseRecordDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.form.AllocationRetWarehouseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.vo.AllocationRetWarehouseRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.repository.AllocationRetWarehouseRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationretwarehouserecordmanager.domain.entity.AllocationRetWarehouseRecordEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AllocationRetWarehouseRecordRepositoryImpl extends ServiceImpl<AllocationRetWarehouseRecordDao, AllocationRetWarehouseRecordEntity> implements AllocationRetWarehouseRecordRepository {
    @Override
    public AllocationRetWarehouseRecordVO getDetail(Long recordId, Boolean deletedFlag) {
        return this.getBaseMapper().getDetail(recordId, deletedFlag);
    }

    @Override
    public List<AllocationRetWarehouseRecordVO> getListByQueryPage(Page<?> page, AllocationRetWarehouseQueryForm queryForm) {
        // 1. 去重逻辑优化（基于Java8 Stream特性）
        List<Long> distinctIds = this.getBaseMapper().queryPageWithoutPage(queryForm)
                .stream()
                .collect(Collectors.toMap(
                        AllocationRetWarehouseRecordVO::getRecordId,
                        Function.identity(),
                        (existing, replacement) -> replacement,// 声明重复键时的覆盖策略，保留最后一个出现的元素
                        LinkedHashMap::new
                ))
                .keySet()
                .stream()
                .collect(Collectors.toList());
        if (distinctIds.size() == 0) {
            return new ArrayList<>();
        }

        // 3. 执行分页查询（遵循MyBatis-Plus分页规范）
        return this.getBaseMapper().queryPage(page, distinctIds);

    }
}

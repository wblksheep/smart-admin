package net.lab1024.sa.admin.module.business.sprinklermanager.machine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.vo.MachineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MachineDao extends BaseMapper<MachineEntity> {
    List<MachineVO> queryPage(Page<?> page, @Param("queryForm") MachineQueryForm queryForm);

    MachineVO getDetail(Long machineId, Boolean deletedFlag);
}

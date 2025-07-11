package net.lab1024.sa.admin.module.business.sprinklermanager.machine.repository;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.dao.MachineDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.vo.MachineVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.List;

@Service
public class MachineRepository extends ServiceImpl<MachineDao, MachineEntity> {
    public MachineEntity getByMachineName(@Param("machineName") String machineName, @Param("excludeMachineId") Long excludeMachineId, @Param("deletedFlag") Boolean deletedFlag) {
        LambdaQueryWrapper<MachineEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MachineEntity::getMachineName, machineName);
        lqw.eq(MachineEntity::getDeletedFlag, deletedFlag);
        if (excludeMachineId != null) {
            lqw.ne(MachineEntity::getMachineId, excludeMachineId);
        }
        return this.getBaseMapper().selectOne(lqw);
    }

    public List<MachineVO> queryPage(Page<?> page, @Param("queryForm") MachineQueryForm queryForm) {
        return this.getBaseMapper().queryPage(page, queryForm);
    }

    public MachineVO getDetail(Long machineId, Boolean deletedFlag) {
        return this.getBaseMapper().getDetail(machineId, deletedFlag);
    }

    public void deleteMachine(Long machineId, Boolean deletedFlag) {
        LambdaUpdateWrapper<MachineEntity> luw = new LambdaUpdateWrapper<>();
        luw.eq(MachineEntity::getMachineId, machineId).set(MachineEntity::getDeletedFlag, deletedFlag);
        this.update(luw);
    }

    public List<MachineEntity> getByMachineNames(String machineType) {
        LambdaQueryWrapper<MachineEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MachineEntity::getMachineType, machineType);
        lqw.eq(MachineEntity::getDeletedFlag, Boolean.FALSE);
        return this.getBaseMapper().selectList(lqw);
    }
}

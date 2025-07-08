package net.lab1024.sa.admin.module.business.sprinklermanager.machine;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.entity.MachineEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.form.MachineUpdateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.domain.vo.MachineVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.machine.repository.MachineRepository;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MachineService {

    @Resource
    private MachineRepository machineRepository;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> create(@Valid MachineCreateForm createVO) {
        MachineEntity validateMachine = machineRepository.getByMachineName(createVO.getMachineName(), null, Boolean.FALSE);
        if (Objects.nonNull(validateMachine)) {
            return ResponseDTO.userErrorParam("机台名称重复");
        }
        // 数据插入
        MachineEntity insertMachine = SmartBeanUtil.copy(createVO, MachineEntity.class);
        machineRepository.save(insertMachine);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(@Valid MachineUpdateForm updateVO) {
        Long machineId = updateVO.getMachineId();
        // 校验机台是否存在
        MachineEntity machineDetail = machineRepository.getById(machineId);
        if (Objects.isNull(machineDetail) || machineDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("机台不存在");
        }
        // 验证机台名称是否重复
        MachineEntity validateMachine = machineRepository.getByMachineName(updateVO.getMachineName(), machineId, Boolean.FALSE);
        if (Objects.nonNull(validateMachine)) {
            return ResponseDTO.userErrorParam("机台名称重复");
        }
        // 数据编辑
        MachineEntity updateEntity = SmartBeanUtil.copy(machineDetail, MachineEntity.class);
        SmartBeanUtil.copyProperties(updateVO, updateEntity);
        machineRepository.updateById(updateEntity);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询机台模块
     */
    public ResponseDTO<PageResult<MachineVO>> queryByPage(@Valid MachineQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MachineVO> machineList = machineRepository.queryPage(page, queryForm);
        PageResult<MachineVO> pageResult = SmartPageUtil.convert2PageResult(page, machineList);
        return ResponseDTO.ok(pageResult);
    }


    /**
     * 查询机台详情
     */
    public MachineVO getDetail(Long machineId) {
        return machineRepository.getDetail(machineId, Boolean.FALSE);
    }

    /**
     * 删除机台
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long machineId) {
        //校验机台是否存在
        MachineEntity machineDetail = machineRepository.getById(machineId);
        if (Objects.isNull(machineDetail) || machineDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        machineRepository.deleteMachine(machineId, Boolean.TRUE);
        return ResponseDTO.ok();
    }
}

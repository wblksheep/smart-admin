package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MachineSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.UsableSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineSprinklerRepositoryImpl extends BaseServiceImpl<MachineSprinklerDao, MachineSprinklerEntity> implements MachineSprinklerRepository, RepositorySprinklerType {
    @Override
    public List<MachineSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, MachineSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<MachineSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, MachineSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }

    @Override
    public BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag) {
        MachineSprinklerEntity machineEntity = this.getById(sprinklerEntity.getSprinklerId());
        MachineSprinklerVO vo = new MachineSprinklerVO();

        // 分步拷贝
        SmartBeanUtil.copyProperties(sprinklerEntity, vo);   // 源1
        SmartBeanUtil.copyProperties(machineEntity, vo);     // 源2
        return vo;
    }

    @Override
    public MachineSprinklerEntity getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag) {
        LambdaQueryWrapper<MachineSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MachineSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        lqw.eq(MachineSprinklerEntity::getSprinklerId, sprinklerId);
        lqw.ne(MachineSprinklerEntity::getDeletedFlag, deletedFlag);
        return this.getBaseMapper().selectOne(lqw);
    }

    @Override
    public void myUpdateById(Object updateEntity) {
        this.updateById((MachineSprinklerEntity) updateEntity);
    }

    @Override
    public RepositorySprinklerTypeEnum getSprinklerType() {
        return RepositorySprinklerTypeEnum.MACHINE_REPOSITORY;
    }
}

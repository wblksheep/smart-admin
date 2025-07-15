package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.AllocatingSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MachineSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.AllocatingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.AllocatingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.AllocatingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllocatingSprinklerRepositoryImpl extends BaseServiceImpl<AllocatingSprinklerDao, AllocatingSprinklerEntity> implements AllocatingSprinklerRepository, RepositorySprinklerType {
    @Override
    public List<AllocatingSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, AllocatingSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<AllocatingSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, AllocatingSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }

    @Override
    public BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag) {
        AllocatingSprinklerEntity allocatingEntity = this.getById(sprinklerEntity.getSprinklerId());
        AllocatingSprinklerVO vo = new AllocatingSprinklerVO();

        // 分步拷贝
        SmartBeanUtil.copyProperties(sprinklerEntity, vo);   // 源1
        SmartBeanUtil.copyProperties(allocatingEntity, vo);     // 源2
        return vo;
    }

    @Override
    public AllocatingSprinklerEntity getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag) {
        LambdaQueryWrapper<AllocatingSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AllocatingSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        lqw.eq(AllocatingSprinklerEntity::getSprinklerId, sprinklerId);
        lqw.ne(AllocatingSprinklerEntity::getDeletedFlag, deletedFlag);
        return this.getBaseMapper().selectOne(lqw);
    }

    @Override
    public void myUpdateById(Object updateEntity) {
        this.updateById((AllocatingSprinklerEntity) updateEntity);
    }

    @Override
    public RepositorySprinklerTypeEnum getSprinklerType() {
        return RepositorySprinklerTypeEnum.ALLOCATING_REPOSITORY;
    }
}

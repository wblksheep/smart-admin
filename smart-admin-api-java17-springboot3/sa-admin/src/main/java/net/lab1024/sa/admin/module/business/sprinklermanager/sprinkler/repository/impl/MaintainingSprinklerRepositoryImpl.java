package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.maintainingrecord.domain.entity.MaintainingRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MachineSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MaintainingSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MaintainingSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.DamagedSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintainingSprinklerRepositoryImpl extends BaseServiceImpl<MaintainingSprinklerDao, MaintainingSprinklerEntity> implements MaintainingSprinklerRepository, RepositorySprinklerType {
    @Override
    public List<MaintainingSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, MaintainingSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<MaintainingSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, MaintainingSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }

    @Override
    public List<MaintainingSprinklerEntity> getListByCondition() {
        LambdaQueryWrapper<MaintainingSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.lt(MaintainingSprinklerEntity::getRetMaintainenceDate, "2025-04-01");
        lqw.eq(MaintainingSprinklerEntity::getRetMaintainenceReason, "活性堵嘴歪针");
        return this.list(lqw);
    }

    @Override
    public List<MaintainingSprinklerEntity> getListBySprinklerSerials(List<String> sprinklerSerials, List<Long> sprinklerIds, Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.in(MaintainingSprinklerEntity::getSprinklerSerial, sprinklerSerials);
        lqw.eq(MaintainingSprinklerEntity::getDeletedFlag, deletedFlag);
        if (sprinklerIds != null && sprinklerIds.size() > 0) {
            lqw.in(MaintainingSprinklerEntity::getSprinklerId, sprinklerIds);
        }
        return this.list(lqw);
    }

    @Override
    public MaintainingSprinklerEntity getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag) {
        LambdaQueryWrapper<MaintainingSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MaintainingSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        lqw.eq(MaintainingSprinklerEntity::getSprinklerId, sprinklerId);
        lqw.ne(MaintainingSprinklerEntity::getDeletedFlag, deletedFlag);
        return this.getBaseMapper().selectOne(lqw);
    }

    @Override
    public BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag) {
        MaintainingSprinklerEntity maintainingEntity = this.getById(sprinklerEntity.getSprinklerId());
        MaintainingSprinklerVO vo = new MaintainingSprinklerVO();

        // 分步拷贝
        SmartBeanUtil.copyProperties(sprinklerEntity, vo);   // 源1
        SmartBeanUtil.copyProperties(maintainingEntity, vo);     // 源2
        return vo;
    }


    @Override
    public void myUpdateById(Object updateEntity) {
        this.getBaseMapper().updateById((MaintainingSprinklerEntity) updateEntity);
    }

    @Override
    public RepositorySprinklerTypeEnum getSprinklerType() {
        return RepositorySprinklerTypeEnum.MAINTAINING_REPOSITORY;
    }
}

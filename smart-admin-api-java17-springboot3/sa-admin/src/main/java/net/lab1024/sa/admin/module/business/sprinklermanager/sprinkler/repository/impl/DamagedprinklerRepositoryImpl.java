package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.DamagedSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.DamagedSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.DamagedSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.DamagedSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.DamagedSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamagedprinklerRepositoryImpl extends BaseServiceImpl<DamagedSprinklerDao, DamagedSprinklerEntity> implements DamagedSprinklerRepository, RepositorySprinklerType {

    @Override
    public List<DamagedSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, DamagedSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<DamagedSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, DamagedSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }

    @Override
    public BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag) {
        DamagedSprinklerEntity damagedEntity = this.getById(sprinklerEntity.getSprinklerId());
        DamagedSprinklerVO vo = new DamagedSprinklerVO();

        // 分步拷贝
        SmartBeanUtil.copyProperties(sprinklerEntity, vo);   // 源1
        SmartBeanUtil.copyProperties(damagedEntity, vo);     // 源2
        return vo;
    }

    @Override
    public DamagedSprinklerEntity getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag) {
        LambdaQueryWrapper<DamagedSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DamagedSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        lqw.eq(DamagedSprinklerEntity::getSprinklerId, sprinklerId);
        lqw.ne(DamagedSprinklerEntity::getDeletedFlag, deletedFlag);
        return this.getBaseMapper().selectOne(lqw);
    }

    @Override
    public void myUpdateById(Object updateEntity) {
        this.getBaseMapper().updateById((DamagedSprinklerEntity) updateEntity);
    }

    @Override
    public RepositorySprinklerTypeEnum getSprinklerType() {
        return RepositorySprinklerTypeEnum.DAMAGED_REPOSITORY;
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.RmaSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.RmaSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.RmaSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.RmaSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RmaprinklerRepositoryImpl extends BaseServiceImpl<RmaSprinklerDao, RmaSprinklerEntity> implements RmaSprinklerRepository, RepositorySprinklerType {
    @Override
    public List<RmaSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, RmaSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<RmaSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, RmaSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }

    @Override
    public BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag) {
        RmaSprinklerEntity rmaEntity = this.getById(sprinklerEntity.getSprinklerId());
        RmaSprinklerVO vo = new RmaSprinklerVO();

        // 分步拷贝
        SmartBeanUtil.copyProperties(sprinklerEntity, vo);   // 源1
        SmartBeanUtil.copyProperties(rmaEntity, vo);     // 源2
        return vo;
    }

    @Override
    public RmaSprinklerEntity getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag) {
        LambdaQueryWrapper<RmaSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RmaSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        lqw.eq(RmaSprinklerEntity::getSprinklerId, sprinklerId);
        lqw.ne(RmaSprinklerEntity::getDeletedFlag, deletedFlag);
        return this.getBaseMapper().selectOne(lqw);
    }

    @Override
    public void myUpdateById(Object updateEntity) {
        this.updateById((RmaSprinklerEntity) updateEntity);
    }

    @Override
    public RepositorySprinklerTypeEnum getSprinklerType() {
        return RepositorySprinklerTypeEnum.RMA_REPOSITORY;
    }
}

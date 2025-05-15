package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerType;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.UsableSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsableSprinklerRepositoryImpl extends BaseServiceImpl<UsableSprinklerDao, UsableSprinklerEntity> implements UsableSprinklerRepository, RepositorySprinklerType {
    @Override
    public List<UsableSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, UsableSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<UsableSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, UsableSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }

    @Override
    public BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag) {
        UsableSprinklerEntity usableEntity = this.getById(sprinklerEntity.getSprinklerId());
        UsableSprinklerVO vo = new UsableSprinklerVO();

        // 分步拷贝
        SmartBeanUtil.copyProperties(sprinklerEntity, vo);   // 源1
        SmartBeanUtil.copyProperties(usableEntity, vo);     // 源2
        return vo;
    }

    @Override
    public UsableSprinklerEntity getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag) {
        LambdaQueryWrapper<UsableSprinklerEntity> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UsableSprinklerEntity::getSprinklerSerial, sprinklerSerial);
        lqw.eq(UsableSprinklerEntity::getSprinklerId, sprinklerId);
        lqw.ne(UsableSprinklerEntity::getDeletedFlag, deletedFlag);
        return this.getBaseMapper().selectOne(lqw);
    }

    @Override
    public void myUpdateById(Object updateEntity) {
        this.updateById((UsableSprinklerEntity) updateEntity);
    }

    @Override
    public RepositorySprinklerTypeEnum getSprinklerType() {
        return RepositorySprinklerTypeEnum.USABLE_REPOSITORY;
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.DamagedSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprinklerRepositoryImpl extends ServiceImpl<SprinklerDao, SprinklerEntity> implements SprinklerRepository {

    @Override
    public List<SprinklerEntity> getListBySprinklerSerials(List<String> sprinklerSerials) {
        LambdaQueryWrapper<SprinklerEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SprinklerEntity::getSprinklerSerial, sprinklerSerials);
        return this.list(queryWrapper);
    }

    @Override
    public List<SprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm) {
        return this.getBaseMapper().queryPage(page, queryForm);
    }

    @Override
    public List<SprinklerExcelVO> selectSprinklerExcelExportData(SprinklerQueryForm queryForm) {
        return this.getBaseMapper().selectExcelExportData(queryForm);
    }

}

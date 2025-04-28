package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.UsableSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsableSprinklerRepositoryImpl extends ServiceImpl<UsableSprinklerDao, UsableSprinklerEntity> implements UsableSprinklerRepository {
    @Override
    public List<UsableSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, UsableSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<UsableSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, UsableSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.MachineSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.UsableSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.MachineSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineSprinklerRepositoryImpl extends ServiceImpl<MachineSprinklerDao, MachineSprinklerEntity> implements MachineSprinklerRepository {
    @Override
    public List<MachineSprinklerVO> getListByQueryPage(Page<?> page, SprinklerQueryForm queryForm, MachineSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryPage(page, queryForm, joinForm);
    }

    @Override
    public List<MachineSprinklerExcelVO> getListByQueryPage(SprinklerQueryForm queryForm, MachineSprinklerQueryForm joinForm) {
        return this.getBaseMapper().queryExcel(queryForm, joinForm);
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MachineSprinklerStrategy implements RepositorySprinklerQueryStrategy<MachineSprinklerQueryForm, MachineSprinklerVO, MachineSprinklerExcelVO> {

    @Autowired
    private MachineSprinklerRepository machineSprinklerRepository;

    @Override
    public List<MachineSprinklerExcelVO> executeExport(SprinklerQueryForm queryForm, MachineSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return machineSprinklerRepository.getListByQueryPage(queryForm, joinForm);
    }

    @Override
    public List<MachineSprinklerVO> executeQuery(Page<?> page, SprinklerQueryForm queryForm, MachineSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return machineSprinklerRepository.getListByQueryPage(page, queryForm, joinForm);
    }

    @Override
    public Class<MachineSprinklerVO> getResultType() {
        return MachineSprinklerVO.class;
    }

    @Override
    public Class<MachineSprinklerExcelVO> getExcelResultType() {
        return MachineSprinklerExcelVO.class;
    }
}

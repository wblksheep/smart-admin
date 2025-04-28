package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MaintainingSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MaintainingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MaintainingSprinklerStrategy implements RepositorySprinklerQueryStrategy<MaintainingSprinklerQueryForm, MaintainingSprinklerVO, MaintainingSprinklerExcelVO> {

    @Autowired
    private MaintainingSprinklerRepository maintainingSprinklerRepository;


    @Override
    public List<MaintainingSprinklerExcelVO> executeExport(SprinklerQueryForm queryForm, MaintainingSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return maintainingSprinklerRepository.getListByQueryPage(queryForm, joinForm);
    }

    @Override
    public List<MaintainingSprinklerVO> executeQuery(Page<?> page, SprinklerQueryForm queryForm, MaintainingSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return maintainingSprinklerRepository.getListByQueryPage(page, queryForm, joinForm);
    }

    @Override
    public Class<MaintainingSprinklerVO> getResultType() {
        return MaintainingSprinklerVO.class;
    }

    @Override
    public Class<MaintainingSprinklerExcelVO> getExcelResultType() {
        return MaintainingSprinklerExcelVO.class;
    }
}

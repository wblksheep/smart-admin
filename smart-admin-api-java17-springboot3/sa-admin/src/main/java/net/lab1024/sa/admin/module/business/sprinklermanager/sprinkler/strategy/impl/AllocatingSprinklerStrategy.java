package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.AllocatingSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.AllocatingSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.AllocatingSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.MachineSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.AllocatingSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.MachineSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllocatingSprinklerStrategy implements RepositorySprinklerQueryStrategy<AllocatingSprinklerQueryForm, AllocatingSprinklerVO, AllocatingSprinklerExcelVO> {

    @Autowired
    private AllocatingSprinklerRepository allocatingSprinklerRepository;

    @Override
    public List<AllocatingSprinklerExcelVO> executeExport(SprinklerQueryForm queryForm, AllocatingSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return allocatingSprinklerRepository.getListByQueryPage(queryForm, joinForm);
    }

    @Override
    public List<AllocatingSprinklerVO> executeQuery(Page<?> page, SprinklerQueryForm queryForm, AllocatingSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return allocatingSprinklerRepository.getListByQueryPage(page, queryForm, joinForm);
    }

    @Override
    public Class<AllocatingSprinklerVO> getResultType() {
        return AllocatingSprinklerVO.class;
    }

    @Override
    public Class<AllocatingSprinklerExcelVO> getExcelResultType() {
        return AllocatingSprinklerExcelVO.class;
    }
}

package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.DamagedSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.DamagedSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.DamagedSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DamagedSprinklerStrategy implements RepositorySprinklerQueryStrategy<DamagedSprinklerQueryForm, DamagedSprinklerVO> {

    @Autowired
    private DamagedSprinklerRepository damagedSprinklerRepository;


    @Override
    public List<DamagedSprinklerVO> executeQuery(Page<?> page, SprinklerQueryForm queryForm, DamagedSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return damagedSprinklerRepository.getListByQueryPage(page, queryForm, joinForm);
    }

    @Override
    public Class<DamagedSprinklerVO> getResultType() {
        return DamagedSprinklerVO.class;
    }
}

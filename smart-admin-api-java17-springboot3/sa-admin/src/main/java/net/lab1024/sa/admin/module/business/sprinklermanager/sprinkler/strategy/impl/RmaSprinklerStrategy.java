package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.RmaSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.RmaSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.RmaSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RmaSprinklerStrategy implements RepositorySprinklerQueryStrategy<RmaSprinklerQueryForm, RmaSprinklerVO> {

    @Autowired
    private RmaSprinklerRepository rmaSprinklerRepository;


    @Override
    public List<RmaSprinklerVO> executeQuery(Page<?> page, SprinklerQueryForm queryForm, RmaSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return rmaSprinklerRepository.getListByQueryPage(page, queryForm, joinForm);
    }

    @Override
    public Class<RmaSprinklerVO> getResultType() {
        return RmaSprinklerVO.class;
    }
}

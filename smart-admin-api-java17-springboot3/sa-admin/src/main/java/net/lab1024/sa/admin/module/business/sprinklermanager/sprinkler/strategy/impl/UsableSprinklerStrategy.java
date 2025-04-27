package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsableSprinklerStrategy implements RepositorySprinklerQueryStrategy<UsableSprinklerQueryForm, UsableSprinklerVO> {

    @Autowired
    private UsableSprinklerRepository usableSprinklerRepository;

    @Autowired
    private SprinklerRepository sprinklerRepository;

    @Override
    public List<UsableSprinklerVO> executeQuery(Page<?> page, SprinklerQueryForm queryForm, UsableSprinklerQueryForm joinForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        return usableSprinklerRepository.getListByQueryPage(page, queryForm, joinForm);
    }

    @Override
    public Class<UsableSprinklerVO> getResultType() {
        return UsableSprinklerVO.class;
    }
}

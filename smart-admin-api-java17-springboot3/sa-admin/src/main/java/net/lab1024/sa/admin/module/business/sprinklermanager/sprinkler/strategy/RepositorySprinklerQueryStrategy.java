package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;

public interface RepositorySprinklerQueryStrategy<T extends BaseQueryForm, R> {
    ResponseDTO<PageResult<?>> executeQuery(Page<?> page, SprinklerQueryForm queryForm, T joinForm);
}

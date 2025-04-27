package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;

import java.util.List;

public interface RepositorySprinklerQueryStrategy<T extends BaseQueryForm, R> {
    List<R> executeQuery(Page<?> page, SprinklerQueryForm queryForm, T joinForm);
    Class<R> getResultType();
}

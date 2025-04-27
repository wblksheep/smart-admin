package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;

public interface RepositorySprinklerQueryStrategy<T, R>{
    ResponseDTO<PageResult<R>> executeQuery(Page<T> page, SprinklerQueryForm queryForm, BaseQueryForm joinQueryForm);
}

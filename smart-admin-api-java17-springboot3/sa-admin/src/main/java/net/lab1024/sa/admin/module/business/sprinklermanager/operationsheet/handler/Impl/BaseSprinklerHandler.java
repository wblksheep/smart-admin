package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.SprinklerForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.SprinklerOperationHandler;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public abstract class BaseSprinklerHandler<T extends SprinklerForm> implements SprinklerOperationHandler {

    @Resource
    private SprinklerDao sprinklerDao;

    @Transactional(rollbackFor = Exception.class)
    public void handle(T form){
        validateSprinkler(form);//校验的逻辑是一致的，看存不存在喷头
        // 模板方法
        validateOperation(form);
        executeOperation(form);
        updateStatus(form);
        addTrace(form);
    }

    private ResponseDTO<String> validateSprinkler(T form) {
        Long sprinklerId = form.getSprinklerId();
        SprinklerEntity sprinklerDetail = sprinklerDao.selectById(sprinklerId);
        if (Objects.isNull(sprinklerDetail) || sprinklerDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("喷头不存在");
        }
        return ResponseDTO.ok();
    }

    protected abstract void validateOperation(T form);
    protected abstract void executeOperation(T form);
    protected abstract void updateStatus(T form);
    protected abstract void addTrace(T form);


}

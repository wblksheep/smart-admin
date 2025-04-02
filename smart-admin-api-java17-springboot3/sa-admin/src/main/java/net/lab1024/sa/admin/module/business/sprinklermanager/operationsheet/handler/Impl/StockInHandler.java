package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.StockInForm;
import org.springframework.stereotype.Component;

@Component
public class StockInHandler extends BaseSprinklerHandler<StockInForm>{
    @Override
    protected void validateOperation(StockInForm form) {

    }

    @Override
    protected void executeOperation(StockInForm form) {

    }

    @Override
    protected void updateStatus(StockInForm form) {

    }

    @Override
    protected void addTrace(StockInForm form) {

    }

    @Override
    public void handle() {

    }

    @Override
    public String getOperationType() {
        return "stock_in";
    }
}

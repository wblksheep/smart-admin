package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;

public interface BaseIService<T> extends IService<T> {
    BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag);

    T getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag);

    void myUpdateById(Object updateEntity);
}

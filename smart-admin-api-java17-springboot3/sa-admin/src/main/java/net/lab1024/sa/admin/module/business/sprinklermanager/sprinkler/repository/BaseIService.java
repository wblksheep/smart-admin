package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Length;

public interface BaseIService<T> extends IService<T> {
    BaseSprinklerVO getDetail(SprinklerEntity sprinklerEntity, Boolean deletedFlag);

    T getBySprinklerSerial(String sprinklerSerial, Long sprinklerId, Boolean deletedFlag);

    void myUpdateById(Object updateEntity);
}

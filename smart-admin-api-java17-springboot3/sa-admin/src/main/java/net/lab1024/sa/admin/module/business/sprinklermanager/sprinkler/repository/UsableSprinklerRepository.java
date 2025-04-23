package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import org.hibernate.validator.constraints.Length;

public interface UsableSprinklerRepository extends IService<UsableSprinklerEntity> {
    boolean existsBySprinklerSerial(String sprinklerSerial);
}

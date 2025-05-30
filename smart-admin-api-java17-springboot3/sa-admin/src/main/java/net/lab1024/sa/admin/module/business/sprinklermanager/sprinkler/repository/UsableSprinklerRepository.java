package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.UsableSprinklerVO;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public interface UsableSprinklerRepository extends BaseIService<UsableSprinklerEntity> {
    List<UsableSprinklerVO> getListByQueryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") UsableSprinklerQueryForm joinForm);
    List<UsableSprinklerExcelVO> getListByQueryPage(@Param("queryForm") SprinklerQueryForm queryForm, @Param("joinForm") UsableSprinklerQueryForm joinForm);
    List<UsableSprinklerEntity> getListBySprinklerSerials(List<String> sprinklerSerials);
}

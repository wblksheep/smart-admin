package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public interface SprinklerRepository extends IService<SprinklerEntity> {
    List<SprinklerEntity> getListBySprinklerSerials(List<String> sprinklerSerial);

    List<SprinklerVO> getListByQueryPage(Page<?> page, @Param("queryForm") SprinklerQueryForm queryForm);

    List<SprinklerExcelVO> selectSprinklerExcelExportData(@Param("queryForm") SprinklerQueryForm queryForm);

    SprinklerEntity queryBySprinklerSerial(@Param("sprinklerSerial") String sprinklerSerial,@Param("excludeSprinklerId") Long sprinklerId, Boolean deletedFlag);
}

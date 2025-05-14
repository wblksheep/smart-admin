package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.entity.StatisticEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.statistic.domain.vo.MonthlyDamagedSprinklerUsingDaysVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Component
public interface StatisticDao extends BaseMapper<StatisticEntity> {
    List<MonthlyDamagedSprinklerUsingDaysVO> queryByRetDamagedAndDate(LocalDate startDate, LocalDate endDate);
}

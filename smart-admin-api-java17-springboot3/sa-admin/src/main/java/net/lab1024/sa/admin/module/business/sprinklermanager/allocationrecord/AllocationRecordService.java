package net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.entity.AllocationRecordEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.form.AllocationRecordQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.domain.vo.AllocationRecordVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.allocationrecord.repository.AllocationRecordRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AllocationRecordService {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private AllocationRecordRepository allocationRecordRepository;

    /**
     * 新建领用记录
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createAllocationRecord(AllocationRecordCreateForm createVO) {
        // 验证喷头是否存在
        List<SprinklerEntity> validateSprinkler = sprinklerRepository.getListBySprinklerSerials(Arrays.asList(createVO.getSprinklerSerial()));
        if (validateSprinkler.isEmpty()) {
            return ResponseDTO.userErrorParam("无效的喷头");
        }
        AllocationRecordEntity insertAllocationRecord = SmartBeanUtil.copy(createVO, AllocationRecordEntity.class);
        insertAllocationRecord.setSprinklerId(validateSprinkler.get(0).getSprinklerId());
        allocationRecordRepository.save(insertAllocationRecord);
        return ResponseDTO.ok();
    }

    /**
     * 分页查询领用记录模块
     */
    public ResponseDTO<PageResult<AllocationRecordVO>> queryByPage(AllocationRecordQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<AllocationRecordVO> allocationRecordList = allocationRecordRepository.getListByQueryPage(page, queryForm);

        PageResult<AllocationRecordVO> pageResult = SmartPageUtil.convert2PageResult(page, allocationRecordList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询领用记录详情
     *
     */
    public AllocationRecordVO getDetail(Long recordId) {
        return allocationRecordRepository.getDetail(recordId, Boolean.FALSE);
    }
}

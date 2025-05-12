package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.DataProcessorFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.RepositorySprinklerCreateFormFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.RepositorySprinklerStrategyFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.SprinklerRepositoryFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SprinklerService {


    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private RepositorySprinklerStrategyFactory repositorySprinklerStrategyFactory;

    @Resource
    private SprinklerRepositoryFactory sprinklerRepositoryFactory;
    @Autowired
    private UsableSprinklerRepository usableSprinklerRepository;


    /**
     * 分页查询全部喷头模块
     */
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(SprinklerQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SprinklerVO> sprinklerList = sprinklerRepository.getListByQueryPage(page, queryForm);

        PageResult<SprinklerVO> pageResult = SmartPageUtil.convert2PageResult(page, sprinklerList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 分页查询各仓喷头模块
     */
    public <R, EXCEL> ResponseDTO<PageResult<R>> repositoryQueryByPage(@Valid CombinedQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        BaseQueryForm joinForm = queryForm.getJoinQueryForm();

        // 获取具体策略
        @SuppressWarnings("unchecked")
        RepositorySprinklerQueryStrategy<BaseQueryForm, R, EXCEL> strategy =
                (RepositorySprinklerQueryStrategy<BaseQueryForm, R, EXCEL>)
                        repositorySprinklerStrategyFactory.getStrategy(joinForm.getClass());

        // 执行策略查询
        List<R> resultList = strategy.executeQuery(
                page,
                queryForm.getQueryForm(),
                joinForm
        );

        // 带类型转换的分页结果构建
        PageResult<R> pageResult = SmartPageUtil.convert2PageResult(
                page,
                resultList,
                strategy.getResultType()  // 使用策略提供的类型信息
        );

        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询各仓喷头详情
     *
     */
    public BaseSprinklerVO getDetail(Long sprinklerId) {
        // 1. 获取喷头实体
        SprinklerEntity sprinklerEntity = sprinklerRepository.getById(sprinklerId);
        // 2. 根据状态值获取枚举类型（核心优化点）
        RepositorySprinklerTypeEnum type = RepositorySprinklerTypeEnum.fromStatus(sprinklerEntity.getStatus())
                .orElseThrow(() -> new IllegalArgumentException("无效的状态值：" + sprinklerEntity.getStatus()));
        // 3. 类型安全获取仓库实现类
        BaseServiceImpl<?, ?> repository = sprinklerRepositoryFactory.getRepository(type);

        // 4. 执行详情查询
        return repository.getDetail(sprinklerEntity, Boolean.FALSE);
    }

    /**
     * 获取导出数据
     */
    public List<SprinklerExcelVO> getSprinklerExcelExportData(@Valid SprinklerQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        return sprinklerRepository.selectSprinklerExcelExportData(queryForm);
    }


    @Resource
    private RepositorySprinklerCreateFormFactory createFormFactory;

    @Resource
    private DataProcessorFactory processorFactory;

    /**
     * 导入各仓喷头模块
     *
     */
    public ResponseDTO<String> batchRepositorySprinklerCreate(@Valid MultipartFile file, RequestUser requestUser, @Valid Integer type) {
        Class<? extends BaseCreateForm> createVOClazz = createFormFactory.getSprinklerClass(type);
        List<? extends BaseCreateForm> list = ExcelUtil
                .importExcelByClass(file, createVOClazz)
                .stream()
                .peek(vo -> initCreateVO(vo, requestUser))
                .toList();
        DataProcessor dataProcessor = processorFactory
                .getProcessor(RepositorySprinklerTypeEnum.values()[type].getDesc());
        dataProcessor.process(list);
        return ResponseDTO.ok();
    }

    /**
     * 导入全部喷头模块
     *
     */
    public ResponseDTO<String> batchSprinklerCreate(@Valid MultipartFile file, RequestUser requestUser) {
        //使用收集器一次性完成字段设置，避免冗余操作
        List<SprinklerCreateForm> createVOs = ExcelUtil.importExcelByClass(file, SprinklerCreateForm.class)
                .stream()
                .peek(vo -> initCreateVO(vo, requestUser)) // 提取字段设置为独立方法
                .toList();

        //提前返回空值情况
        if (createVOs.isEmpty()) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 校验1：收集无效数据（空值或空字符串）
        Set<String> invalidSerials = createVOs.stream()
                .filter(vo -> StringUtils.isBlank(vo.getSprinklerSerial()))
                .map(SprinklerCreateForm::getSprinklerSerial) // 实际会得到null或空字符串
                .collect(Collectors.toSet());

        //使用提取方法优化可读性
        // 校验2：收集已存在数据
        Set<String> existingSerials = getExistingSprinklerSerials(createVOs);

        //合并校验结果
        Map<Boolean, List<SprinklerCreateForm>> partitionedData = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        vo -> StringUtils.isNotBlank(vo.getSprinklerSerial())
                                && !existingSerials.contains(vo.getSprinklerSerial())
                ));

        List<SprinklerEntity> validData = partitionedData.get(true).stream()
                .map(this::convertToEntity)
                .toList();

        // 错误数据合并（空值+重复值）
        Set<String> errorData = new HashSet<>();
        errorData.addAll(invalidSerials);
        errorData.addAll(partitionedData.get(false).stream()
                .map(SprinklerCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet()));

        // 重复数据去重
        validData = validData.stream()
                .filter(distinctByKey(SprinklerEntity::getSprinklerSerial))
                .collect(Collectors.toList());

        // 执行插入并返回详细信息
        if (!validData.isEmpty()) {
            sprinklerRepository.saveBatch(validData);
            return buildResponse(validData.size(), errorData);
        }
        return ResponseDTO.userErrorParam("无有效数据可插入，错误数据：" + String.join(",", errorData));

    }



    // 辅助方法
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private ResponseDTO<String> buildResponse(int successCount, Set<String> errorData) {
        String msg = String.format(
                "成功插入%d条，错误数据（空值/重复）:%s",
                successCount,
                errorData.isEmpty() ? "无" : String.join(",", errorData)
        );
        return ResponseDTO.okMsg(msg);
    }

    // 辅助方法：对象转换
    private SprinklerEntity convertToEntity(SprinklerCreateForm form) {
        return SmartBeanUtil.copy(form, SprinklerEntity.class);
    }

    // 辅助方法：获取已存在序列号
    private Set<String> getExistingSprinklerSerials(List<SprinklerCreateForm> createVOs) {
        List<String> serials = createVOs.stream()
                .map(SprinklerCreateForm::getSprinklerSerial)
                .toList();

        return sprinklerRepository.getListBySprinklerSerials(serials)
                .stream()
                .map(SprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // 保持查询顺序
    }

    // 辅助方法：初始化创建对象
    private <T extends BaseCreateForm> void initCreateVO(T vo, RequestUser user) {
        vo.setDisabledFlag(Boolean.FALSE);
        vo.setCreateUserId(user.getUserId());
        vo.setCreateUserName(user.getUserName());
    }


    public List<?> getRepositorySprinklerExcelExportData(@Valid CombinedQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        BaseQueryForm joinForm = queryForm.getJoinQueryForm();

        // 获取具体策略
        @SuppressWarnings("unchecked")
        RepositorySprinklerQueryStrategy<BaseQueryForm, ?, ?> strategy =
                (RepositorySprinklerQueryStrategy<BaseQueryForm, ?, ?>)
                        repositorySprinklerStrategyFactory.getStrategy(joinForm.getClass());

        // 执行策略查询
        List<?> resultList = strategy.executeExport(
                queryForm.getQueryForm(),
                joinForm
        );

        return resultList;
    }


}

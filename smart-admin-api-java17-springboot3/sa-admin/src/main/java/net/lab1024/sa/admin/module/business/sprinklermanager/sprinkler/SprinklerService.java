package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeChineseEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.BaseSprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.BaseIService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.abstractimpl.BaseServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.service.TypeService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerQueryStrategy;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.strategy.RepositorySprinklerTransferStrategy;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 喷头管理-喷头服务
 *
 * @Author 海印：芦苇
 */
@Service
@Slf4j
public class SprinklerService {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private RepositorySprinklerStrategyFactory repositorySprinklerStrategyFactory;

    @Resource
    private SprinklerRepositoryFactory sprinklerRepositoryFactory;

    @Resource
    private RepositorySprinklerTransferStrategyFactory repositorySprinklerTransferStrategyFactory;
    @Autowired
    private UsableSprinklerRepository usableSprinklerRepository;
    @Autowired
    private EnterpriseDao enterpriseDao;


    /**
     * 分页查询全部喷头模块
     */
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(SprinklerQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQueryBySprinklerSerial(queryForm);
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
     */
    public BaseSprinklerVO getDetail(Long sprinklerId) {
        // 1. 获取喷头实体
        SprinklerEntity sprinklerEntity = sprinklerRepository.getById(sprinklerId);
        // 2. 根据状态值获取枚举类型（核心优化点）
        RepositorySprinklerTypeEnum type = RepositorySprinklerTypeEnum.fromStatus(sprinklerEntity.getStatus().byteValue())
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
    private RepositorySprinklerImportFormFactory importFormFactory;

    @Resource
    private DataProcessorFactory processorFactory;

    /**
     * 导入各仓喷头模块
     */
    public ResponseDTO<String> batchRepositorySprinklerImport(@Valid MultipartFile file, RequestUser requestUser, @Valid Integer type) {
        Class<? extends BaseImportForm> importVOClazz = importFormFactory.getSprinklerClass(type);
        List<? extends BaseImportForm> list = ExcelUtil
                .importExcelByClass(file, importVOClazz)
                .stream()
                .peek(vo -> initImportVO(vo, requestUser))
                .toList();
        DataProcessor dataProcessor = processorFactory
                .getProcessor(RepositorySprinklerTypeEnum.values()[type].getDesc());
        dataProcessor.process(list);
        return ResponseDTO.ok();
    }

    /**
     * 导入全部喷头模块
     */
    public ResponseDTO<String> batchSprinklerImport(@Valid MultipartFile file, RequestUser requestUser) {
        //使用收集器一次性完成字段设置，避免冗余操作
        List<SprinklerImportForm> importVOs = ExcelUtil.importExcelByClass(file, SprinklerImportForm.class)
                .stream()
                .peek(vo -> initImportVO(vo, requestUser)) // 提取字段设置为独立方法
                .toList();

        //提前返回空值情况
        if (importVOs.isEmpty()) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 校验1：收集无效数据（空值或空字符串）
        Set<String> invalidSerials = importVOs.stream()
                .filter(vo -> StringUtils.isBlank(vo.getSprinklerSerial()))
                .map(SprinklerImportForm::getSprinklerSerial) // 实际会得到null或空字符串
                .collect(Collectors.toSet());

        //使用提取方法优化可读性
        // 校验2：收集已存在数据
        Set<String> existingSerials = getExistingSprinklerSerials(importVOs);

        //合并校验结果
        Map<Boolean, List<SprinklerImportForm>> partitionedData = importVOs.stream()
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
                .map(SprinklerImportForm::getSprinklerSerial)
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
        return ResponseDTO.userErrorParam("无有效数据可插入，错误数据：全部为空值或重复序列号");

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

    /**
     * 编辑全部喷头
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateSprinkler(@Valid SprinklerUpdateForm updateVO) {
        List<SprinklerEntity> sprinklerDetails = sprinklerRepository.getListBySprinklerSerials(Arrays.asList(updateVO.getSprinklerSerial()));
        if (sprinklerDetails.isEmpty()) {
            return ResponseDTO.userErrorParam("喷头不存在");
        }
        // 校验喷头是否存在
        SprinklerEntity sprinklerDetail = sprinklerDetails.get(0);
        if (Objects.isNull(sprinklerDetail) || sprinklerDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("喷头不存在");
        }
        Long sprinklerId = sprinklerDetail.getSprinklerId();
        // 验证喷头序列号是否重复
        SprinklerEntity validateSprinkler = sprinklerRepository.queryBySprinklerSerial(updateVO.getSprinklerSerial(), sprinklerId, Boolean.FALSE);
        if (Objects.nonNull(validateSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        SprinklerEntity updateEntity = SmartBeanUtil.copy(sprinklerDetail, SprinklerEntity.class);
        SmartBeanUtil.copyProperties(updateVO, updateEntity);
        updateEntity.setSprinklerId(sprinklerId);
        Byte oldStatus = sprinklerDetail.getStatus().byteValue();
        Byte newStatus = updateEntity.getStatus().byteValue();
        if (oldStatus == newStatus) {
            // 更新对应仓喷头
            SprinklerEntity updateEntity2 = SmartBeanUtil.copy(sprinklerDetail, SprinklerEntity.class);
            SmartBeanUtil.copyProperties(updateVO, updateEntity2);
            sprinklerRepository.updateById(updateEntity2);
            return ResponseDTO.ok();
        }
        RepositorySprinklerTransferStrategy curStrategy = repositorySprinklerTransferStrategyFactory.getStrategy(oldStatus);
        curStrategy.updateDeletedFlag(sprinklerDetail);

        RepositorySprinklerTransferStrategy nextStrategy = repositorySprinklerTransferStrategyFactory.getStrategy(newStatus);
        nextStrategy.updateRepository(sprinklerDetail);
//        RequestUser requestUser = SmartRequestUtil.getRequestUser();
//        updateEntity.setHistory(updateEntity.getHistory() + LocalDate.now() + requestUser.getUserName() + "将喷头从" + RepositorySprinklerTypeChineseEnum.fromStatus(oldStatus).get().getDesc() + "转入" + RepositorySprinklerTypeChineseEnum.fromStatus(newStatus).get().getDesc() + ";");
        sprinklerRepository.updateById(updateEntity);
        return ResponseDTO.ok();
    }


    @Resource
    private TypeService typeService;

    /**
     * 编辑各仓喷头
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRepositorySprinkler(@Valid BaseUpdateForm updateVO, Byte type) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Long sprinklerId = updateVO.getSprinklerId();
        // 校验喷头是否存在
        SprinklerEntity sprinklerDetail = sprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(sprinklerDetail) || sprinklerDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("喷头不存在");
        }
        // 验证喷头序列号是否重复
        SprinklerEntity validSprinkler = sprinklerRepository.queryBySprinklerSerial(updateVO.getSprinklerSerial(), sprinklerId, Boolean.FALSE);
        if (Objects.nonNull(validSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        if (updateVO.getHistory() != null) { // 如果历史不为空，则更新
            sprinklerDetail.setHistory(updateVO.getHistory());
            sprinklerRepository.updateById(sprinklerDetail);
        }

        Class<?> clazz = typeService.getCachedEntity(type);
        BaseIService<?> repository = typeService.getCachedRepository(type);

        // 校验各仓喷头是否存在
        Object repoSprinklerDetail = repository.getById(sprinklerId);
        if (Objects.isNull(repoSprinklerDetail)) {
            return ResponseDTO.userErrorParam("该仓喷头不存在");
        }
        // 动态实例化并拷贝
        Object entity = clazz.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(repoSprinklerDetail, entity);
        // 获取字段值（不依赖getter方法）
        Field deletedFlagField = clazz.getDeclaredField("deletedFlag");
        deletedFlagField.setAccessible(true);
        Boolean deleted = (Boolean) deletedFlagField.get(repoSprinklerDetail);
        if (Boolean.TRUE.equals(deleted)) {
            return ResponseDTO.userErrorParam("该喷头已被删除");
        }

        // 验证各仓喷头序列号是否重复
        Object detailObj = repository.getBySprinklerSerial(updateVO.getSprinklerSerial(), sprinklerId, Boolean.FALSE);

        if (Objects.nonNull(detailObj)) {
            return ResponseDTO.userErrorParam("所在仓喷头序列号重复");
        }

        // 更新对应仓喷头
        Object updateEntity = SmartBeanUtil.copy(repoSprinklerDetail, clazz);
        SmartBeanUtil.copyProperties(updateVO, updateEntity);
        repository.myUpdateById(updateEntity);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateBatchSprinkler(@Valid MultipartFile file, RequestUser requestUser) {
        //使用收集器一次性完成字段设置，避免冗余操作
        List<SprinklerImportForm> importVOs = ExcelUtil.importExcelByClass(file, SprinklerImportForm.class)
                .stream()
                .peek(vo -> initImportVO(vo, requestUser)) // 提取字段设置为独立方法
                .toList();
        for (int i = 0; i < importVOs.size(); i++) {
            SprinklerImportForm importVO = importVOs.get(i);
            SprinklerUpdateForm updateVO = new SprinklerUpdateForm();
            BeanUtils.copyProperties(importVO, updateVO);
            RepositorySprinklerTypeChineseEnum[] values = RepositorySprinklerTypeChineseEnum.values();
            for (int j = 0; j < values.length; j++) {
                if (values[j].getDesc().equals(importVO.getStatus())) {
                    updateVO.setStatus(values[j].getValue());
                }
            }
            if (updateVO.getStatus() == null) {
                return ResponseDTO.userErrorParam("编辑数据所在仓数据有误");
            }
            updateSprinkler(updateVO);
        }
        return ResponseDTO.ok();
    }

    /**
     * 批量编辑全部喷头
     */
    public <T extends BaseUpdateForm> ResponseDTO<String> updateBatchSprinkler(@Valid MultipartFile file, Byte type, RequestUser requestUser) {
        Class<T> baseUpdateFormClass = (Class<T>) typeService.getCachedUpdateForm(type);
        //使用收集器一次性完成字段设置，避免冗余操作
        List<T> updateVOs = ExcelUtil.importExcelByClass(file, baseUpdateFormClass)
                .stream()
                .peek(vo -> initUpdateVO(vo, requestUser)) // 提取字段设置为独立方法
                .toList();
        updateVOs.forEach(vo -> {
            try {
                updateRepositorySprinkler(vo, type);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseDTO.ok();
    }


    /**
     * 喷头转仓
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> transferRepo(Long sprinklerId, Byte type) {
        SprinklerEntity sprinklerDetail = sprinklerRepository.getById(sprinklerId);
        if (Objects.isNull(sprinklerDetail) || sprinklerDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("喷头不存在");
        }
        if (sprinklerDetail.getStatus().byteValue() == type) {
            return ResponseDTO.userErrorParam("喷头已经在该仓了");
        }
        RepositorySprinklerTransferStrategy curStrategy = repositorySprinklerTransferStrategyFactory.getStrategy(sprinklerDetail.getStatus());
        curStrategy.updateDeletedFlag(sprinklerDetail);

        RepositorySprinklerTransferStrategy nextStrategy = repositorySprinklerTransferStrategyFactory.getStrategy(type);
        nextStrategy.updateRepository(sprinklerDetail);
        return ResponseDTO.ok();
    }

    /**
     * 新建全部喷头
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createSprinkler(SprinklerCreateForm createVO) {
        // 验证喷头序列号是否重复
        SprinklerEntity validSprinkler = sprinklerRepository.queryBySprinklerSerial(createVO.getSprinklerSerial(), null, Boolean.FALSE);
        if (Objects.nonNull(validSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        // 数据插入
        SprinklerEntity insertSprinkler = SmartBeanUtil.copy(createVO, SprinklerEntity.class);
        insertSprinkler.setStatus((byte) 0);
        sprinklerRepository.save(insertSprinkler);

        UsableSprinklerEntity usableSprinkler = new UsableSprinklerEntity();
        usableSprinkler.setSprinklerId(insertSprinkler.getSprinklerId());
        usableSprinkler.setSprinklerSerial(insertSprinkler.getSprinklerSerial());
        usableSprinkler.setCreateUserId(insertSprinkler.getCreateUserId());
        usableSprinkler.setCreateUserName(insertSprinkler.getCreateUserName());
        usableSprinklerRepository.save(usableSprinkler);
        return ResponseDTO.ok();
    }

    private <T extends BaseImportForm> void initImportVO(T vo, RequestUser requestUser) {
        vo.setDisabledFlag(Boolean.FALSE);
        vo.setCreateUserId(requestUser.getUserId());
        vo.setCreateUserName(requestUser.getUserName());
    }

    // 辅助方法w
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private ResponseDTO<String> buildResponse(int successCount, Set<String> errorData) {
        String msg = String.format(
                "成功插入%d条，错误数据（空值/重复）:%s",
                successCount,
                errorData.isEmpty() ? "无" : "存在空值或重复数据"
        );
        return ResponseDTO.okMsg(msg);
    }

    // 辅助方法：对象转换
    private SprinklerEntity convertToEntity(SprinklerImportForm form) {
        return SmartBeanUtil.copy(form, SprinklerEntity.class);
    }

    // 辅助方法：获取已存在序列号
    private Set<String> getExistingSprinklerSerials(List<SprinklerImportForm> createVOs) {
        List<String> serials = createVOs.stream()
                .map(SprinklerImportForm::getSprinklerSerial)
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

    // 辅助方法：初始化创建对象
    private <T extends BaseUpdateForm> void initUpdateVO(T vo, RequestUser user) {
        vo.setDisabledFlag(Boolean.FALSE);
        vo.setCreateUserId(user.getUserId());
        vo.setCreateUserName(user.getUserName());
    }


}

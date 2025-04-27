package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.builder.JoinConditionBuilder;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerStockInDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerStockInEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.*;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.DataProcessorFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.RepositorySprinklerCreateFormFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SprinklerService {

    @Resource
    private SprinklerStockInDao sprinklerStockInDao;


    @Resource
    private DataTracerService dataTracerService;

    @Resource
    private SprinklerRepository sprinklerRepository;



    /**
     * 分页查询全部喷头模块
     *
     */
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(SprinklerQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SprinklerVO> sprinklerList = sprinklerRepository.getListByQueryPage(page, queryForm);

//        List<SprinklerVO> sprinklerList = sprinklerStockInDao.queryPage(page, queryForm);
        PageResult<SprinklerVO> pageResult = SmartPageUtil.convert2PageResult(page, sprinklerList);
        return ResponseDTO.ok(pageResult);
//        return ResponseDTO.ok();
    }

    /**
     * 分页查询喷头信息（动态联表版）
     * ⚠️优化点1：引入泛型支持多类型联表查询
     * ⚠️优化点2：采用注解驱动式条件构建
     * @param queryForm 联合查询参数
//     * @param mainQuery 主表查询参数
//     * @param joinQuery 联表查询参数（支持任意实现了JoinConditionBuilder的表单）
//     * @param pageable 分页参数
     * @return 分页结果（自动类型转换）
     */
    public <T extends JoinConditionBuilder> ResponseDTO<PageResult<SprinklerVO>> repositoryQueryByPage(
            @Valid CombinedQueryForm queryForm) {

        SprinklerQueryForm mainQuery = queryForm.getQueryForm();
        T joinQueryForm = (T) queryForm.getJoinQueryForm();

        MPJLambdaWrapper<SprinklerEntity> wrapper = new MPJLambdaWrapper<>();

        // 1. 安全字段映射（防止SQL注入）
        configureFieldMapping(wrapper);

        // 2. 主表动态条件（类型安全构建）
        buildMainConditions(wrapper, mainQuery);

        // 调用示例
//        String generatedSql = getActualSqlSafely(wrapper);
//        System.out.println("Generated SQL: " + generatedSql);
        // 3. 动态联表条件（策略模式替代反射）
        if (joinQueryForm != null) {
            joinQueryForm.buildJoinConditions(wrapper);
        }
//
//        // 4. 执行分页（自动Count优化）
//        IPage<SprinklerVO> resultPage = sprinklerMapper.selectJoinPage(
//                PageConvert.toMPPage(pageable),
//                SprinklerVO.class,
//                wrapper
//        );
//
//        return new PageResult<>(resultPage.getRecords(), resultPage.getTotal());
        String generatedSql = getActualSqlSafely(wrapper);
        System.out.println("Generated SQL: " + generatedSql);
        return ResponseDTO.ok();
    }

    public static String getActualSqlSafely(MPJLambdaWrapper<?> wrapper) {
        try {
            if (wrapper == null) {
                return "[ERROR] Wrapper cannot be null";
            }

            // 获取SQL片段（处理空值）
            String sqlSelect = Optional.ofNullable(wrapper.getSqlSelect()).orElse("*");
            String sqlSegment = Optional.ofNullable(wrapper.getSqlSegment()).orElse("");

            // 构建基础SQL（注意：需根据实际表名替换your_table）
            StringBuilder actualSql = new StringBuilder();
            actualSql.append(sqlSelect)
                    .append(" FROM your_table ") // 表名应从实体类获取（需改进点）
                    .append(sqlSegment);

            // 处理参数替换
            Map<String, Object> params = wrapper.getParamNameValuePairs();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String placeholder = "#{ew.paramNameValuePairs." + entry.getKey() + "}";
                    String replacement = formatSqlValue(entry.getValue());
                    int index;
                    while ((index = actualSql.indexOf(placeholder)) != -1) {
                        actualSql.replace(index, index + placeholder.length(), replacement);
                    }
                }
            }

            return actualSql.toString();
        } catch (Exception e) {
            // 记录异常日志
            return "[ERROR] Failed to generate SQL: " + e.getMessage();
        }
    }

    /**
     * 安全处理SQL参数值（防止SQL注入式替换）
     */
    private static String formatSqlValue(Object value) {
        if (value == null) {
            return "NULL";
        }

        // 处理特殊字符转义（示例处理，根据数据库类型调整）
        if (value instanceof String) {
            return "'" + ((String) value).replace("'", "''") + "'"; // 转义单引号
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        }
        return "'" + value.toString().replace("'", "''") + "'"; // 默认处理
    }

    /**
     * 配置字段映射关系（集中管理映射逻辑）
     * ⚠️优化点3：使用显式字段声明替代selectAll
     */
    private void configureFieldMapping(MPJLambdaWrapper<SprinklerEntity> wrapper) {
        wrapper.select(SprinklerEntity::getSprinklerId, SprinklerEntity::getSprinklerSerial)
//                .selectAs(Warehouse::getWarehouseName, SprinklerVO::getWarehouseName)
//                .selectAs(Warehouse::getLocation, SprinklerVO::getWarehouseLocation)
                .leftJoin(UsableSprinklerEntity.class, UsableSprinklerEntity::getSprinklerId, UsableSprinklerEntity::getSprinklerSerial);
    }

    /**
     * 主表条件构建（基于JSR303校验结果）
     * ⚠️优化点4：集成参数校验结果
     */
    private void buildMainConditions(MPJLambdaWrapper<SprinklerEntity> wrapper,
                                     @Valid SprinklerQueryForm queryForm) {
        wrapper.selectAll(SprinklerEntity.class)
                .eq(SprinklerEntity::getDeletedFlag, queryForm.getDeletedFlag())
                // INSTR条件处理
                .apply(queryForm.getPurchaseDateContractNumber() != null,
                        "INSTR(purchase_date_contract_number, {0}) > 0", queryForm.getPurchaseDateContractNumber())
                .apply(queryForm.getSprinklerModel() != null,
                        "INSTR(sprinkler_model, {0}) > 0", queryForm.getSprinklerModel())
                .apply(queryForm.getSprinklerSerial() != null,
                        "INSTR(sprinkler_serial, {0}) > 0", queryForm.getSprinklerSerial())
                // 日期范围条件
                .ge(queryForm.getShippingDateStartTime() != null,
                        SprinklerEntity::getShippingDate, queryForm.getShippingDateStartTime())
                .le(queryForm.getShippingDateEndTime() != null,
                        SprinklerEntity::getShippingDate, queryForm.getShippingDateEndTime())
                .ge(queryForm.getWarehouseDateStartTime() != null,
                        SprinklerEntity::getWarehouseDate, queryForm.getWarehouseDateStartTime())
                .le(queryForm.getWarehouseDateEndTime() != null,
                        SprinklerEntity::getWarehouseDate, queryForm.getWarehouseDateEndTime())
                .ge(queryForm.getAllocateDateStartTime() != null,
                        SprinklerEntity::getAllocateDate, queryForm.getAllocateDateStartTime())
                .le(queryForm.getAllocateDateEndTime() != null,
                        SprinklerEntity::getAllocateDate, queryForm.getAllocateDateEndTime())
                // 其他INSTR条件
                .apply(queryForm.getAllocateUser() != null,
                        "INSTR(allocate_user, {0}) > 0", queryForm.getAllocateUser())
                .apply(queryForm.getAllocatePurpose() != null,
                        "INSTR(allocate_purpose, {0}) > 0", queryForm.getAllocatePurpose())
                .apply(queryForm.getAllocatePosition() != null,
                        "INSTR(allocate_position, {0}) > 0", queryForm.getAllocatePosition())
                .apply(queryForm.getHistory() != null,
                        "INSTR(history, {0}) > 0", queryForm.getHistory())
                // 等值条件
                .eq(queryForm.getStatus() != null,
                        SprinklerEntity::getStatus, queryForm.getStatus())
                .eq(queryForm.getIsNew() != null,
                        SprinklerEntity::getIsNew, queryForm.getIsNew())
                .apply(queryForm.getSprinklerDetail() != null,
                        "INSTR(sprinkler_detail, {0}) > 0", queryForm.getSprinklerDetail())
                .eq(queryForm.getDisabledFlag() != null,
                        SprinklerEntity::getDisabledFlag, queryForm.getDisabledFlag())
                // 排序处理
                .orderByDesc(ObjectUtils.isEmpty(queryForm.getSortItemList()),
                        SprinklerEntity::getCreateTime);
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createSprinklerStockIn(SprinklerStockInCreateForm createVO) {
        //验证喷头序列号是否重复
        SprinklerStockInEntity validateSprinkler = sprinklerStockInDao.queryBySprinklerSerial(createVO.getSprinklerSerial(), null, Boolean.FALSE);
        if(Objects.nonNull(validateSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        //数据插入
        SprinklerStockInEntity insertSprinkler = SmartBeanUtil.copy(createVO, SprinklerStockInEntity.class);
        sprinklerStockInDao.insert(insertSprinkler);

        return ResponseDTO.ok();
    }

    /**
     * 查询喷头详情
     *
     */
    public SprinklerStockInVO getDetail(Long sprinklerId) {
        return sprinklerStockInDao.getDetail(sprinklerId, Boolean.FALSE);
    }


    /**
     * 获取导出数据
     */
    public List<SprinklerStockInExcelVO> getExcelExportData(@Valid SprinklerStockInQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        return sprinklerStockInDao.selectExcelExportData(queryForm);
    }

    @Resource
    private RepositorySprinklerCreateFormFactory createFormFactory;

    @Resource
    private DataProcessorFactory processorFactory;

    public ResponseDTO<String> batchRepositorySprinklerCreate(@Valid MultipartFile file, RequestUser requestUser, @Valid Integer type) {
        Class<? extends BaseCreateForm> createVOClazz = createFormFactory.getSprinklerClass(type);
        List<? extends BaseCreateForm> list = ExcelUtil.importExcelByClass(file, createVOClazz).stream().peek(vo->initCreateVO(vo, requestUser)).toList();
        DataProcessor dataProcessor = processorFactory.getProcessor(RepositorySprinklerTypeEnum.values()[type].getDesc());
        dataProcessor.process(list);
        return ResponseDTO.ok();
    }


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

        // 执行插入并返回详细信息
        if (!validData.isEmpty()) {
            sprinklerRepository.saveBatch(validData);
            return buildResponse(validData.size(), errorData);
        }
        return ResponseDTO.userErrorParam("无有效数据可插入，错误数据：" + String.join(",", errorData));

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


}

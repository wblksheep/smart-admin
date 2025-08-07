package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.service;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.*;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class ClassEntityTransferService {

    public SprinklerUpdateForm transferImportToUpdate(SprinklerImportForm form) {
        SprinklerUpdateForm updateVO = SmartBeanUtil.copy(form, SprinklerUpdateForm.class);

        // 预定义支持的日期格式
        final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy.M.dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.d"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        );

        // 通用日期解析方法
        Function<String, LocalDate> parseDate = (dateStr) -> {
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    // 尝试下一个格式
                }
            }
            throw new BusinessException("非法日期格式: " + dateStr);
        };

        // 处理日期字段（支持动态添加新字段）
        Map<String, Consumer<LocalDate>> dateSetters = Map.of(
                "allocateDate", updateVO::setAllocateDate,
                "shippingDate", updateVO::setShippingDate,
                "warehouseDate", updateVO::setWarehouseDate);

        dateSetters.forEach((fieldName, setter) -> {
            try {
                setter.accept(parseDate.apply(getDateField(form, fieldName)));
            } catch (BusinessException e) {
                throw new BusinessException(String.format("喷头序列号 %s 的%s失败: %s", form.getSprinklerSerial(), fieldName, e.getMessage()));
            }
        });

        // 处理布尔值转换
        Optional.ofNullable(form.getIsNew()).filter(Predicate.not(String::isEmpty)).ifPresent(val -> updateVO.setIsNew("新喷头".equals(val)));
        // 检查form是否为空（根据调用上下文确保form非空）
        if (form != null) {
            // 处理jetsout
            String jetsoutStr = form.getJetsoutNew();
            if (jetsoutStr != null && !jetsoutStr.trim().isEmpty()) {
                try {
                    updateVO.setJetsout(Byte.parseByte(jetsoutStr));
                } catch (NumberFormatException e) {

                }
                updateVO.setJetsoutNew(Float.parseFloat(form.getJetsoutNew()));
            }

            // 处理voltage
            String voltageStr = form.getVoltage();
            if (voltageStr != null && !voltageStr.trim().isEmpty()) {
                updateVO.setVoltage(Float.parseFloat(voltageStr));
            }
            String status = form.getStatus();
            if (status != null && !status.trim().isEmpty()) {// 无风险
                updateVO.setStatus(caseRepo(form.getStatus()));
            } else {
                throw new BusinessException("所在仓字段名有问题，请检查");
            }

        }
        return updateVO;
    }

    private Byte caseRepo(String status) {
        switch (status) {
            case "可用仓":
                return 0;
            case "机台":
                return 1;
            case "维修仓":
                return 2;
            case "破损仓":
                return 3;
            case "rma":
                return 4;
            default:
                throw new BusinessException("异常的状态值:" + status);
        }
    }

    private String getDateField(SprinklerImportForm form, String fieldName) {
        switch (fieldName) {
            case "allocateDate":
                return form.getAllocateDate();
            case "shippingDate":
                return form.getShippingDate();
            case "warehouseDate":
                return form.getWarehouseDate();
            default:
                throw new BusinessException("非法的参数：" + fieldName);
        }
    }

    private UsableSprinklerUpdateForm transferImportToUpdate(UsableSprinklerImportForm form) {
        // 使用Bean拷贝工具优化属性复制
        UsableSprinklerUpdateForm updateVO = SmartBeanUtil.copy(form, UsableSprinklerUpdateForm.class);
        // 预定义支持的日期格式
        final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy.M.dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.d"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        );
        // 通用日期解析方法
        Function<String, LocalDate> parseDate = (dateStr) -> {
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    // 尝试下一个格式
                }
            }
            throw new BusinessException("非法日期格式: " + dateStr);
        };
        // 处理日期字段（支持动态添加新字段）
        Map<String, Consumer<LocalDate>> dateSetters = Map.of(
                "retWarehouseDate", updateVO::setRetWarehouseDate);
        dateSetters.forEach((fieldName, setter) -> {
            try {
                setter.accept(parseDate.apply(getDateField(form, fieldName)));
            } catch (BusinessException e) {
                throw new BusinessException(String.format("喷头序列号 %s 的%s失败: %s", form.getSprinklerSerial(), fieldName, e.getMessage()));
            }
        });
        if (!form.getStatus().equals("可用仓")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        return updateVO;
    }

    private String getDateField(UsableSprinklerImportForm form, String fieldName) {
        switch (fieldName) {
            case "retWarehouseDate":
                return form.getRetWarehouseDate();
            default:
                throw new BusinessException("非法的参数：" + fieldName);
        }
    }

    public <T extends BaseImportForm> BaseUpdateForm transferImportToUpdate(T vo, Byte type) {
        switch (type) {
            case 0:
                return transferImportToUpdate((UsableSprinklerImportForm) vo);
            case 1:
                return transferImportToUpdate((MachineSprinklerImportForm) vo);
            case 2:
                return transferImportToUpdate((MaintainingSprinklerImportForm) vo);
            case 3:
                return transferImportToUpdate((DamagedSprinklerImportForm) vo);
            case 4:
                return transferImportToUpdate((RmaSprinklerImportForm) vo);
            case 5:
                return transferImportToUpdate((AllocatingSprinklerImportForm) vo);
            default:
                throw new BusinessException("未知的仓库类型" + type);
        }
    }

    private AllocatingSprinklerUpdateForm transferImportToUpdate(AllocatingSprinklerImportForm form) {
        // 使用Bean拷贝工具优化属性复制
        AllocatingSprinklerUpdateForm updateVO = SmartBeanUtil.copy(form, AllocatingSprinklerUpdateForm.class);
        if (!form.getStatus().equals("领用中")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        return updateVO;
    }

    private RmaSprinklerUpdateForm transferImportToUpdate(RmaSprinklerImportForm form) {
        // 使用Bean拷贝工具优化属性复制
        RmaSprinklerUpdateForm validSprinkler = SmartBeanUtil.copy(form, RmaSprinklerUpdateForm.class);
        // 预定义支持的日期格式
        final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy.M.dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.d"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        );
        // 通用日期解析方法
        Function<String, LocalDate> parseDate = (dateStr) -> {
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    // 尝试下一个格式
                }
            }
            throw new BusinessException("非法日期格式: " + dateStr);
        };
        // 处理日期字段（支持动态添加新字段）
        Map<String, Consumer<LocalDate>> dateSetters = Map.of(
                "retMaintainenceDate", validSprinkler::setRetMaintainenceDate,
                "retWarehouseDate", validSprinkler::setRetWarehouseDate
        );
        dateSetters.forEach((fieldName, setter) -> {
            try {
                setter.accept(parseDate.apply(getDateField(form, fieldName)));
            } catch (BusinessException e) {
                throw new BusinessException(String.format("喷头序列号 %s 的%s失败: %s", form.getSprinklerSerial(), fieldName, e.getMessage()));
            }
        });
        if (form == null) {
            throw new BusinessException("空消息异常");
        }
        if (form.getStatus() == null) {
            throw new BusinessException("空状态异常");
        }
        if (!form.getStatus().equals("rma")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        return validSprinkler;
    }

    private String getDateField(RmaSprinklerImportForm form, String fieldName) {
        switch (fieldName) {
            case "retMaintainenceDate":
                return form.getRetMaintainenceDate();
            case "retWarehouseDate":
                return form.getRetWarehouseDate();
            default:
                throw new BusinessException("非法的参数：" + fieldName);
        }
    }

    private DamagedSprinklerUpdateForm transferImportToUpdate(DamagedSprinklerImportForm form) {
        // 使用Bean拷贝工具优化属性复制
        DamagedSprinklerUpdateForm updateVO = SmartBeanUtil.copy(form, DamagedSprinklerUpdateForm.class);
        // 预定义支持的日期格式
        final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy.M.dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.d"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        );
        // 通用日期解析方法
        Function<String, LocalDate> parseDate = (dateStr) -> {
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    // 尝试下一个格式
                }
            }
            throw new BusinessException("非法日期格式: " + dateStr);
        };
        // 处理日期字段（支持动态添加新字段）
        Map<String, Consumer<LocalDate>> dateSetters = Map.of(
                "retWarehouseDate", updateVO::setRetWarehouseDate);
        dateSetters.forEach((fieldName, setter) -> {
            try {
                setter.accept(parseDate.apply(getDateField(form, fieldName)));
            } catch (BusinessException e) {
                throw new BusinessException(String.format("喷头序列号 %s 的%s失败: %s", form.getSprinklerSerial(), fieldName, e.getMessage()));
            }
        });
        if (!form.getStatus().equals("破损仓")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        return updateVO;
    }

    private String getDateField(DamagedSprinklerImportForm form, String fieldName) {
        switch (fieldName) {
            case "retWarehouseDate":
                return form.getRetWarehouseDate();
            default:
                throw new BusinessException("非法的参数：" + fieldName);
        }
    }

    private MaintainingSprinklerUpdateForm transferImportToUpdate(MaintainingSprinklerImportForm form) {
// 使用Bean拷贝工具优化属性复制
        MaintainingSprinklerUpdateForm updateVO = SmartBeanUtil.copy(form, MaintainingSprinklerUpdateForm.class);
        // 预定义支持的日期格式
        final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy.M.dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.d"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd")
        );
        // 通用日期解析方法
        Function<String, LocalDate> parseDate = (dateStr) -> {
            if (dateStr == null || dateStr.isEmpty()) {
                return null;
            }

            for (DateTimeFormatter formatter : DATE_FORMATTERS) {
                try {
                    return LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    // 尝试下一个格式
                }
            }
            throw new BusinessException("非法日期格式: " + dateStr);
        };
        // 处理日期字段（支持动态添加新字段）
        Map<String, Consumer<LocalDate>> dateSetters = Map.of(
                "retMaintainenceDate", updateVO::setRetMaintainenceDate);
        dateSetters.forEach((fieldName, setter) -> {
            try {
                setter.accept(parseDate.apply(getDateField(form, fieldName)));
            } catch (BusinessException e) {
                throw new BusinessException(String.format("喷头序列号 %s 的%s失败: %s", form.getSprinklerSerial(), fieldName, e.getMessage()));
            }
        });
        if (!form.getStatus().equals("维修仓")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        return updateVO;
    }

    private String getDateField(MaintainingSprinklerImportForm form, String fieldName) {
        switch (fieldName) {
            case "retMaintainenceDate":
                return form.getRetMaintainenceDate();
            default:
                throw new BusinessException("非法的参数：" + fieldName);
        }
    }

    private MachineSprinklerUpdateForm transferImportToUpdate(MachineSprinklerImportForm form) {
        // 使用Bean拷贝工具优化属性复制
        MachineSprinklerUpdateForm updateVO = SmartBeanUtil.copy(form, MachineSprinklerUpdateForm.class);
        if (!form.getStatus().equals("机台")) {
            throw new BusinessException(String.format("喷头序列号 %s 的仓参数非法: %s", form.getSprinklerSerial(), form.getStatus()));
        }
        return updateVO;
    }

}

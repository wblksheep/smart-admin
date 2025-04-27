package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.ConditionBuildException;
import org.springframework.util.ReflectionUtils;
import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class LambdaUtils {
    private static final ConcurrentHashMap<String, SFunction<?, ?>> CACHE = new ConcurrentHashMap<>();

    /**
     * 将数据库列名解析为Lambda表达式
     * @param tableClass 实体类
     * @param column     数据库列名（支持驼峰或下划线）
     * @return 对应的SFunction
     */
    @SuppressWarnings("unchecked")
    public static <T> SFunction<T, ?> resolveSFunction(Class<T> tableClass, String column) {
        String key = tableClass.getName() + "#" + column;
        return (SFunction<T, ?>) CACHE.computeIfAbsent(key, k -> {
            try {
                String fieldName = convertColumnToField(column); // 列名转字段名
                Field field = ReflectionUtils.findField(tableClass, fieldName);
                if (field == null) throw new NoSuchFieldException(fieldName);

                String methodName = getterMethodName(field);     // 生成getter方法名
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodType methodType = MethodType.methodType(field.getType(), tableClass);
                CallSite site = LambdaMetafactory.metafactory(
                        lookup,
                        "apply",
                        MethodType.methodType(SFunction.class),
                        methodType.generic(),
                        lookup.findVirtual(tableClass, methodName, methodType),
                        methodType
                );
                return (SFunction<T, ?>) site.getTarget().invokeExact();
            } catch (Throwable e) {
                try {
                    throw new ConditionBuildException("Lambda解析失败: " + column, (Exception) e);
                } catch (ConditionBuildException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    // 数据库列名转字段名（示例：user_name -> userName）
    private static String convertColumnToField(String column) {
        return StringUtils.isBlank(column) ? "" : StringUtils.underlineToCamel(column);
    }

    // 根据字段类型生成getter方法名（兼容boolean字段）
    private static String getterMethodName(Field field) {
        String prefix = field.getType() == boolean.class ? "is" : "get";
        return StringUtils.concatCapitalize(prefix, field.getName());
    }
}
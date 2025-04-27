package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.annotation.ConditionField;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.annotation.JoinConditionConfig;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception.ConditionBuildException;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.utils.LambdaUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JoinConditionProcessor {
    public static void process(Object form, MPJLambdaWrapper<?> wrapper) {
        Class<?> formClass = form.getClass();
        JoinConditionConfig config = formClass.getAnnotation(JoinConditionConfig.class);

        if (config == null) return;


        Arrays.stream(formClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ConditionField.class))
                .forEach(f -> applyCondition(f, form, wrapper, config));
    }

    private static void applyCondition(Field field, Object form,
                                       MPJLambdaWrapper<?> wrapper,
                                       JoinConditionConfig config){
        try {
            field.setAccessible(true);
            Object value = field.get(form);
            if (value == null) return;


            ConditionField condition = field.getAnnotation(ConditionField.class);
            Class<?> tableClass = config.tableClass();
            String column = condition.column();


            switch (condition.type()) {
                case EQUAL -> wrapper.eq(columnMapping(tableClass, column), value);
                case LIKE -> wrapper.like(columnMapping(tableClass, column), "%" + value + "%");
//                case APPLY -> wrapper.apply()
                // 扩展其他条件类型...
            }
        } catch (IllegalAccessException e) {
//            throw new ConditionBuildException("条件构建失败", e);
            e.printStackTrace();
        }
    }


    private static <T> SFunction<T, ?> columnMapping(Class<T> tableClass, String column) {
        // 实现列名到Lambda的映射（可通过缓存优化）
        return LambdaUtils.resolveSFunction(tableClass, column);
    }
}

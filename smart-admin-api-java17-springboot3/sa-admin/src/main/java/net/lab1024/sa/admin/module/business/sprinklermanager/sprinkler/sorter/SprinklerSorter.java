package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.sorter;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SprinklerSorter {
    private static final Pattern SERIAL_PATTERN = Pattern.compile("^(\\w{6})-(\\d{2})$"); // 匹配 XXXXX-AA 格式

    /**
     * 通用排序方法：按 sprinklerSerial 字段的 XXXXX 部分升序，AA 部分升序，其他格式置后
     *
     * @param list 待排序列表（支持 SprinklerVO/UsableSprinklerVO 等具有 getSprinklerSerial() 方法的对象）
     */
    public static <T> void sortBySprinklerSerial(List<T> list) {
        list.sort(Comparator
                .comparing((T obj) -> parseSerial(obj)) // 解析序列号
                .thenComparing(obj -> parsePrefix(obj)) // 解析前缀
                .thenComparing(obj -> parseSuffix(obj)) // 解析后缀
                .thenComparing(obj -> getSerialString(obj)) // 保证稳定性
        );
    }

    // 解析序列号格式优先级
    private static <T> Integer parseSerial(T obj) {
        String serial = getSerialString(obj);
        Matcher matcher = SERIAL_PATTERN.matcher(serial);
        return matcher.find() ? 0 : 1; // 0=符合格式（优先），1=其他格式（置后）
    }

    // 提取 XXXXX 部分并转换为比较键
    private static <T> Integer parsePrefix(T obj) {
        String serial = getSerialString(obj);
        Matcher matcher = SERIAL_PATTERN.matcher(serial);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : Integer.MAX_VALUE;
    }

    // 提取 AA 部分并转换为数字
    private static <T> Integer parseSuffix(T obj) {
        String serial = getSerialString(obj);
        Matcher matcher = SERIAL_PATTERN.matcher(serial);
        return matcher.find() ? Integer.parseInt(matcher.group(2)) : Integer.MAX_VALUE;
    }

    // 反射获取 sprinklerSerial 字段值（泛型兼容）
    private static <T> String getSerialString(T obj) {
        try {
            return (String) obj.getClass().getMethod("getSprinklerSerial").invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException("对象缺少 getSprinklerSerial() 方法", e);
        }
    }
}

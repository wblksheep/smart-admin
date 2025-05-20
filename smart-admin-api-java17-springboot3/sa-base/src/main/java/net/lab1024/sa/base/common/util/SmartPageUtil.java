package net.lab1024.sa.base.common.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.PageParam;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页工具类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020-04-23 20:51:40
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
public class SmartPageUtil {

    /**
     * 转换为查询参数
     */
    public static Page<?> convert2PageQuery(PageParam pageParam) {
        Page<?> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());

        if (pageParam.getSearchCount() != null) {
            page.setSearchCount(pageParam.getSearchCount());
        }

        List<PageParam.SortItem> sortItemList = pageParam.getSortItemList();
        if (CollectionUtils.isEmpty(sortItemList)) {
            return page;
        }

        // 设置排序字段并检测是否含有sql注入
        List<OrderItem> orderItemList = new ArrayList<>();
        for (PageParam.SortItem sortItem : sortItemList) {

            if (SmartStringUtil.isEmpty(sortItem.getColumn())) {
                continue;
            }

            if (SqlInjectionUtils.check(sortItem.getColumn())) {
                log.error("《存在SQL注入：》 : {}", sortItem.getColumn());
                throw new BusinessException("存在SQL注入风险，请联系技术工作人员！");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(sortItem.getColumn());
            orderItem.setAsc(sortItem.getIsAsc());
            orderItemList.add(orderItem);
        }
        page.setOrders(orderItemList);
        return page;
    }

    /**
     * 转换为查询参数
     */
    public static Page<?> convert2PageQueryBySprinklerSerial(PageParam pageParam) {
        Page<?> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());

        if (pageParam.getSearchCount() != null) {
            page.setSearchCount(pageParam.getSearchCount());
        }

        List<PageParam.SortItem> sortItemList = pageParam.getSortItemList();
        if (CollectionUtils.isEmpty(sortItemList)) {
            return page;
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        for (PageParam.SortItem sortItem : sortItemList) {
            if (SmartStringUtil.isEmpty(sortItem.getColumn())) continue;

            // 特定字段特殊处理
            if ("sprinkler_serial".equals(sortItem.getColumn())) {
                addSprinklerSerialSort(orderItemList, sortItem.getIsAsc());
                continue;
            }

            // 常规字段安全检查
            if (SqlInjectionUtils.check(sortItem.getColumn())) {
                log.error("SQL注入风险: {}", sortItem.getColumn());
                throw new BusinessException("非法排序参数");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(sortItem.getColumn());
            orderItem.setAsc(sortItem.getIsAsc());
            orderItemList.add(orderItem);
        }
        page.setOrders(orderItemList);
        return page;
    }

    // 添加 sprinklerSerial 特殊排序规则
    private static void addSprinklerSerialSort(List<OrderItem> orderList, Boolean isAsc) {
        // 规则1：优先排序符合 XXXXXX-AA 格式的记录
        OrderItem formatCheck = new OrderItem();
        formatCheck.setColumn("CASE WHEN sprinkler_serial REGEXP '^\\\\w{6}-\\\\d{2}$' THEN 0 ELSE 1 END");
        formatCheck.setAsc(true); // 强制符合格式的在前

        // 规则2：按 XXXXXX 部分升序
        OrderItem prefixSort = new OrderItem();
        prefixSort.setColumn("SUBSTRING(sprinkler_serial, 1, 6)");
        prefixSort.setAsc(isAsc); // 继承原始排序方向

        // 规则3：按 AA 部分数字升序
        OrderItem suffixSort = new OrderItem();
        suffixSort.setColumn("CAST(SUBSTRING(sprinkler_serial, 7, 2) AS UNSIGNED)");
        suffixSort.setAsc(isAsc); // 继承原始排序方向

        Collections.addAll(orderList, formatCheck, prefixSort, suffixSort);
    }

    /**
     * 转换为 PageResult 对象
     */
    public static <T, E> PageResult<T> convert2PageResult(Page<?> page, List<E> sourceList, Class<T> targetClazz) {
        return convert2PageResult(page, SmartBeanUtil.copyList(sourceList, targetClazz));
    }

    /**
     * 转换为 PageResult 对象
     */
    public static <E> PageResult<E> convert2PageResult(Page<?> page, List<E> sourceList) {
        PageResult<E> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setList(sourceList);
        pageResult.setEmptyFlag(CollectionUtils.isEmpty(sourceList));
        return pageResult;
    }

    /**
     * 转换分页结果对象
     */
    public static <E, T> PageResult<T> convert2PageResult(PageResult<E> pageResult, Class<T> targetClazz) {
        PageResult<T> newPageResult = new PageResult<>();
        newPageResult.setPageNum(pageResult.getPageNum());
        newPageResult.setPageSize(pageResult.getPageSize());
        newPageResult.setTotal(pageResult.getTotal());
        newPageResult.setPages(pageResult.getPages());
        newPageResult.setEmptyFlag(pageResult.getEmptyFlag());
        newPageResult.setList(SmartBeanUtil.copyList(pageResult.getList(), targetClazz));
        return newPageResult;
    }

    public static <T> PageResult<T> subListPage(Integer pageNum, Integer pageSize, List<T> list) {
        PageResult<T> pageRet = new PageResult<T>();
        //总条数
        int count = list.size();
        int pages = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(pageNum * pageSize, count);

        if (pageNum > pages) {
            pageRet.setList(Lists.newLinkedList());
            pageRet.setPageNum(pageNum.longValue());
            pageRet.setPages((long) pages);
            pageRet.setTotal((long) count);
            return pageRet;
        }
        List<T> pageList = list.subList(fromIndex, toIndex);
        pageRet.setList(pageList);
        pageRet.setPageNum(pageNum.longValue());
        pageRet.setPages((long) pages);
        pageRet.setTotal((long) count);
        return pageRet;
    }
}

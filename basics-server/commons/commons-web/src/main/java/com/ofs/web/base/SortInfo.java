package com.ofs.web.base;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 排序对象
 *
 * @author ly
 */
@SuppressWarnings("unchecked")
public class SortInfo implements Serializable {
    /**
     * 代替关键字
     */
    private static final String ORDER_EXPR_FLG = "?";
    /**
     * 替换数量
     */
    private static final int EXPR_LENGTH = 2;

    /**
     * 合法字符
     */
    private static final String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";

    /**
     * 升降序
     */
    private Direction direction;
    /**
     * 排序字段
     */
    private String property;
    /**
     * 排序函数处理
     */
    private String orderExpr;

    /**
     * 是否要把驼峰转成大写下划线格式
     */
    private boolean camelToUnderlineFlg = true;

    public SortInfo(String property, Direction direction) {
        this.direction = direction;
        this.property = property;
    }

    public SortInfo(String property, Direction direction, String orderExpr) {
        this.direction = direction;
        this.property = property;
        this.orderExpr = orderExpr;
    }


    public SortInfo(String property, Direction direction, String orderExpr, boolean camelToUnderlineFlg) {
        this.direction = direction;
        this.property = property;
        this.orderExpr = orderExpr;
        this.camelToUnderlineFlg = camelToUnderlineFlg;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * 把小驼峰转成数据库“_”字段分隔
     *
     * @return
     */
    public String getProperty() {
        if (camelToUnderlineFlg) {
            return StringUtils.camelToUnderline(property);
        } else {
            return property;
        }
    }

    public String getOrderExpr() {
        return orderExpr;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setOrderExpr(String orderExpr) {
        this.orderExpr = orderExpr;
    }


    public static boolean isSQLInjection(String str) {
        return !Pattern.matches(INJECTION_REGEX, str);
    }

    @Override
    public String toString() {
        property = this.getProperty();
        if (isSQLInjection(property)) {
            throw new IllegalArgumentException("SQLInjection property: " + property);
        }
        if (orderExpr != null && orderExpr.indexOf(ORDER_EXPR_FLG) != -1) {
            String[] exprs = orderExpr.split("\\?");
            if (exprs.length == EXPR_LENGTH) {
                return String.format(orderExpr.replaceAll("\\?", "%s"), property) + (direction == null ? "" : " " + direction.name());
            }
            return String.format(orderExpr.replaceAll("\\?", "%s"), property, direction == null ? "" : " " + direction.name());
        }
        return property + (direction == null ? "" : " " + direction.name());
    }


    public static List<SortInfo> formStringToList(String orderSegment, boolean camelToUnderlineFlg) {
        return formStringToList(orderSegment, null, camelToUnderlineFlg);
    }

    /**
     * Order.formStringToList("name.desc", "nlssort(? ,'NLS_SORT=SCHINESE_PINYIN_M')");
     * nlssort(name ,'NLS_SORT=SCHINESE_PINYIN_M') DESC
     *
     * @param orderSegment ex: "id.asc,code.desc" or "code.desc"
     */
    public static List<SortInfo> formStringToList(String orderSegment, String orderExpr, boolean camelToUnderlineFlg) {
        if (orderSegment == null || "".equals(orderSegment.trim())) {
            return new ArrayList<>(0);
        }

        List<SortInfo> results = new ArrayList();
        String[] orderSegments = orderSegment.trim().split(",");
        for (int i = 0; i < orderSegments.length; i++) {
            String sortSegment = orderSegments[i];
            SortInfo order = formStringToObject(sortSegment, orderExpr, camelToUnderlineFlg);
            if (order != null) {
                results.add(order);
            }
        }
        return results;
    }


    private static SortInfo formStringToObject(String orderSegment, String orderExpr, boolean camelToUnderlineFlg) {

        if (orderSegment == null || "".equals(orderSegment.trim()) ||
                orderSegment.startsWith("null.") || orderSegment.startsWith(".")) {
            return null;
        }

        String[] array = orderSegment.trim().split("\\.");
        if (array.length != 1 && array.length != EXPR_LENGTH) {
            throw new IllegalArgumentException("orderSegment pattern must be {property}.{direction}, input is: " + orderSegment);
        }

        return create(array[0], array.length == EXPR_LENGTH ? array[1] : "asc", orderExpr, camelToUnderlineFlg);
    }

    public static SortInfo create(String property, String direction, boolean camelToUnderlineFlg) {
        return create(property, direction, null, camelToUnderlineFlg);
    }

    /**
     * @param property
     * @param direction
     * @param orderExpr placeholder is "?", in oracle like: "nlssort( ? ,'NLS_SORT=SCHINESE_PINYIN_M')".
     *                  Warning: you must prevent orderExpr SQL injection.
     * @return
     */
    public static SortInfo create(String property, String direction, String orderExpr, boolean camelToUnderlineFlg) {
        return new SortInfo(property, SortInfo.Direction.fromString(direction), orderExpr, camelToUnderlineFlg);
    }


    /**
     * PropertyPath implements the pairing of an {@link Direction} and a property. It is used to provide input for
     *
     * @author Oliver Gierke
     */
    public enum Direction {
        /**
         * 排序规则
         */
        ASC, DESC;

        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                return ASC;
            }
        }
    }
}

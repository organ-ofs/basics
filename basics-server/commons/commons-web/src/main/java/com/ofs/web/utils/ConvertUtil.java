package com.ofs.web.utils;

import com.ofs.web.exception.GlobalErrorException;
import com.ofs.web.knowledge.FrameMessageEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * 类型转换的工具类
 *
 * @author 赵琦伟
 * @since 2018/04/17
 */
public class ConvertUtil {
    private static final String BLANK_STR = "";
    private static final String DATE_REGEX = "^\\d{8}$";
    private static final String TIME_REGEX = "^\\d{6}$";
    private static final String DATE_TIME_REGEX = "^\\d{14}$";
    private static final String NUM_REGEX = "^[+\\-]?\\d+(\\.\\d+)?$";

    /**
     * 检查类型
     *
     * @author gaoly
     */
    private enum CHECK_TYPE {
        /**
         * 日期
         */
        DATE,
        /**
         * 时间
         */
        TIME,
        /**
         * 日期时间
         */
        DATETIME
    }

    private ConvertUtil() {
    }

    /**
     * 检查输入的对象是否可以转换为数值
     *
     * @param obj Object
     * @return 是否可以转换为数值
     */
    public static boolean canToNum(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Number) {
            return true;
        }
        String str = obj.toString();
        if (str.isEmpty()) {
            return true;
        } else {
            return str.matches(NUM_REGEX);
        }
    }

    /**
     * 将Object转换为String<br>
     * 如果参数为null, 返回空串
     *
     * @param obj Object
     * @return 转换后的字符串
     */
    public static String toStr(Object obj) {
        if (obj == null) {
            return BLANK_STR;
        } else {
            return obj.toString();
        }
    }

    /**
     * 将Object转换为String<br>
     * 如果参数为null, 返回null
     *
     * @param obj Object
     * @return 转换后的字符串
     */
    public static String toStrWithNull(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }

    /**
     * 将Object转换为数值
     *
     * @param obj Object
     * @return 转换后的数值
     */
    public static Number toNum(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number) {
            return (Number) obj;
        }
        try {
            return new BigDecimal(obj.toString());
        } catch (NumberFormatException e) {
            throw new GlobalErrorException(FrameMessageEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 将Object转换为日期
     *
     * @param obj 日期型 或者 yyyyMMdd 型的字符串
     * @return 转换后的日期
     */
    public static Date toDate(Object obj) {
        return toCalendar(obj, CHECK_TYPE.DATE);
    }

    /**
     * 将Object转换为日期
     *
     * @param obj 日期型 或者 HHmmss 型的字符串
     * @return 转换后的日期
     */
    public static Date toTime(Object obj) {
        return toCalendar(obj, CHECK_TYPE.TIME);
    }

    /**
     * 将Object转换为日期
     *
     * @param obj 日期型对象
     * @return 转换后的日期
     */
    public static Date toDatetime(Object obj) {
        return toCalendar(obj, CHECK_TYPE.DATETIME);
    }

    private static Date toCalendar(Object obj, CHECK_TYPE checkType) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof Calendar) {
            return ((Calendar) obj).getTime();
        }

        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTimeInMillis(0);
        int yy = 0;
        int mm = 0;
        int dd = 0;
        int hh = 0;
        int mi = 0;
        int ss = 0;

        if (obj instanceof LocalDate) {
            LocalDate date = (LocalDate) obj;
            yy = date.getYear();
            mm = date.getMonthValue();
            dd = date.getDayOfMonth();
            cal.set(yy, mm, dd, hh, mi, ss);
            return cal.getTime();
        }

        if (obj instanceof LocalDateTime) {
            LocalDateTime date = (LocalDateTime) obj;
            yy = date.getYear();
            mm = date.getMonthValue();
            dd = date.getDayOfMonth();
            hh = date.getHour();
            mi = date.getMinute();
            ss = date.getSecond();
            cal.set(yy, mm, dd, hh, mi, ss);
            return cal.getTime();
        }

        String dateStr = Objects.toString(obj);
        if (dateStr.isEmpty()) {
            return null;
        }
        if (checkType == CHECK_TYPE.DATE && !dateStr.matches(DATE_REGEX) && !dateStr.matches(DATE_TIME_REGEX)) {
            throw new GlobalErrorException(FrameMessageEnum.SYSTEM_ERROR);
        }
        if (checkType == CHECK_TYPE.TIME && !dateStr.matches(TIME_REGEX) && !dateStr.matches(DATE_TIME_REGEX)) {
            throw new GlobalErrorException(FrameMessageEnum.SYSTEM_ERROR);
        }
        if (checkType == CHECK_TYPE.DATETIME && !dateStr.matches(DATE_REGEX) && !dateStr.matches(DATE_TIME_REGEX)) {
            throw new GlobalErrorException(FrameMessageEnum.SYSTEM_ERROR);
        }
        if (dateStr.matches(DATE_REGEX)) {
            yy = Integer.valueOf(dateStr.substring(0, 4));
            mm = Integer.valueOf(dateStr.substring(4, 6));
            dd = Integer.valueOf(dateStr.substring(6, 8));
        }
        if (dateStr.matches(TIME_REGEX)) {
            hh = Integer.valueOf(dateStr.substring(0, 2));
            mi = Integer.valueOf(dateStr.substring(2, 4));
            ss = Integer.valueOf(dateStr.substring(4, 6));
        }
        if (dateStr.matches(DATE_TIME_REGEX)) {
            yy = Integer.valueOf(dateStr.substring(0, 4));
            mm = Integer.valueOf(dateStr.substring(4, 6));
            dd = Integer.valueOf(dateStr.substring(6, 8));
            hh = Integer.valueOf(dateStr.substring(8, 10));
            mi = Integer.valueOf(dateStr.substring(10, 12));
            ss = Integer.valueOf(dateStr.substring(12, 14));
        }
        cal.set(yy, mm, dd, hh, mi, ss);
        return cal.getTime();
    }
}

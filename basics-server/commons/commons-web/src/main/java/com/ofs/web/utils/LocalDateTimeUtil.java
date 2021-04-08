package com.ofs.web.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 日期实际工具类
 */
public class LocalDateTimeUtil {

    private static final int INT_1 = 1;
    private static final int INT_5 = 5;
    private static final int INT_7 = 7;
    private static final String[] WEEK_DAYS = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    // ==格式到年==
    /**
     * 日期格式，年份，例如：2004，2008
     */
    public static final String FMT_YYYY = "yyyy";


    // ==格式到年月 ==
    /**
     * 日期格式，年份和月份，例如：200707，200808
     */
    public static final String FMT_YYYYMM = "yyyyMM";

    /**
     * 日期格式，年份和月份，例如：200707，2008-08
     */
    public static final String FMT_YYYY_MM = "yyyy-MM";


    // ==格式到年月日==
    /**
     * 日期格式，年月日，例如：050630，080808
     */
    public static final String FMT_YYMMDD = "yyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
     */
    public static final String FMT_YY_MM_DD = "yy-MM-dd";

    /**
     * 日期格式，年月日，例如：20050630，20080808
     */
    public static final String FMT_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
     */
    public static final String FMT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式，年月日，例如：2016.10.05
     */
    public static final String FMT_POINTYYYYMMDD = "yyyy.MM.dd";

    /**
     * 日期格式，年月日，例如：2016年10月05日
     */
    public static final String DATE_TIME_FORMAT_YYYYHMMHDDH = "yyyy年MM月dd日";


    // ==格式到年月日 时分 ==

    /**
     * 日期格式，年月日时分，例如：200506301210，200808081210
     */
    public static final String FMT_YYYYMMDDHHMM = "yyyyMMddHHmm";

    /**
     * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

    /**
     * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";


    // ==格式到年月日 时分秒==
    /**
     * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
     * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";


    // ==格式到年月日 时分秒 毫秒==
    /**
     * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";


    // ==特殊格式==
    /**
     * 日期格式，月日时分，例如：10-05 12:00
     */
    public static final String FMT_MMDDHHMI = "MM-dd HH:mm";

    private LocalDateTimeUtil() {
    }

    /**
     * 将Date转换为LocalDateTime
     *
     * @param date Date
     * @return 将Date转换为LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 取得系统时间
     *
     * @return 系统时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 以YYYY/MM/DD HH24:MI:SS格式返回系统日期时间
     *
     * @return 系统日期时间
     */
    public static String getSysDateTimeString() {
        return formatDateTime(now(), DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
    }

    /**
     * 取得当天日期(时间部分为0)
     *
     * @return 当天日期(时间部分为0)
     */
    public static LocalDateTime today() {
        return now().toLocalDate().atStartOfDay();
    }

    /**
     * 取得当天日期(yyyyMMdd格式)
     *
     * @return 当天日期(yyyyMMdd格式)
     */
    public static String getTodayStr() {
        return formatDateTime(now(), FMT_YYYYMMDD);
    }

    /**
     * 取得当月日期(yyyyMM格式)
     *
     * @return 当天日期(yyyyMM格式)
     */
    public static String getMonthStr() {
        return formatDateTime(now(), FMT_YYYYMM);
    }

    /**
     * 取得当年日期(yyyy格式)
     *
     * @return 当天日期(yyyy格式)
     */
    public static String getYearStr() {
        return formatDateTime(now(), FMT_YYYY);
    }

    /**
     * 日期转字符串(yyyyMMdd格式)
     *
     * @param date 日期
     * @return 字符串
     */
    public static String dateToString(LocalDateTime date) {
        return formatDateTime(date, FMT_YYYYMMDD);
    }

    /**
     * 日期转字符串(yyyy-MM-dd HH:mm:ss格式)
     *
     * @param date 日期
     * @return 字符串
     */
    public static String dateTimeToString(LocalDateTime date) {
        return formatDateTime(date, DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
    }

    /**
     * 格式化Date时间
     *
     * @param time       Date类型时间
     * @param timeFormat String类型格式
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(LocalDateTime time, String timeFormat) {
        return formatDateTime(time, timeFormat);
    }

    /**
     * 格式化String时间
     *
     * @param timeStr    String类型时间
     * @param timeFormat String类型格式
     * @return 格式化后的Date日期
     */
    public static LocalDateTime parseStrToDate(String timeStr, String timeFormat) {
        return parseDateTime(timeStr, timeFormat);
    }

    /**
     * 获取当前日期是一年中第几周
     *
     * @param date 日期
     * @return 周
     */
    public static Integer getWeekthOfYear(LocalDate date) {
        if (date == null) {
            return null;
        }
        int yearStartWeek = LocalDate.of(date.getYear(), INT_1, INT_1).getDayOfWeek().getValue();
        return (date.getDayOfYear() + yearStartWeek + INT_5) / INT_7;
    }

    /**
     * 获取当前日期是一年中第几周
     *
     * @param date 日期
     * @return 周
     */
    public static Integer getWeekthOfYear(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return getWeekthOfYear(date.toLocalDate());
    }


    /**
     * 获取某一年的总周数
     *
     * @param year 年
     * @return 周数
     */
    public static Integer getWeekCountOfYear(int year) {
        return getWeekthOfYear(Year.of(year).atMonth(Month.DECEMBER).atEndOfMonth());
    }

    /**
     * 获取指定日期所在周的第一天
     *
     * @param date 日期
     * @return 日期
     */
    public static LocalDateTime getFirstDayOfWeek(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate().plusDays(date.getDayOfWeek().getValue() - INT_1).atStartOfDay();
    }

    /**
     * 获取指定日期所在周的最后一天
     *
     * @param date 日期
     * @return 日期
     */
    public static LocalDateTime getLastDayOfWeek(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate().plusDays(INT_7 - date.getDayOfWeek().getValue()).atStartOfDay();
    }

    /**
     * 获取某年某周的第一天
     *
     * @param year 目标年份
     * @param week 目标周数
     * @return 日期
     */
    public static LocalDateTime getFirstDayOfWeek(int year, int week) {
        LocalDate date = LocalDate.of(year, INT_1, INT_1);
        date = date.plusWeeks(week - INT_1);
        return date.plusDays(date.getDayOfWeek().getValue() - INT_1).atStartOfDay();
    }

    /**
     * 获取某年某周的最后一天
     *
     * @param year 目标年份
     * @param week 目标周数
     * @return 日期
     */
    public static LocalDateTime getLastDayOfWeek(int year, int week) {
        LocalDate date = LocalDate.of(year, INT_1, INT_1);
        date = date.plusWeeks(week - INT_1);
        return date.plusDays(INT_7 - date.getDayOfWeek().getValue()).atStartOfDay();
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return 日期
     */
    public static LocalDateTime getFirstDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, INT_1).atStartOfDay();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  目标年份
     * @param month 目标月份
     * @return 日期
     */
    public static LocalDateTime getLastDayOfMonth(int year, int month) {
        return YearMonth.of(year, month).atEndOfMonth().atStartOfDay();
    }

    /**
     * 获取某个日期为星期几
     *
     * @param date 日期
     * @return String "星期*"  "星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期日"
     */
    public static String getDayWeekOfDate1(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return WEEK_DAYS[date.getDayOfWeek().getValue()];
    }

    /**
     * 获得指定日期的星期几数
     *
     * @param date 日期
     * @return int 星期 （星期一：1，星期二：2，星期三：3，星期四：4，星期五：5，星期六：6，星期日：7）
     */
    public static Integer getDayWeekOfDate2(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.getDayOfWeek().getValue();
    }

    /**
     * 获得两个日期的时间戳之差 (天)
     *
     * @param startDate 开始日期
     * @param endDate   结算日期
     * @return 天数
     */
    public static Long getDistanceTimestamp(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        return Duration.between(startDate.toLocalDate(), endDate.toLocalDate()).toDays();
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date 时间
     * @return 时间
     */
    public static LocalDateTime getDayBeginTime(final LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate().atStartOfDay();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date 日期
     * @return 日期时间
     */
    public static LocalDateTime getDayEndTime(final LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate().plusDays(1).atStartOfDay().minusNanos(1);
    }

    /**
     * 取得指定月以后的日期
     *
     * @param sDate  (yyyyMM) : 如为Null，默认取当前系统时间
     * @param offset 月数
     * @return yyyyMM
     */
    public static String addMonth(String sDate, int offset) {
        YearMonth date;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(FMT_YYYYMM);
        if (sDate == null || sDate.isEmpty()) {
            date = YearMonth.now();
        } else {
            date = YearMonth.parse(sDate, fmt);
        }
        return date.plusMonths(offset).format(fmt);
    }

    public static String formatDateTime(LocalDateTime dateTime, String formatPattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static LocalDateTime parseDateTime(String timeStr, String formatPattern) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        TemporalAccessor temporal = DateTimeFormatter.ofPattern(formatPattern).parse(timeStr);
        LocalDate date = null;
        LocalTime time = null;
        try {
            date = LocalDate.from(temporal);
        } catch (DateTimeException e) {
            date = LocalDate.of(2000, 1, 1);
        }
        try {
            time = LocalTime.from(temporal);
        } catch (DateTimeException e) {
            time = LocalTime.ofNanoOfDay(0);
        }
        LocalDateTime dt = date.atStartOfDay();
        dt.plusNanos(time.toNanoOfDay());
        return dt;
    }

}

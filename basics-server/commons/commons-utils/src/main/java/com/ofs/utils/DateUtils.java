package com.ofs.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final String PATTERN_DATE = "yyyyMMdd";
    public static final String PATTERN_DATE_ONE = "yyyy-MM-dd";
    public static final String PATTERN_DATE_TWO = "yyyyMM";
    public static final String PATTERN_TIME = "HHmmss";
    public static final String PATTERN_TIME_TWO = "HH:mm:ss";

    public static String getCurrentDateWithLine() {
        LocalDate localDate = LocalDate.now();
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }

    public static String getCurrentDateWithOutLine() {
        LocalDate localDate = LocalDate.now();
        return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getCurrentDateWithOutLine(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateToStrFormatter = DateTimeFormatter.ofPattern(format);
        return dateToStrFormatter.format(now);
    }

    public static String getDateByDays(String date, int days) {
        LocalDate nextDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")).plusDays((long) days);
        return nextDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getDateByDays(String date, String formatter, int days) {
        LocalDate nextDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(formatter)).plusDays((long) days);
        return nextDate.format(DateTimeFormatter.ofPattern(formatter));
    }

    public static String getCurrentTimeInSeconds() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateToStrFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateToStrFormatter.format(now);
    }

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateToStrFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return dateToStrFormatter.format(now);
    }
}

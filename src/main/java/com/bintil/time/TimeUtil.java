package com.bintil.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author ：ywb
 * @date ：Created in 2023/1/4 14:47
 */
@SuppressWarnings({"unused", "unchecked"})
public class TimeUtil {
    private final static int START_YEAR = LocalDateTime.MIN.getYear();

    /**
     * 获取当前日期
     *
     * @param patten 日期转换模板
     */
    public static String getTimeString(String patten) {
        return formatTime(LocalDateTime.now(), patten);
    }


    /**
     * 获取当前日期 前或者后n的格式化日期
     *
     * @param patten     日期转换模板
     * @param preOrNextN 前n或者后n
     * @param chronoUnit 天，周，月，年...
     */
    public static String getTimeString(String patten, int preOrNextN, ChronoUnit chronoUnit) {
        return formatTime(LocalDateTime.now().plus(preOrNextN, chronoUnit), patten);
    }

    /**
     * 格式化日期
     *
     * @param time   时间
     * @param patten 日期转换模板
     */
    public static String formatTime(Object time, String patten) {
        LocalDateTime localDateTime = convertTime(time, LocalDateTime.class);
        return localDateTime.format(DateTimeFormatter.ofPattern(patten));
    }

    /**
     * 时间计算
     *
     * @param preOrNext  -为前，+为后
     * @param chronoUnit 单位
     * @param resultType localDateTime,date,calendar
     */
    public static <T> T computeTime(int preOrNext, ChronoUnit chronoUnit, Class<T> resultType) {
        LocalDateTime localDateTime = LocalDateTime.now().plus(preOrNext, chronoUnit);
        return convertTime(localDateTime, resultType);
    }

    /**
     * 按照输入的时间计算
     *
     * @param time       传入的时间
     * @param preOrNext  -为前，+为后
     * @param chronoUnit 单位
     * @param resultType localDateTime,date,calendar
     */
    public static <T> T computeTime(Object time, int preOrNext, ChronoUnit chronoUnit, Class<T> resultType) {
        LocalDateTime localDateTime = convertTime(time, LocalDateTime.class).plus(preOrNext, chronoUnit);
        return convertTime(localDateTime, resultType);
    }


    /**
     * 计算时间间隔
     *
     * @param start      开始时间
     * @param end        结束时间
     * @param chronoUnit 返回单位
     * @return 以start为基础，如果返回-n，那就是end<start
     */
    public static long computeTimeInterval(Object start, Object end, ChronoUnit chronoUnit) {
        LocalDateTime startTime = convertTime(start, LocalDateTime.class);
        LocalDateTime endTime = convertTime(end, LocalDateTime.class);
        Duration between = Duration.ZERO;
        if (chronoUnit.getDuration().getSeconds() < ChronoUnit.MONTHS.getDuration().getSeconds()) {
            between = Duration.between(startTime, endTime);
        }
        return switch (chronoUnit) {
            case MINUTES -> between.toMinutes();
            case SECONDS -> between.getSeconds();
            case HOURS -> between.toHours();
            case DAYS -> between.toDays();
            case MONTHS -> endTime.getMonthValue() - startTime.getMonthValue();
            case YEARS -> endTime.getYear() - startTime.getYear();
            default -> -1;
        };
    }

    /**
     * 时间转换
     *
     * @param time       时间 date,calendar,localDate,localDateTime
     * @param resultType 需要返回的类型
     * @param <T>        返回类型 ， 如果不为时间类型，返回的就是传入time的类型
     */
    public static <T> T convertTime(Object time, Class<T> resultType) {
        if (time instanceof Date) {
            Date date = (Date) time;
            if (resultType == LocalDateTime.class) {
                return (T) date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            if (resultType == Calendar.class) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                return (T) gregorianCalendar;
            }
            return (T) date;
        }
        if (time instanceof Calendar) {
            Calendar date = (Calendar) time;
            if (resultType == LocalDateTime.class) {
                return (T) date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            }
            if (resultType == Date.class) {
                return (T) date.getTime();
            }
            return (T) date;
        }

        if (time instanceof LocalDate) {
            time = ((LocalDate) time).atStartOfDay();
        }

        if (time instanceof LocalDateTime) {
            LocalDateTime date = (LocalDateTime) time;
            if (resultType == Date.class) {
                return (T) Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
            }
            if (resultType == Calendar.class) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
                return (T) gregorianCalendar;
            }
            return (T) date;
        }
        throw new RuntimeException("时间类型错误");
    }


    /**
     * 获取星期几或者在每年的索引
     *
     * @param time       时间
     * @param year       年份
     * @param chronoUnit 类型
     */
    public static int getDayIdxOfWeekOrYear(Object time, int year, ChronoUnit chronoUnit) {

        LocalDateTime localDateTime = convertTime(time, LocalDateTime.class);
        if (year > START_YEAR) {
            localDateTime = localDateTime.withYear(year);
        }
        if (chronoUnit == ChronoUnit.WEEKS) {
            return localDateTime.getDayOfWeek().getValue();
        }
        if (chronoUnit == ChronoUnit.YEARS) {
            return localDateTime.getDayOfYear();
        }
        throw new RuntimeException("出错");
    }


    /**
     * 输入的时间是那个月的第几个星期
     *
     * @param time       输入的时间
     * @param year       年份
     * @param chronoUnit 类型
     */
    public static int getWeekIdxOfMonthOrYear(Object time, int year, ChronoUnit chronoUnit) {
        LocalDateTime localDateTime = convertTime(time, LocalDateTime.class);
        if (year > START_YEAR) {
            localDateTime = localDateTime.withYear(year);
        }
        if (chronoUnit == ChronoUnit.MONTHS) {
            return localDateTime.get(WeekFields.ISO.weekOfMonth());
        }
        if (chronoUnit == ChronoUnit.YEARS) {
            return localDateTime.get(WeekFields.ISO.weekOfYear());
        }
        throw new RuntimeException("参数错误");
    }

    /**
     * 时区转换时间
     *
     * @param time       时间
     * @param timeZone   GMT时区
     * @param resultType 返回类型
     * @param <T>        返回
     */
    public static <T> T timeZoneConvert(Object time, String timeZone, Class<T> resultType) {
        return convertTime(LocalDateTime.ofInstant(convertTime(time, Date.class).toInstant(),
                        ZoneId.of(timeZone)),
                resultType);
    }
}

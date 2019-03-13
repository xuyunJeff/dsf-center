
package com.xuyun.platform.dsfcenterdata.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 日期时间工具类
 */
public class DateUtil {
//	2018-03-22 13:02:58.0
    private static final int step = 3;
    private static final int fullCursor = 2;
    private static final int fullIndex = 4 * 5;
    private static final String MATCH_TEMPLATE = "yyyy/MM/dd HH:mm:ss:SSS";
    private static final String PATTERN_TEMPLATE = "0000/00/00 00:00:00:000";
    public static final String FORMAT_25_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String FORMAT_22_DATE_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_17_DATE_TIME ="yyyyMMddHHmmssSSS";
    public static final String FORMAT_18_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_12_DATE_TIME_2 = "yyyyMMddHHmm";
    public static final String FORMAT_18_DATE_TIME2 = "yyyyMMddHHmmss";
    public static final String FORMAT_12_DATE_TIME = "yyyy-MM-dd HH";
    public static final String FORMAT_10_DATE = "yyyy-MM-dd";
    public static final String FORMAT_8_DATE = "yyyyMMdd";
    public static final String FORMAT_4 = "yyyyMM";
    public static final DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    public static final String FORMAT_DATE_T = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_STATE="00:00:00";
    public static final String DATE_END="23:59:59";
    private static String suffix = ".000Z";
    private static ZoneOffset ZONEOFFSET_BEIJING = ZoneOffset.of("+8");
    private static ZoneOffset ZONEOFFSET_EAST_US = ZoneOffset.of("-4");

    /**
     * 时间戳转为localDateTime
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTimeEastUs(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZONEOFFSET_EAST_US;
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static Long getDateTimeOfTimestampEastUs(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZONEOFFSET_EAST_US;
        return LocalDateTime.ofInstant(instant, zone).toInstant(ZONEOFFSET_EAST_US).toEpochMilli();
    }


    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZONEOFFSET_BEIJING;
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static Long getDateTimeOfTimestamps(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZONEOFFSET_BEIJING;
        return LocalDateTime.ofInstant(instant, zone).toInstant(ZONEOFFSET_BEIJING).toEpochMilli();
    }

       /**
     * 获取当前时间戳，毫秒
     * @return
     */
    public static Long currentTimestamp(){
        return LocalDateTime.now().toInstant(ZONEOFFSET_BEIJING).toEpochMilli();
    }

    /**
     * 获取当前时间戳，毫秒
     * @return
     */
    public static Long currentTimestampUs(){
        return LocalDateTime.now().toInstant(ZONEOFFSET_EAST_US).toEpochMilli();
    }

    /**
     * 获取当前时间戳，毫秒
     * @return
     */
    public static LocalDateTime current(){
        ZoneId beiJing = ZoneId.of("Asia/Shanghai");
        return LocalDateTime.now(beiJing);
    }

    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    /**
     * 获取当前的月份
     * @return
     */
    public static String currentMonth(){
        SimpleDateFormat format = local.get();
        format.applyPattern(FORMAT_4);
        Date d =new Date();
        return format.format(d);
    }

    /**
     * 获取指定时间戳和现在的差距有多少个月
     * @param timestamp
     * @return
     */
    public static List<String> getTargetMonth(Long timestamp){
        LocalDate today = LocalDate.now();
        LocalDate consumerDate = DateUtil.getDateTimeOfTimestamp(timestamp).toLocalDate();
        long months = consumerDate.until(today, ChronoUnit.MONTHS);
        List<String> monthList = new LinkedList<>();
        if(0 == months){
            monthList.add(currentMonth());
        }
        for (int q = 0; q < months; q++) {
            monthList.add(consumerDate.plusMonths(q+1).format(yearMonthFormatter));
        }
        return monthList;
    }

    /**
     * 获取SimpleDateFormat实例
     *
     * @param pattern 模式串
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        SimpleDateFormat format = local.get();
        format.applyPattern(pattern);
        return format;
    }
    public static String getSimpleDateFormat(String pattern,Long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);//定义格式，不显示毫秒
        Timestamp now = new Timestamp(timestamp);//获取系统当前时间
        return df.format(now);
    }

    /**
     * 获取当前日期向前推某个月的时间
     * @param month
     * @return
     */
    public static Date getBeforeDate(Integer month) {
    	Integer m = 0-month;
    	Date dNow = new Date();   //当前时间
    	Date dBefore = new Date();
    	Calendar calendar = Calendar.getInstance(); //得到日历
    	calendar.setTime(dNow);//把当前时间赋给日历
    	calendar.add(calendar.MONTH, m);  //设置为前多少月
    	dBefore = calendar.getTime();   //得到前多少月的时间
    	return dBefore;
 }






    /**
     * 获取表示当前时间的字符串
     *
     * @param pattern 模式串
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取表示当前美东时间
     *
     * @return
     */
    public static String getDate(String pattern, Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(date);
    }

    /**
     * 获取表示当前美东时间
     *
     * @return
     */
    public static String getAmericaDate(String pattern, Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        sf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return sf.format(date);
    }

    /**
     * 日期时间格式化
     *
     * @param date    Date
     * @param pattern 模式串
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = getSimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 解析字符串类型日期, 为参数自动匹配解析模式串
     *
     * @param date 日期字符串
     * @return
     */
    public static Date parse(String date) {
        try {
            String[] mapper = format(date);
            SimpleDateFormat format = getSimpleDateFormat(mapper[0]);
            return format.parse(mapper[1]);
        } catch (ParseException e) {
            throw new RuntimeException("Unparseable date: \"" + date + "\"");
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 解析字符串类型日期
     *
     * @param date    日期字符串
     * @param pattern 模式串
     * @return
     */
    public static Date parse(String date, String pattern) {
        try {
            SimpleDateFormat format = getSimpleDateFormat(pattern);
            return format.parse(date);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 格式化参数日期字符串, 源日期字符填充到预设模板
     *
     * @param date 日期字符串
     * @return
     */
    private static String[] format(String date) {
        char[] origin = date.toCharArray();
        char[] pattern = PATTERN_TEMPLATE.toCharArray();
        char o, p;
        int cursor = 0, j = 0;
        for (int i = 0; i < origin.length; i++, j++) {
            o = origin[i];
            p = pattern[j];
            if (isCursor(o)) {
                if (!isCursor(p)) {
                    moveToNext(pattern, j - 1);
                    j++;
                }
                cursor = j;
            }
            if (isCursor(p)) {
                if (!isCursor(o)) {
                    cursor = j;
                    j++;
                }
            }
            pattern[j] = o;
        }
        j--;
        if (cursor < fullIndex - 1 && j - cursor == 1) {
            moveToNext(pattern, j);
        }
        cursor = pattern.length;
        for (int i = 0; i < fullCursor; i++) {
            if (pattern[cursor - 1] == '0') {
                cursor--;
            } else {
                break;
            }
        }
        if (cursor != pattern.length) {
            char[] target = new char[cursor];
            System.arraycopy(pattern, 0, target, 0, cursor);
            pattern = target;
        }
        char[] match = MATCH_TEMPLATE.toCharArray();
        for (int i = 4; i < fullIndex; i += step) {
            match[i] = pattern[i];
        }
        return new String[]{new String(match), new String(pattern)};
    }

    /**
     * 字符是否为非数值
     *
     * @param ch 被测试的字符
     * @return
     */
    private static boolean isCursor(char ch) {
        if (ch >= '0' && ch <= '9') {
            return false;
        }
        return true;
    }

    /**
     * 后移元素
     *
     * @param pattern 数组
     * @param i       被移动的元素的索引值
     */
    private static void moveToNext(char[] pattern, int i) {
        pattern[i + 1] = pattern[i];
        pattern[i] = '0';
    }


}
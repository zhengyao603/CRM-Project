package com.ssmproject.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date类型格式转换工具类
 */
public class DateUtils {

    /**
     * 对指定的dateTime对象进行格式化
     * @param dateTime
     * @return 字符串: yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(dateTime);
    }

    /**
     * 对指定的dateTime对象进行格式化
     * @param date
     * @return 字符串: yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 对指定的dateTime对象进行格式化
     * @param time
     * @return 字符串: HH:mm:ss
     */
    public static String formatTime(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(time);
    }
}

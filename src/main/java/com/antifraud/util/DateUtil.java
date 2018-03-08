package com.antifraud.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {
    /**
     * 通用时间格式
     */
    private static String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * long 转 String
     *
     * @param currentTime 当前时间long类型
     * @return 当前时间String类型
     * @throws ParseException 异常
     */
    public static String longToString(long currentTime) throws ParseException {
        Date date = longToDate(currentTime);
        return dateToString(date);
    }

    /**
     * long 转 Date
     *
     * @param currentTime 当前时间long类型
     * @return 当前时间Date类型
     * @throws ParseException 异常
     */
    public static Date longToDate(long currentTime) throws ParseException {
        Date   dateOld   = new Date(currentTime);
        String sDateTime = dateToString(dateOld);
        return stringToDate(sDateTime);
    }

    /**
     * Date 转 String
     *
     * @param date 当前时间Date类型
     * @return 当前时间String类型
     */
    public static String dateToString(Date date) {
        return new SimpleDateFormat(DateUtil.FORMAT).format(date);
    }

    /**
     * String 转 Date
     *
     * @param strTime 当前时间String类型
     * @return 当前时间Date类型
     * @throws ParseException 异常
     */
    public static Date stringToDate(String strTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtil.FORMAT);
        return formatter.parse(strTime);
    }

    private DateUtil() {
    }

}
package com.meiku.dev.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 

public class DateUtils {
	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss"; //模式时间格式
	
	/**
	 * 将指定日期格式化成指定格式的字符串输出
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format); // 格式化当前系统日期
		String str = dateFormat.format(date);
		return str;
	}
 

	/**
	 * 获取指定格式的当前时间
	 * 
	 * @return
	 */
	public static String getCurFormate(String format) {
		if (Tool.isEmpty(format)) {
			format = FORMAT;
		}
		return formatDate(new Date(), format);
	}
	
	  /**
     * 获取给定的时间，给定格式的时间字串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 获取给定的时间的yyyy-MM-dd字串
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return toString(date, FORMAT);
    }

    /**
     * 把给定的时间字符串从原有的格式转换为另一格式时间字串
     *
     * @param date
     *            原来的时间字串
     * @param sourcePattern
     *            原时间格式字串
     * @param targetPattern
     *            期望的时间格式字串
     * @return
     */
    public static String dateFormat(String date, String sourcePattern,
                                    String targetPattern) {
        return toString(toDate(date, sourcePattern), targetPattern);
    }
    
    public static Date toDate(String date, String pattern) {
        try {
            return toDateWithException(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Date toDateWithException(String date, String pattern)
            throws ParseException {
        if (Tool.isEmpty(date)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(date);
    }

    public static Date toDate(String date) {
        try {
            return toDateWithException(date, FORMAT);
        } catch (Exception e) {
            try {
                return toDateWithException(date, "dd/MM/yyyy");
            } catch (Exception e1) {
                return toDate(date, "yyyy/MM/dd");
            }
        }
    }

}

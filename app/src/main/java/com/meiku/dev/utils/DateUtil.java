package com.meiku.dev.utils;

import android.annotation.SuppressLint;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/** 注意格里历和儒略历交接时的日期差别 */
	private static transient int gregorianCutoverYear = 1582;
	public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	
	/**
	 * 获取网络当前时间字符串
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String getNetTimeStr() {
		 return  getCurFormate("");
//		URL url;
//		try {
//			url = new URL("http://www.baidu.com");
//			URLConnection uc = url.openConnection();// 生成连接对象
//			uc.connect(); // 发出连接
//			long ld = uc.getDate(); // 取得网站日期时间
//			Date date = new Date(ld); // 转换为标准时间对象
//			
//			SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT); // 格式化当前系统日期
//			String timeStr = dateFormat.format(date);
//			if(Tool.isEmpty(timeStr)) {
//				return getCurFormate("");
//			} else {
//				return timeStr;
//			}
//			
//		}  catch ( Exception e) {
//			 return  getCurFormate("");
//		}
	}
	
	/**
	 * 获取网络当前时间long型
	 */
	@SuppressLint("SimpleDateFormat") 
	public static long getNetTimeLong() {
		return  System.currentTimeMillis();
//		URL url;
//		try {
//			url = new URL("http://www.baidu.com");
//			URLConnection uc = url.openConnection();// 生成连接对象
//			uc.connect(); // 发出连接
//			long ld = uc.getDate(); // 取得网站日期时间
//		 
//			if(ld<=0) {
//				return System.currentTimeMillis();
//			}
//			return ld;
//		}  catch ( Exception e) {
//			 return  System.currentTimeMillis();
//		}
	}

	/**
	 * 检查传入的参数代表的年份是否为闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		if (year >= gregorianCutoverYear) {
			return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
		}

		return (year % 4 == 0);
	}

	/**
	 * 取当前日期时间，返回日期格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurSysTime() {
		return formatDateTime(new Date());
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

	public static Date str2Date(String str) {
		return str2Date(str, null);
	}

	public static Date str2Date(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * 将指定日期加一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date addOneDay(Date date) {
		return addDay(date, 1);
	}

	/**
	 * 将指定日期加上指定天数
	 * 
	 * @param date
	 *            指定日期
	 * @param dayNum
	 *            指定天数
	 * @return 返回增加指定天数后的日期
	 */
	public static Date addDay(Date date, int dayNum) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, dayNum);
		return c.getTime();
	}

	/**
	 * 将指定日期加上指定月数
	 * 
	 * @param date
	 *            指定日期
	 * @param dayNum
	 *            指定月数
	 * @return 返回增加指定月数后的日期
	 */
	public static Date addMonth(Date date, int monthNum) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, monthNum);
		return c.getTime();
	}

	/**
	 * 将指定日期加上指定分钟数
	 * 
	 * @return 返回增加指定月数后的日期
	 */
	public static Date addMinute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}

	/**
	 * 将指定日期加上指定秒数
	 */
	public static Date addSecond(Date date, int seconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}

	/**
	 * 计算两个日期之间相隔的年数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getYearsBetween(Date beginDate, Date endDate) {
		if (beginDate == null || endDate == null)
			return 0;
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		if (beginDate.after(endDate)) {
			beginCalendar.setTime(endDate);
			endCalendar.setTime(beginDate);
		} else {
			beginCalendar.setTime(beginDate);
			endCalendar.setTime(endDate);
		}

		int years = endCalendar.get(java.util.Calendar.YEAR)
				- beginCalendar.get(java.util.Calendar.YEAR);

		return years;
	}

	/**
	 * 计算两个日期之间相隔的天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getDaysBetween(Date beginDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		if (beginDate.after(endDate)) {
			beginCalendar.setTime(endDate);
			endCalendar.setTime(beginDate);
		} else {
			beginCalendar.setTime(beginDate);
			endCalendar.setTime(endDate);
		}

		int days = endCalendar.get(java.util.Calendar.DAY_OF_YEAR)
				- beginCalendar.get(java.util.Calendar.DAY_OF_YEAR);
		int endYear = endCalendar.get(java.util.Calendar.YEAR);

		// 如果不是同一年
		if (beginCalendar.get(java.util.Calendar.YEAR) != endYear) {
			do {
				days += beginCalendar
						.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				beginCalendar.add(java.util.Calendar.YEAR, 1);
			} while (beginCalendar.get(java.util.Calendar.YEAR) != endYear);
		}

		return days;
	}

	/***
	 * 剩余天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getLeftDays(Date beginDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		beginCalendar.setTime(beginDate);
		endCalendar.setTime(endDate);

		int days = endCalendar.get(java.util.Calendar.DAY_OF_YEAR)
				- beginCalendar.get(java.util.Calendar.DAY_OF_YEAR);
		int endYear = endCalendar.get(java.util.Calendar.YEAR);

		// 如果不是同一年
		if (beginCalendar.get(java.util.Calendar.YEAR) != endYear) {
			do {
				days += beginCalendar
						.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				beginCalendar.add(java.util.Calendar.YEAR, 1);
			} while (beginCalendar.get(java.util.Calendar.YEAR) != endYear);
		}

		return days;
	}

	/**
	 * 计算两个日期之间相隔的小时数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static String getHoursBetween(Date beginDate, Date endDate) {
		long between = (endDate.getTime() - beginDate.getTime()) / 1000;// 除以1000是为了转换成秒
		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60 / 60;
		if (day1 > 0) {
			return (int) day1 + "天前";
		} else if (hour1 > 0) {
			return (int) hour1 + "小时前";
		} else if (minute1 > 0) {
			return (int) minute1 + "分钟前";
		} else {
			return "刚刚";
		}

	}

	/**
	 * 将指定日期格式化成yyyy-mm-dd格式的字符串输出
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		String str = formatDate(date, "yyyy-MM-dd");
		return str;
	}

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
	 * 将指定日期格式化成yyyy-mm-dd HH:mm:ss格式的字符串输出
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 设置日期，返回Date
	 * 
	 * @param date
	 * @param hours
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date setTimeOfDate(Date date, int hours, int minute,
			int second) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hours);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		return c.getTime();
	}

	public static Calendar convertDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;
	}

	public static void main(String[] args) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.set(2008, (11 - 1), 28); // 2008-11-28
		c2.set(2009, (5 - 1), 18); // 2009-2-28
		// System.out.println(addOneDay(c1.getTime()));
		System.out.println(formatDate(c2.getTime()));
		System.out.println(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
	}

	public static Date StringToDate(String dateStr, String formatStr) {
		Date date = null;
		try {
			DateFormat dd = new SimpleDateFormat(formatStr);
			date = dd.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static long StringToLong(String formatDate,String date) {
		try {
		 SimpleDateFormat sdf= new SimpleDateFormat(formatDate);    
		 Date dt = sdf.parse(date);      
		 return dt.getTime(); 
		}catch (Exception e) {
			return System.currentTimeMillis();
		}
	}

	public static String ChangeFormat(String dateStr, String informatStr,
			String outformatStr) {
		String outString = "";
		try {
			Date date = null;
			DateFormat dd = new SimpleDateFormat(informatStr);
			date = dd.parse(dateStr);
			dd = new SimpleDateFormat(outformatStr);
			outString = dd.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outString;
	}

	public static int getDaysOfMonth(String dateStr, String formatStr) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat(formatStr);
		try {

			rightNow.setTime(simpleDate.parse(dateStr));

		} catch (ParseException e) {

			e.printStackTrace();

		}

		int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);

		return days;
	}

	/**
	 * 判断二个时间是否在同一个周
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 产生周序列,即得到当前时间所在的年度是第几周
	 * 
	 * @return
	 */
	public static String getSeqWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int week = c.get(Calendar.DAY_OF_WEEK);
		// sdate中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

}

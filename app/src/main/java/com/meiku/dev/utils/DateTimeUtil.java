package com.meiku.dev.utils;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.text.TextUtils;

/**
 * Created by xiekun on 2015/5/26.
 */
public class DateTimeUtil {
	private static final String defaultPattern = "yyyy-MM-dd";

	/**
	 * 格式化时间
	 * 
	 * @param date_time
	 * @return 2015-07-06 15:15
	 */
	public static String formatDateTime(String date_time) {
		String new_date_time;
		new_date_time = date_time.substring(0, date_time.length() - 5);
		return new_date_time;
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @return 07-06
	 */
	public static String formatDate(String date) {
		String new_date_time;
		new_date_time = date.substring(5, date.length() - 11);
		return new_date_time;
	}

	public static class DateRange {

		private Date start;

		private Date end;

		private DateRange(String range, String split, String startPattern,
				String endPattern) {
			String[] values = range.split(split);
			String startStr = values[0], endStr = values[1];
			start = toDate(startStr, startPattern);
			end = toDate(endStr, endPattern);
		}

		private DateRange() {

		}

		public Date getStart() {
			return start;
		}

		public Date getEnd() {
			return end;
		}

		public String toString() {
			if (diff(start, end, Calendar.DATE) == 0) {
				return DateTimeUtil.toString(start, "yyyy-MM-dd HH:mm") + "--"
						+ DateTimeUtil.toString(end, "HH:mm");
			}
			return DateTimeUtil.toString(start, "yyyy-MM-dd HH:mm") + "--"
					+ DateTimeUtil.toString(end, "yyyy-MM-dd HH:mm");
		}

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
		if (TextUtils.isEmpty(date)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(date);
	}

	public static Date toDate(String date) {
		try {
			return toDateWithException(date, defaultPattern);
		} catch (Exception e) {
			try {
				return toDateWithException(date, "dd/MM/yyyy");
			} catch (Exception e1) {
				return toDate(date, "yyyy/MM/dd");
			}
		}
	}

	public static Date cast(Object obj) {
		if (obj instanceof Date) {
			return (Date) obj;
		}
		return null;
	}

	/**
	 * 比较两个时间的差异
	 * 
	 * @param start
	 *            起始时间
	 * @param end
	 *            结束时间
	 * @param value
	 *            时间单位
	 * @return
	 */
	public static long diff(Date start, Date end, int value) {
		if (start == null || end == null) {
			return 0;
		}
		return (end.getTime() - start.getTime()) / factor(value);
	}

	private static long factor(int value) {
		switch (value) {
		case Calendar.SECOND:
			return 1000;
		case Calendar.MINUTE:
			return 60 * 1000;
		case Calendar.HOUR:
			return 60 * 60 * 1000;
		case Calendar.DATE:
			return 24 * 60 * 60 * 1000;
		case Calendar.YEAR:
			return 365 * 24 * 60 * 60 * 1000;
		}
		return 0;
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
		return toString(date, defaultPattern);
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

	/**
	 * 根据格式化字符串获取当前系统时间字符串
	 * 
	 * @param pattern
	 * @return
	 */
	public static String now(String pattern) {
		return toString(new Date(), pattern);
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取yyyy-MM-dd类型的DATE
	 * 
	 * @return
	 */
	public static Date getShortNowDate() {
		return toDate(now(defaultPattern));
	}

	/**
	 * 获取yyyy-MM-dd类型的DATE
	 * 
	 * @return
	 */
	public static Date getShortNowDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return toDate(toString(cal.getTime(), defaultPattern));
	}

	public static String now() {
		return now(defaultPattern);
	}

	public static int year(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static int year() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int month(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int month() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int dayOfMonth(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int dayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int hour(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int hour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static DateRange range(String range, String split,
			String startPattern, String endPattern) {
		return new DateRange(range, split, startPattern, endPattern);
	}

	/**
	 * 以String形式表示时间区间，如果起始时间与结束时间在同一天，省略第二个时间的年月日
	 * <p>
	 * 例如：2014-09-08 09:20:00 2014-09-08 10:00:00 将表示为 2014-09-08
	 * 09:20:00--10:00:00
	 * </p>
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static String toString(Date start, Date end) {
		if (start == null || end == null)
			return null;
		DateRange d = new DateRange();
		d.start = start;
		d.end = end;
		return d.toString();
	}

	/**
	 * 计算两个时间相差的分钟
	 * 
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 * @see DateTimeUtil#diff(Date, Date, int)
	 */
	@Deprecated
	public static String dateDiffMins(Date dateBegin, Date dateEnd) {
		long mins = 0;
		if (dateBegin != null && dateEnd != null) {
			long timeMillis = dateEnd.getTime() - dateBegin.getTime();
			mins = timeMillis / 1000 / 60; // 相差分钟数
		}
		return mins + "";
	}

	public static String timeNo() {
		String num = "";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		num = sdf.format(date);
		return num;
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @param dt1
	 * @param dt2
	 * @return 如果dt1在dt2之前，返回1；如果dt1在dt2之后，返回-1；如果dt1等于dt2，返回0。
	 */
	public static int compare(Date dt1, Date dt2) {
		try {
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static Date dateDiff(Date date, int diff) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.add(Calendar.DATE, diff);
		return cal.getTime();
	}

	private static final String MAX_INTERVAL_TAG = "3个月前";

	private static final String MONTH_INTERVAL_TAG = "个月前";

	private static final String WEEK_INTERVAL_TAG = "周前";

	private static final String DAY_INTERVAL_TAG = "天前";

	private static final String HOUR_INTERVAL_TAG = "小时前";

	private static final String MINITE_INTERVAL_TAG = "分钟前";

	/**
	 * 
	 * @Description: 获取传入时间与当前时间的时间差字符串
	 * @Title: getTimeIntervalString
	 * @param timeString
	 *            要比较的时间字符串（格式:yyyy-MM-dd HH:mm:ss）
	 * @return
	 */
	public static String getTimeIntervalString(String timeString) {
		String intervalString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String bigStr = sdf.format(new Date());
		try {
			Date big = sdf.parse(bigStr);
			Date small = sdf.parse(timeString);
			// 分钟间隔
			long mInterval = (big.getTime() - small.getTime()) / (1000 * 60);
			// 小时间隔
			long hInterval = mInterval / 60;
			// 天数间隔
			long dInterval = hInterval / 24;
			// 周数间隔
			long wInterval = dInterval / 7;
			// 月数间隔
			long monInterval = dInterval / 30;
			if (monInterval >= 3)// 大于3个月
			{
				intervalString = MAX_INTERVAL_TAG;
			} else// 小于3个月
			{
				if (monInterval > 0) {
					intervalString = monInterval + MONTH_INTERVAL_TAG;
				} else {
					if (wInterval > 0) {
						intervalString = wInterval + WEEK_INTERVAL_TAG;
					} else {
						if (dInterval > 0) {
							intervalString = dInterval + DAY_INTERVAL_TAG;
						} else {
							if (hInterval > 0) {
								intervalString = hInterval + HOUR_INTERVAL_TAG;
							} else {
								if (mInterval > 0) {
									intervalString = mInterval
											+ MINITE_INTERVAL_TAG;
								} else// 如果小于一分钟则返回1分钟前（即：只精确到分钟级别）
								{
//									intervalString = 1 + MINITE_INTERVAL_TAG;
									intervalString = "刚刚";
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			intervalString = "";
			e.printStackTrace();
		}

		return intervalString;
	}

	/**
	 * 
	 * @Description: 时间与当前时间比较
	 * @Title: compareDate
	 * @param timeStr
	 *            要比较的时间字符串（格式:yyyy-MM-dd）
	 * @return
	 */
	public static boolean compareDate(String timeStr) {
		if (Tool.isEmpty(timeStr)) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowStr = sdf.format(new Date());
		try {
			Date nowDt = sdf.parse(nowStr);
			Date timeDt = sdf.parse(timeStr);
			if (timeDt.getTime() >= nowDt.getTime()) {
				return true;
			}

			return false;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @Description: 时间增加M月D日
	 * @Title: addDateTime
	 * @param str
	 *            初始时间字符串（格式:yyyy-MM-dd）
	 * @return
	 */
	public static String addDateTime(String str, Integer month, Integer day) {
		return addDateTime("yyyy-MM-dd", str, month, day);
	}

	/**
	 * 
	 * @Description: 时间增加M月D日
	 * @Title: addDateTime
	 * @param str
	 *            初始时间字符串（格式:yyyy-MM-dd）
	 * @return
	 */
	public static String addDateTime(String format, String str, Integer month,
			Integer day) {
		String retStr = "";
		if (Tool.isEmpty(str)) {
			return retStr;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dt = null;
		try {
			dt = sdf.parse(str);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			if (month != null && month > 0) {
				rightNow.add(Calendar.MONTH, month);// 日期加M个月
			}
			if (day != null && day > 0) {
				rightNow.add(Calendar.DAY_OF_YEAR, day);// 日期加D天
			}
			Date nowdt = rightNow.getTime();
			retStr = sdf.format(nowdt);
		} catch (ParseException e) {
		}
		return retStr;
	}

	public static String timeFormat(long timeMillis, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		return format.format(new Date(timeMillis));
	}

	public static String formatPhotoDate(long time) {
		return timeFormat(time, "yyyy-MM-dd");
	}

	public static String formatPhotoDate(String path) {
		File file = new File(path);
		if (file.exists()) {
			long time = file.lastModified();
			return formatPhotoDate(time);
		}
		return "1970-01-01";
	}
	

    public static boolean isEarly(int days, long time) {
        return (currentTimeMillis() - time) > (days * 24 * 3600 * 1000);
    }

    public static int currentTimeSecond() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long[] getTsTimes() {
        long[] times = new long[2];

        Calendar calendar = Calendar.getInstance();

        times[0] = calendar.getTimeInMillis() / 1000;

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        times[1] = calendar.getTimeInMillis() / 1000;

        return times;
    }

    public static String getFormatDatetime(int year, int month, int day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new GregorianCalendar(year, month, day).getTime());
    }

    public static Date getDateFromFormatString(String formatDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getNowDatetime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return (formatter.format(new Date()));
    }

    public static int getNow() {
        return (int) ((new Date()).getTime() / 1000);
    }

    public static String getNowDateTime(String format) {
        Date date = new Date();

        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(date);
    }

    public static String getDateString(long milliseconds) {
        return getDateTimeString(milliseconds, "yyyyMMdd");
    }

    public static String getTimeString(long milliseconds) {
        return getDateTimeString(milliseconds, "HHmmss");
    }

    public static String getBeijingNowTimeString(String format) {
        TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");

        Date date = new Date(currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        formatter.setTimeZone(timezone);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeZone(timezone);
        String prefix = gregorianCalendar.get(Calendar.AM_PM) == Calendar.AM ? "上午" : "下午";

        return prefix + formatter.format(date);
    }

    public static String getBeijingNowTime(String format) {
        TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");

        Date date = new Date(currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        formatter.setTimeZone(timezone);

        return formatter.format(date);
    }

    public static String getDateTimeString(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(date);
    }


    public static String getFavoriteCollectTime(long milliseconds) {
        String showDataString = "";
        Date today = new Date();
        Date date = new Date(milliseconds);
        Date firstDateThisYear = new Date(today.getYear(), 0, 0);
        if (!date.before(firstDateThisYear)) {
            SimpleDateFormat dateformatter = new SimpleDateFormat("MM-dd", Locale.getDefault());
            showDataString = dateformatter.format(date);
        } else {
            SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            showDataString = dateformatter.format(date);
        }
        return showDataString;
    }

    public static String getTimeShowString(long milliseconds, boolean abbreviate) {
        String dataString;
        String timeStringBy24;

        Date currentTime = new Date(milliseconds);
        Date today = new Date();
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date todaybegin = todayStart.getTime();
        Date yesterdaybegin = new Date(todaybegin.getTime() - 3600 * 24 * 1000);
        Date preyesterday = new Date(yesterdaybegin.getTime() - 3600 * 24 * 1000);

        if (!currentTime.before(todaybegin)) {
            dataString = "今天";
        } else if (!currentTime.before(yesterdaybegin)) {
            dataString = "昨天";
        } else if (!currentTime.before(preyesterday)) {
            dataString = "前天";
        } else if (isSameWeekDates(currentTime, today)) {
            dataString = getWeekOfDate(currentTime);
        } else {
            SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dataString = dateformatter.format(currentTime);
        }

        SimpleDateFormat timeformatter24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeStringBy24 = timeformatter24.format(currentTime);

        if (abbreviate) {
            if (!currentTime.before(todaybegin)) {
                return getTodayTimeBucket(currentTime);
            } else {
                return dataString;
            }
        } else {
            return dataString + " " + timeStringBy24;
        }
    }

    /**
     * 根据不同时间段，显示不同时间
     *
     * @param date
     * @return
     */
    public static String getTodayTimeBucket(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat timeformatter0to11 = new SimpleDateFormat("KK:mm", Locale.getDefault());
        SimpleDateFormat timeformatter1to12 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 5) {
            return "凌晨 " + timeformatter0to11.format(date);
        } else if (hour >= 5 && hour < 12) {
            return "上午 " + timeformatter0to11.format(date);
        } else if (hour >= 12 && hour < 18) {
            return "下午 " + timeformatter1to12.format(date);
        } else if (hour >= 18 && hour < 24) {
            return "晚上 " + timeformatter1to12.format(date);
        }
        return "";
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    public static boolean isSameDay(long time1, long time2) {
        return isSameDay(new Date(time1), new Date(time2));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    /**
     * 判断两个日期是否在同一周
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
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    public static long getSecondsByMilliseconds(long milliseconds) {
        long seconds = new BigDecimal((float) ((float) milliseconds / (float) 1000)).setScale(0,
                BigDecimal.ROUND_HALF_UP).intValue();
        // if (seconds == 0) {
        // seconds = 1;
        // }
        return seconds;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else retStr = "" + i;
        return retStr;
    }

    public static String getElapseTimeForShow(int milliseconds) {
        StringBuilder sb = new StringBuilder();
        int seconds = milliseconds / 1000;
        if (seconds < 1)
            seconds = 1;
        int hour = seconds / (60 * 60);
        if (hour != 0) {
            sb.append(hour).append("小时");
        }
        int minute = (seconds - 60 * 60 * hour) / 60;
        if (minute != 0) {
            sb.append(minute).append("分");
        }
        int second = (seconds - 60 * 60 * hour - 60 * minute);
        if (second != 0) {
            sb.append(second).append("秒");
        }
        return sb.toString();
    }
}

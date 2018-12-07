package com.app.tanklib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式工具类
 * 
 * @author jie.li
 * 
 */
public class TimeUtil {
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public static String getNowDay() {
		return format.format(new Date());
	}
	public static String getNowDay1() {
		return format1.format(new Date());
	}

	/**
	 * 比较两个日期的年月日，忽略时分秒。
	 * 
	 * @param d1
	 * @param d2
	 * @return 如果d1晚于d2返回大于零的值，如果d1等于d2返回0，否则返回一个负值。
	 */
	public static int dateCompare(Date d1, Date d2) {
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, c.get(Calendar.YEAR));
		c2.set(Calendar.MONTH, c.get(Calendar.MONTH));
		c2.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
		Date date0 = c2.getTime();

		c.setTime(d2);
		c2.set(Calendar.YEAR, c.get(Calendar.YEAR));
		c2.set(Calendar.MONTH, c.get(Calendar.MONTH));
		c2.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
		Date date1 = c2.getTime();

		return date0.compareTo(date1);
	}
	
	public static boolean isAfter(String s, int n) {
		try {
			Date d = format1.parse(s);
			if (d.getTime() - System.currentTimeMillis() > n * 1000) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 返回固定格式的时间
	 * 
	 * @param timeMills
	 * @param formatType
	 * @return
	 */
	public static String getFormatTimeByTimeMillis(long timeMills,
			String formatType) {
		Date date = new Date();
		date.setTime(timeMills);
		SimpleDateFormat formatter = new SimpleDateFormat(formatType,
				Locale.getDefault());
		String formatTime = formatter.format(date);
		return formatTime;
	}

	/**
	 * 根据固定格式返回时间戳
	 * 
	 * @param timeString
	 * @param formatType
	 * @return
	 */
	public static long getTimestampByTime(String timeString, String formatType) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType,
				Locale.getDefault());
		try {
			Date d = formatter.parse(timeString);
			return d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 判断时间戳是否在今一天
	 * 
	 * @param timestamp
	 * @return
	 */
	public static boolean isToday(long timestamp) {
		String timeString = getFormatTimeByTimeMillis(timestamp, "yyyy-MM-dd");
		String todayString = getFormatTimeByTimeMillis(
				System.currentTimeMillis(), "yyyy-MM-dd");
		return timeString.equals(todayString);
	}

	/**
	 * 获取本周星期一的日期
	 * 
	 * @return
	 */
	public static String getMondayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return getFormatTimeByTimeMillis(c.getTimeInMillis(), "yyyy-MM-dd");
	}

	/**
	 * 获取本月第一天的日期
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.DAY_OF_MONTH, 1);
		return getFormatTimeByTimeMillis(c.getTimeInMillis(), "yyyy-MM-dd");
	}
}

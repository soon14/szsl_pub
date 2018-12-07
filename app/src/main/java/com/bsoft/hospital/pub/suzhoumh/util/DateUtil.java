package com.bsoft.hospital.pub.suzhoumh.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 时间工具类 除了私信使用getMessageTime外，其他都使用getDateTime方法
 * 
 * 传过来的long都是UTC时间
 * 
 * @author
 */
public class DateUtil {

	public static String tmepReturnStr = "不祥";
	public static DateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static DateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
	public static DateFormat format2 = new SimpleDateFormat("yyyy年MM月");
	public static DateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat format4 = new SimpleDateFormat("MM-dd");
	public static java.util.Calendar cal = java.util.Calendar.getInstance();

	public static String getDateTime(long d) {
		String result = "";
		if (d > 0) {
			result = getString(new Date(d));
		}
		return result;
	}
	
//	/**
//	 * UTC时间
//	 * 
//	 * @param d
//	 * @return
//	 */
//	public static String getDateTime(long d) {
//		String result = "";
//		if (d > 0) {
//			result = getString(new Date(d
//					+ cal.get(java.util.Calendar.ZONE_OFFSET)));
//		}
//		return result;
//	}

	public static int getAge(long bityhday) {
		Date d1 = new Date(bityhday + cal.get(java.util.Calendar.ZONE_OFFSET));
		Date d2 = new Date();
		return d2.getYear() - d1.getYear();
	}

	public static long getTime(String s) {
		try {
			Date d1 = format3.parse(s);
			return d1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 无法选择未来时间，≤now
	public static boolean check(String d) {
		try {
			Date d1 = format3.parse(d);
			Date d2 = format3.parse(format3.format(new Date()));
			return d2.getTime() >= d1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 无法选择未来时间，≤now
	public static boolean check1(String d) {
		try {
			Date d1 = format3.parse(d);
			Date d2 = format3.parse(format3.format(new Date()));
			return d2.getTime() < d1.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getDateTime1(long d) {
		String result = "";
		if (d > 0) {
			result = getString(new Date(d));
		}
		return result;
	}

	// 传过来的不是UTC时间
	public static String getNewHongDongDay(long d) {
		return format1.format(new Date(d));
	}

	// 返回当前的UTC时间
	public static long getNowTime() {
		return System.currentTimeMillis()
				- cal.get(java.util.Calendar.ZONE_OFFSET);
	}

	public static long getUtcTimeByStr(String str) {
		try {
			return format3.parse(str).getTime()
					- cal.get(java.util.Calendar.ZONE_OFFSET);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getBirthDateTime(long d) {
		return format3.format(new Date(d
				+ cal.get(java.util.Calendar.ZONE_OFFSET)));
	}

	public static String getDateTime(DateFormat format, long d) {
		return format.format(new Date(d));
	}

	public static String getDateTime(String format, long d) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(d));
	}

	public static String dateFormate(Date date, String formate) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(formate);
			return sdf.format(date);
		} else {
			return null;
		}

	}

	// public static Date getSodaoDateByString(String d) throws ParseException {
	// format.setTimeZone(TimeZone.getTimeZone("UTC"));
	// return format.parse(d);
	// }

	/**
	 * 0秒-60秒内显示“刚刚” 1分钟-2分钟内显示“1分钟前” 1小时-2小时内显示“1小时前” 1天-2天内显示“1天前”
	 * 7天-14天显示“1周前” 30天-60天显示1月前 之后已月类推计算 ）
	 */
	public static String getString(Date date) {
		long curTime = System.currentTimeMillis();

		long diffSecond = 0L;
		long diffDay = 0L;
		diffSecond = (curTime - date.getTime()) / 1000L;

		if (diffSecond >= 86400) {// 两个相差
			Calendar curDate = new GregorianCalendar();
			curDate.setTime(new Date(curTime));
			curDate.set(Calendar.HOUR_OF_DAY, 23);
			curDate.set(Calendar.MINUTE, 59);
			curDate.set(Calendar.SECOND, 59);
			curDate.set(Calendar.MILLISECOND, 999);
			diffDay = (curDate.getTimeInMillis() - date.getTime()) / 86400000L;
			if (diffDay <= 30) {
				return diffDay + "天前";
			} else {
				if (date.getYear() == new Date().getYear()) {
					return format4.format(new Date(date.getTime()
							+ cal.get(java.util.Calendar.ZONE_OFFSET)));
				} else {
					return getBirthDateTime(date.getTime());
				}
			}
		} else {
			if (diffSecond < 0) {
				return "";
			} else if (diffSecond < 60) {
				return "刚刚";
			} else if (diffSecond < 3600) {
				return diffSecond / 60 + "分钟前";
			} else if (diffSecond < 86400) {
				return diffSecond / 3600 + "小时前";
			} else if (diffSecond < 86400) {
				return diffSecond / 3600 + "小时前";
			}
		}
		return "...";
	}

	public static long getDayTime(String d) {
		try {
			return format1.parse(d).getTime()
					- cal.get(java.util.Calendar.ZONE_OFFSET);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getMonthTime(String d) {
		try {
			return format2.parse(d).getTime()
					- cal.get(java.util.Calendar.ZONE_OFFSET);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取当前是星期几
	 * @param d
	 * @return
	 */
	public static String getWeek(String d)
	{
		String week = "";
		try
		{
			Date date = format3.parse(d);
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			week = sdf.format(date);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return week;
	}

	/**
	 * 是否是上午
	 * @return
	 */
	public static Boolean isAM(String time)
	{
		Boolean state = true;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(time);
			GregorianCalendar ca = new GregorianCalendar();  
			ca.setTime(date);
			if(ca.get(GregorianCalendar.AM_PM)==0)//0是上午，1是下午
			{
				state = true;
			}
			else
			{
				state = false;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return state;
	}
	
	/**
     * 根据日期获得所在周的日期 
     * @param mdate
     * @return
     */
    public static List<Date> getdateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        //Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 0; a <7; a++) {
            fdate = new Date();
            fdate.setTime(mdate.getTime() + (a * 24 * 3600000));
            list.add(a, fdate);
        }
        return list;
    }
	
	public static String dateFormate(String time, String formate) {
		String date = "";
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(formate);
			date = sdf.format(sdf.parse(time));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return date;
	}
	
	/**
	 * 如果是当天就返回时间，如果是以前就返回日期和时间
	 * @param d
	 * @return
	 */
	public static String getDayAndTime(long d)
	{
		String time;
		Date date = new Date(d);
		long curTime = System.currentTimeMillis();

		long diffSecond = 0L;
		long diffDay = 0L;
		diffSecond = (curTime - date.getTime()) / 1000L;
		if (diffSecond >= 86400)//超过一天
		{
			time = getDateTime("MM-dd", d);
		}
		else//当天
		{
			time = getDateTime("HH:mm",d);
		}
		return time;
	}

	/**
	 * 获取两个日期之间的天数之差
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String smdate,String bdate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }

	/**
	 * 获取当前时间的前一天时间
	 * @param cl
	 * @return
	 */
	public static String getBeforeDay(Calendar cl){
		//使用roll方法进行向前回滚
		//cl.roll(Calendar.DATE, -1);
		//使用set方法直接进行设置
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day - 1);
		Date now = cl.getTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sf.format(now);
		return date;
	}

	/**
	 * 判断是否是今天
	 * @param time
	 * @return
	 */
	public static boolean isToday(String time){
		boolean b = false;
		Date today = new Date();
		if(time != null){
			String timeDate = dateFormate(time, "yyyy-MM-dd");
			String nowDate = dateFormate(today, "yyyy-MM-dd");
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
}

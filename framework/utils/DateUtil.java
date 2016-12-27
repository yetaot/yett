package framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;



import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;

import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;

public class DateUtil {
	private final static Logger logger = LogUtil.getInstance(DateUtil.class);

	public final static String diagonalFormatDefault = "MM/dd/yyyy";
	public final static String diagonalFormatAll ="MM/dd/yyyy hh:mm:ss";
	public final static String diagonalFormatExceptS ="MM/dd/yyyy hh:mm";
	
	public final static String FORMAT_TYPE_ALL="yyyy-MM-dd HH:mm:ss";

	//add by jcy 20121019
	public final static String LONG_PATTERN="yyyyMMddHHmm";
	//add by jcy 20130616
	public final static String SHORT_PATTERN="yyyyMMdd";
	public final static String FULL_PATTERN = "yyyyMMddHHmmss";
	//add by jcy 20121207
	public final static String TIME_PATTERN="HH:mm"; 
	public final static String DEFAULT_PATTERN="yyyy-MM-dd";
	public final static String SHORT_TIME_PATTERN="yyyy-MM-dd HH:mm";
	
	public final static String REPORT_DATE_PATTERN="yyyy-MM-dd HH";
	
	public final static String URL_DATE_PATTERN = "yyyy/MM/dd";
	
	public final static String FORMAT_YEAR_PATTERN = "yyyy";
	public final static String FORMAT_MONTH_PATTERN = "MM";
	public final static String FORMAT_DAY_PATTERN = "dd";
	public final static String FORMAT_HOUR_PATTERN = "HH";
	/**
	 * 格式化日期到字符串
	 * @param date Date
	 * @param formatStr 格式化字符串
	 * @return
	 */
	public static String dateFormat(Date date,String formatStr){
		SimpleDateFormat formater=null;
		try{
			 formater=new SimpleDateFormat(formatStr);
			
		}catch(Exception e){
			ExceptionHandler.throwRuntimeException(e, "日期格式转换错误", logger);
		}
		return formater.format(date);
	}
	
		/**
		 * add by jcy 20121207
		 * 获取当期日期是一个星期中的某天
		 * @return
		 */
		public static int dayOfWeek(){
			return dayOfWeek(new Date());
		}
		
		/**
		 * add by jcy 20121207
		 * 根据日期获取当期日期是一个星期中的某天
		 * 星期一 星期二 星期三 ...星期日 1 2 3 ...7
		 * @param date
		 * @return
		 */
		public static int dayOfWeek(Date date){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
	        if(week<0) week =7;
	        return week;
		}
		/**
		 * add by jcy 20121020
		 * 将yyyy-MM-dd HH 格式转换为 yyyy-MM-dd HH:mm:ss
		 * @param date
		 * @return
		 */
		public static String beginHour(String date){
			return date.trim()+":00:00";
		}
		/**
		 * add by jcy 20121022
		 * 将yyyy-MM-dd HH格式转换为 yyyy-MM-dd HH:mm:ss.999
		 * @param date
		 * @return
		 */
		public static String endHour(String date){
			return date.trim()+":59:59.999";
		}
		/**
		 * add by jcy 20121124
		 * 将yyyy-MM-dd 格式转换为 yyyy-MM-dd HH:mm:ss
		 * @param date
		 * @return
		 */
		public static String endTime(String date){
			return date.trim()+" 23:59:59";
		}
		
		public static String timeFomart(long duration){
			
			int hour =(int) (duration/(1000*60*60));
			int  time =(int)duration-hour*(1000*60*60);
			int minute =(int)time/(1000*60);
			time = time-minute*(1000*60);
			int second =(int)time/1000;
			time = time-second*1000;
			String result = StringUtil.leftJoin(hour, 2, "0")+":"+StringUtil.leftJoin(minute, 2, "0")+":"+StringUtil.leftJoin(second, 2, "0");	
			return result;
		}
		public static Long timeToLong(String time){
			try{
				String[] timeArray=time.split(":");
				Long hour =1000*60*60*Long.parseLong(timeArray[0]);
				Long minute =1000*60*Long.parseLong(timeArray[1]);
				Long second =1000*Long.parseLong(timeArray[2]);
				return hour+minute+second;
			}catch(Exception e){
				return 0L;
			}
		}
		/**
		 * 分钟转为毫秒
		 * @param time
		 * @return
		 */
		public static Long minute2Millisecond(String time){
			try{
				Long m=Long.parseLong(time);
				return m*60*1000;
			}catch(Exception e){
				return 0L;
			}
		}
		public static Long timeToSec(String time){
			try{
				String[] timeArray=time.split(":");
				Long hour =60*60*Long.parseLong(timeArray[0]);
				Long minute =60*Long.parseLong(timeArray[1]);
				Long second =Long.parseLong(timeArray[2]);
				return hour+minute+second;
			}catch(Exception e){
				return 0L;
			}
		}
	
	/** 
	 * 将字符串转化为Date
	 * @param date字符串
	 * @param formatStr 字符串格式
	 * @return
	 */
	public static Date getDate(String date,String formatStr){
		try{
			SimpleDateFormat formater=new SimpleDateFormat(formatStr);
			return formater.parse(date);
		}catch(Exception e){
			return new Date();
		}
	}
	
	/**
	 * zkevin 将yyyy-MM-dd格式转换为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String beginDay(String date) {
		return date.trim() + " 00:00:00";
	}

	/**
	 * zkevin 将yyyy-MM-dd格式转换为 yyyy-MM-dd HH:mm:ss.999
	 * 
	 * @param date
	 * @return
	 */
	public static String endDay(String date) {
		return date.trim() + " 23:59:59.999";
	}
	
	
	public static boolean isMatchDate(String dateStr){
		String datePattern = "^(?:([0-9]{4}-(?:(?:0?[1,3-9]|1[0-2])-(?:29|30)|"
            + "((?:0?[13578]|1[02])-31)))|"
            + "([0-9]{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|"
            + "(((?:(\\d\\d(?:0[48]|[2468][048]|[13579][26]))|"
            + "(?:0[48]00|[2468][048]00|[13579][26]00))-0?2-29)))$";
		Pattern p = Pattern.compile(datePattern);
		return p.matcher(dateStr).matches();
	}
	/**
	 * 将传入的日期+-指定的月份，即变更时间的月份
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date changeMonth(Date date,int month){
		Calendar cal = Calendar.getInstance();
		cal.setTime(null!=date?date:new Date());
		cal.add(Calendar.MONTH,month);
		return cal.getTime();
	}
	
	public static long diff(String end, String start) {
		DateFormat df = new SimpleDateFormat(FORMAT_TYPE_ALL);	
		long diff = 0;
		try {
			Date de = df.parse(end);
			Date ds = df.parse(start);
			diff = (de.getTime() - ds.getTime()) / 1000;
		} catch (Exception e) {
			ExceptionHandler.throwRuntimeException(e, logger);
		}
		return diff;
	}
	public static Long second2Millisecond(String time) {
	    try{
		Long m=Long.parseLong(time);
		return m*1000;								}catch(Exception e){								return 0L;
	    }
	}
	
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) {
    	smdate = getDate(dateFormat(smdate, DEFAULT_PATTERN), DEFAULT_PATTERN);
    	bdate = getDate(dateFormat(bdate, DEFAULT_PATTERN), DEFAULT_PATTERN);
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
       return Integer.parseInt(String.valueOf(between_days));
    }
}

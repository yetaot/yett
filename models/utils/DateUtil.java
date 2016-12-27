package models.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;
import framework.utils.StringUtil;

public class DateUtil {
	private static final Logger logger = LogUtil.getInstance(DateUtil.class);
	
	/**
	 * 将传入的日期+-指定的日，即变更时间的日期
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date changeDay(Date date,int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(null!=date?date:new Date());
		cal.add(Calendar.DAY_OF_MONTH,day);
		return cal.getTime();
	}
	
	/**
	 * @param dateStr 
	 * @param type
	 * @return 格式为 yyyy-MM-dd HH:mm
	 */
	public static String formatEndDateStr(String dateStr, int type){
		String str = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try{
			if(!StringUtil.isBlank(dateStr)){
				if(type == 3){
					str = dateStr + " 23:59";
				}else if(type == 2){
					DateFormat df = new SimpleDateFormat("yyyy-MM");
					Calendar cal = Calendar.getInstance();
					cal.setTime(df.parse(dateStr));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					str = format.format(cal.getTime())+" 23:59";
				}else if(type == 1){
					DateFormat df = new SimpleDateFormat("yyyy");
					Calendar cal = Calendar.getInstance();
					cal.setTime(df.parse(dateStr));
					cal.set(Calendar.MONTH, cal.getMaximum(Calendar.MONTH));
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					str = format.format(cal.getTime())+" 23:59";
				}
			}
		}catch(Exception e){
			ExceptionHandler.throwRuntimeException(e, logger);
		}
		return str;
	}
	/**
	 * @param dateStr
	 * @param type
	 * @return
	 */
	public static String formatBeginDateStr(String dateStr, int type){
		String str = null;
		try{
			if(!StringUtil.isBlank(dateStr)){
				if(type == 3){
					str = dateStr + " 00:00";
				}else if(type == 2){
					str = dateStr+"-01 00:00";
				}else if(type == 1){
					str = dateStr + "-01-01 00:00";
				}
			}
		}catch(Exception e){
			ExceptionHandler.throwRuntimeException(e, logger);
		}
		return str;
	}
	
	public static String formatDateString(Date date,String format){
		SimpleDateFormat df=new SimpleDateFormat(format);  
		String time=df.format(date);  
		System.out.println(time);  
		return time;
	}
}

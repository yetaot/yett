package framework.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import framework.base.Constant;
import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;

public class UrlUtil {
	
	private final static Logger logger = LogUtil.getInstance(UrlUtil.class);

	public static String toUrl(Object o,Class clazz){
		Field[] fields = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer("?");
		for(int i=0;i<fields.length;i++){
			Field field = fields[i];
			String fieldName = field.getName();
			String value="";
			try {
				value =StringUtil.trim(String.valueOf(field.get(o)));
				if(StringUtil.isPresent(value)){
					if(i!=0){
						sb.append("&");
					}
					sb.append(Constant.PREFIX_PAGE_PARAM_NAME+fieldName+"="+URLEncoder.encode(value, "utf-8"));
				}
			} catch (IllegalArgumentException e) {
				ExceptionHandler.throwRuntimeException(e, "UrlUtil转换URL错误", logger);
			} catch (IllegalAccessException e) {
				ExceptionHandler.throwRuntimeException(e, "UrlUtil转换URL错误,对象字段不可访问", logger);
			} catch (UnsupportedEncodingException e) {
				ExceptionHandler.throwRuntimeException(e, "UrlUtil转换URL错误,参数值编码错误", logger);
				
			}
			
		}
		return sb.toString();
	}
}

package framework.utils;

import java.lang.reflect.Field;
import java.util.List;

import framework.exceptions.ExceptionHandler;

public class ReflectUtil {
	/**
	 * 反射取值
	 * @param obj
	 * @param column
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public static Object getByColumn(Object obj,String column){
		Field field;
		Object value=new Object();
		try {
			field = obj.getClass().getField(column);
			value=field.get(obj);
		}catch(Exception e) {
			ExceptionHandler.throwRuntimeException(e,"反射取值失败");
		}
		return value;
	}
	
	/**
	 * 为字段反射赋值
	 * @param obj
	 * @param column
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public static Object setByColumn(Object obj,String column,Object value){
		Field field;
		try {
			field = obj.getClass().getField(column);
			field.set(obj,value);
		}catch(Exception e) {
			ExceptionHandler.throwRuntimeException(e,"反射赋值失败");
		}
		return obj;
	}
	/**
	 * 反射取值,从传入对象的父类
	 * @param obj
	 * @param column
	 * @return
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public static Object getByColumnFromSuper(Object obj,String column){
		Field field;
		Object value=new Object();
		try {
			field = obj.getClass().getSuperclass().getField(column);
			value=field.get(obj);
		}catch(Exception e) {
			ExceptionHandler.throwRuntimeException(e,"从对象父类反射取值失败");
		}
		return value;
	}
	/**
	 * 将传入对象转换为指定class的对象
	 * @param obj
	 * @param paser
	 * @return
	 */
	public static Object parseTo(Object obj,Class paser){
		Object newValue=null;
		if(paser.equals(Long.class)){
			newValue=Long.parseLong(String.valueOf(obj));
		}else if(paser.equals(String.class)){
			newValue=String.valueOf(obj);
		}else if(paser.equals(Integer.class)){
			newValue=Integer.parseInt(String.valueOf(obj));
		}else if(paser.equals(Boolean.class)){
			newValue=Boolean.parseBoolean(String.valueOf(obj));
		}else{
			ExceptionHandler.throwRuntimeException(new RuntimeException(),"尝试将"+obj.getClass()+"转换为"+paser+"失败");
		}
		return newValue;
	} 
}

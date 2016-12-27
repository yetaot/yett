package framework.utils;

import java.util.HashSet;
import java.util.Set;

import play.Play;

public class ContextUtil {

	public static String getProperty(String key){
		return Play.configuration.getProperty(key);
	}
	
	public static String getProperty(String key,String defaultValue){
	    return Play.configuration.getProperty(key, defaultValue);
	}
	
	public static boolean contains(Object value){
		return Play.configuration.contains(value);
	}
	
	public static boolean containsKey(Object key){
		return Play.configuration.containsKey(key);
	}
	
	public static boolean containsValue(Object value){
		return Play.configuration.containsValue(value);
		
	}
	
	public static Set keySet(){
		return Play.configuration.keySet();
	}
	
	public static Set<String> grep(String prefix){
		Set<String> result = new HashSet<String>();
		Set keys = Play.configuration.keySet();
		for(Object key : keys){
			if(StringUtil.trim(key).startsWith(prefix)){
				result.add(StringUtil.trim(key));
			}
		}
		return result;
	}
	

	
}

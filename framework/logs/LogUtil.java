package framework.logs;

import org.apache.log4j.Logger;

public class LogUtil {
	
	public static Logger getInstance(String name){
		return Logger.getLogger(name);
	}
	
	public static Logger getInstance(Class clazz){
		return Logger.getLogger(clazz);
	}

	
	
	

	
}

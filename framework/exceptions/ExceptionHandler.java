package framework.exceptions;

import org.apache.log4j.Logger;

import framework.logs.LogUtil;

/**
 * 通用异常处理
 * @author jinchaoyang
 *
 */
public class ExceptionHandler {
	private static final Logger logger=LogUtil.getInstance(ExceptionHandler.class);
	/**
	 * 将应用程序异常作为运行时异常抛出
	 * @param e
	 * @param message
	 */
	public static void throwRuntimeException(Exception e, String message) {
		logger.error(message,e);
		throw new BaseRuntimeException(message, e);
	}
	
	public static void throwRuntimeException(Exception e, String message,Logger logger) {
		logger.error(message,e);
		throw new BaseRuntimeException(message, e);
	}
	
	public static void throwRuntimeException(Exception e,Logger logger) {
		logger.error(e.getMessage());
		throw new BaseRuntimeException(e.getMessage(), e);
	}

}

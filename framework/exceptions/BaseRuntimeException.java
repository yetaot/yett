package framework.exceptions;

/**
 * 基础的运行时异常类
 * @author jinchaoyang
 *
 */
public class BaseRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//错误码
	private String errorCode;
	
	public BaseRuntimeException(){
		super();
	}
	
	public BaseRuntimeException(String message){
		super(message);
	}
	
	public BaseRuntimeException(String message,Throwable cause){
		super(message,cause);
	}
	
	public BaseRuntimeException(Throwable cause){
		super(cause);
	}
	
	public BaseRuntimeException(String errorCode,String message){
	    this(message);
	    this.errorCode = errorCode;
	}
	
	public BaseRuntimeException(String errorCode,String message,Throwable cause){
		this(message,cause);
		this.errorCode = errorCode;
	}
	
	//获取错误码
	public String getErrorCode(){
		return this.errorCode;
	}
	

}

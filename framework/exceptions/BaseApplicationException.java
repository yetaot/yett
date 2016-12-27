package framework.exceptions;


/**
 * 应用程序的业务异常的基类
 * @author jinchaoyang
 *
 */
public abstract class BaseApplicationException extends Exception {
	public BaseApplicationException() {
	}

	public BaseApplicationException(String arg0) {
		super(arg0);
	}

	public BaseApplicationException(Throwable arg0) {
		super(arg0);
	}

	public BaseApplicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}

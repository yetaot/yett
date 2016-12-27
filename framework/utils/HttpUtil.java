package framework.utils;

import framework.exceptions.ExceptionHandler;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

/**
 * 
 * @author 张科伟
 * 2012-12-14 15:44:58
 *
 */
public class HttpUtil {
	/**
	 * 获取远程地址结果
	 * @param url
	 * @return
	 */
	public static String get(String url){
		String resStr="";
		try{
			HttpResponse response=WS.url(url).get();
			if(response.success()){
				resStr=response.getString();
			}
		}catch(Exception e){
			ExceptionHandler.throwRuntimeException(e,"访问"+url+":"+e.getMessage());
		}
		return resStr;
	}
}

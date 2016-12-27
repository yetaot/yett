package framework.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.Http.Request;
import play.mvc.Scope.Flash;
import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;
import framework.utils.DBUtil;
import framework.utils.DateUtil;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

public class EMController extends Controller{
	public static final Logger logger=LogUtil.getInstance(Request.current().controllerClass);
	public final static int PAGE_SIZE=10;
	/**
	 * url过滤器,除了登录URL其他的都需要管理员登录验证
	 */
	@Before
	static void requireBkUser(){
		if(!params.allSimple().containsKey("_down_sign")){ //平台下载列表避过鉴权验证
			//调用远程登陆接口
			if(SecurityUtil.isLogin()){
				if(Constant.APP_DEPLOY_TYPE.equals("remote")){
					Map<String,String> parameters = getLoginUser();
					String userName = parameters.get(SecurityUtil.DB_USERNAME);
					String password = parameters.get(SecurityUtil.DB_PASSWORD);
					DBUtil.refreshConnection(userName, password);
				}
				//调用远程登陆接口
				//String backUrl="http://"+Request.current().domain+":"+Request.current().port+Request.current().url;
				//redirect(SecurityUtil.getRemoteLoginUrl(backUrl));
			}
		}
	}
	/**
	 * 获得页面传递过来的页面页数
	 * @return
	 */
	public static int getPageSize(){
		String _pageSize = params.get(Constant.PAGE_SIZE_PARAM_NAME);
    	int pageSize = StringUtil.isBlank(_pageSize)?PAGE_SIZE:Integer.parseInt(_pageSize);
    	return pageSize;
	}
	/**
	 * 获得页面传递过来的当前页
	 * @return
	 */
	public static int getPageNo(Integer pageNo){
    	return pageNo==null?1:pageNo;
	}
	/**
	 * 如果页面传递的Query对象不存在，重新初始化一个新的query对象
	 */
	public static BaseQuery getQuery(BaseQuery query,Class clazz){
		BaseQuery baseQuery=query;
		if(null==query&&null!=clazz){
			try {
				baseQuery=(BaseQuery)clazz.newInstance();
			}catch(Exception e) {
				ExceptionHandler.throwRuntimeException(e,"根据Class初始化BaseQuery对象失败。");
			}
		}
		return  baseQuery;
	}
	/**
	 * 获得当前登陆用户
	 * id userName realName jobCode
	 * @return
	 */
	public static Map<String,String> getLoginUser(){
		Map<String,String> info=new HashMap<String,String>();
		if(SecurityUtil.isLogin()){
			info.put("id",SecurityUtil.getLoginUserId());
			info.put("userName",SecurityUtil.getUserKey(SecurityUtil.USER_NAME));
			info.put("realName",SecurityUtil.getUserKey(SecurityUtil.USER_REALNAME));
			info.put("jobCode",SecurityUtil.getUserKey(SecurityUtil.USER_JOBCODE));
			info.put("db_username",SecurityUtil.getUserKey(SecurityUtil.DB_USERNAME));
			info.put("db_password",SecurityUtil.getUserKey(SecurityUtil.DB_PASSWORD));
			info.put("emCode",SecurityUtil.getUserKey(SecurityUtil.EM_CODE));
		}
		return info;
	}

	
	
	
	/**
	 * 保存用户操作日志并flush到页面
	 * @param msg
	 */
	public static boolean saveUserLogAndNotice(String msg,boolean isSuccess){
		notice(msg,isSuccess);
		return true;
	}
	/**
	 * 保存用户操作日志并flush到页面
	 * @param msg
	 */
	public static boolean notice(String msg,boolean isSuccess){
		msg=msg.replaceAll("%","％");
		if(isSuccess){
			Flash.current().success(msg);
		}else{
			Flash.current().error(msg);
		}
		return true;
	}
	
	public static void enterDownQueue(){
		String url=StringUtil.trim(params.get("url"));
		String service =StringUtil.trim(params.get("service"));
		String loginer = StringUtil.trim(getLoginUser().get("id"),"0");
		String current = DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL);
		String zip = StringUtil.trim(params.get("zip"),"false");
		String auth = StringUtil.trim(params.get("auth"));
		String accountId = StringUtil.trim(SecurityUtil.getUserKey(SecurityUtil.ACCOUNT_ID));
		Map<String,String> parameters = params.allSimple();
		parameters.remove("body");
		parameters.remove("action");
		parameters.remove("controller");
		parameters.remove("url");
		parameters.remove("service");
		JSONObject obj = new JSONObject();
		obj.put("url", url);
		obj.put("service", service);
		obj.put("creatorId",loginer);
		obj.put("createdAt", current);
		obj.put("zip", zip);
		obj.put("params", parameters);
		obj.put("accountId", accountId);
		obj.put("auth", auth);
		DicUtil.rpush(Constant.DIC_TASK_QUEUE+"_"+accountId, obj.toString());
		renderJSON(true);
	}
	
	
	 @Finally
	    static void transfer(){
	    	if(request.params._contains("cui_domId")){
	    		String domId = request.params.get("cui_domId");
	    		String body = response.out.toString();
	    		body = body.replaceAll(SCRIPT_REGEXP, SCRIPT_BEGIN + "$1})('"+StringUtil.trim(domId)+SCRIPT_END);
	    		ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes());
	    	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	    byte[] buffer = new byte[4000];
	    		try {
	    			int byteRead=0;
	    			while(true){
	    				byteRead = bis.read(buffer);
	    				if(byteRead==-1){
	    					break;
	    				}
	    				bos.write(buffer,0,byteRead);
	    			}
	    		} catch (IOException e) {
					e.printStackTrace();
				}
	    		response.out = bos;
	    	}
	    }

	    private final static String SCRIPT_BEGIN= "<script type=\"text/javascript\">(function(domId){function \\$(selector,context){" +
	    		"var parent = jQuery(\"[domId=\"+domId+\"]\");if(!context){" +
	    		"var obj = jQuery(selector,parent);" +
	    		"if(obj.length > 0){return obj;}else{" +
	    		"return jQuery(selector);}}else{var obj = jQuery(selector,jQuery(context,parent));" +
	    		"if(obj.length > 0){return obj;}else{return jQuery(selector,context);}}}jQuery.extend(\\$,jQuery);";
	    
	    private final static String SCRIPT_END = "');</script>";
	    
	    private final static String SCRIPT_REGEXP="<(?:script|SCRIPT)\\s*(?:(?:type|TYPE)\\s*=\\s*\"(?:text|TEXT)/(?:javascript|JAVASCRIPT)\")?\\s*>([\\s\\S]*?)</(?:script|SCRIPT)>";

	
	
}

package framework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*import models.security.Customer;*/
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Scope.Session;
import play.test.Fixtures;
import framework.logs.LogUtil;
import framework.security.models.SecurityMenu;
import framework.security.store.ISecurityStore;
import framework.security.store.SecurityStoreFactory;

public class SecurityUtil {
	public static final Logger logger = LogUtil.getInstance(SecurityUtil.class);
	//内存数据库中菜单资源内存配置
	public static final String RESOURCE_STORE_CONFIG=ContextUtil.getProperty("security.resource.config","resources/config.yml");
	public static final String RESOURCE_STORE=getResourceConfigByKey("store","redis");
	public static final String RESOURCE_STORE_PREFIX=getResourceConfigByKey("storeFix","RESOURCE.");
	public static final String RESOURCE_DIC=getResourceConfigByKey("resourceDic","RESOURCE_DIC.");
	public static final String ACTION_DIC=getResourceConfigByKey("actionDic","ACTION_DIC.");
	public static final String RESOURCES=getResourceConfigByKey("resources","RESOURCES");
	public static final String SERVICES=getResourceConfigByKey("services","SERVICES");
	public static final String RESOURCE_ACTIONS=getResourceConfigByKey("actions","ACTIONS");
	public static final String SERVICES_CONF=getResourceConfigByKey("servicescfg","resources/services.yml");
	public static String resouceDicFix=RESOURCE_STORE_PREFIX+RESOURCE_DIC;
	public static String resourcesFix=RESOURCE_STORE_PREFIX+RESOURCES;
	public static String serviesFix=RESOURCE_STORE_PREFIX+SERVICES;
	//登录用户在session中的KEY
	public static final String USER_ID="BK_USER_ID";
	//登录用户名在session中的KEY
	public static final String USER_NAME="BK_USER_NAME";
	public static final String USER_REALNAME="BK_USER_REALNAME";
	public static final String USER_JOBCODE = "BK_USER_JOBCODE";
	//登录用户ROLE在CACHE中的KEY
	public static final String USER_RESOURCE="BK_USER_RESOURCES";
	public static final int RESOURCE_CODE_PREFIX_LENGTH=Integer.parseInt(ContextUtil.getProperty("security.rootMenuLength","3"));
	public static final String USER_ACTION="BK_USER_ACTIONS";
	public static final String USER_MENU_SELECTED_CODE="mid";
	public static final String USER_BACKURL = "backurl";
	public static final String USER_EMAIL="BK_USER_EMAIL";
	
	public static final String DB_USERNAME="db_username";
	public static final String DB_PASSWORD="db_password";
		
	public static final String EM_CODE="emCode";
	
	public final static String UUID="uuid";
	public final static String AGENT_ID="agentId";
	
	
	public static final String ACCOUNT_ID = "accountId";
	public final static String CFG_ADMIN_PREMISSION_CODE="999999";
	
	//加密后的密匙
	public static final String SECRET_TOKEN="SECRET_TOKEN";
	public static final String TOCKEN_KEY=ContextUtil.getProperty("application.secret");
	
	public static final String REMOTE_LOGIN_PAGE=ContextUtil.getProperty("security.loginUrl");
	//用户信息修改地址
	public static final String REMOTE_INFO_PAGE=ContextUtil.getProperty("security.adminInfo");
	
	public static final String COOKIE_DOMAIN=ContextUtil.getProperty("application.defaultCookieDomain",null);
	
	//前台菜单
	public static final String RESOURCE_TYPE_EM_KEY=ContextUtil.getProperty("security.app.em");
	//后台菜单
	public static final String RESOURCE_TYPE_PLATFORM_KEY=ContextUtil.getProperty("security.app.platform");
	//platform服务配置整合本地自定义配置菜单项
	
	public static final String CUSTOMER_ID = "FD_CUSTOMER_ID";
	public static final String CUSTOMER_USER_NAME = "FD_CUSTOMER_USER_NAME";
	public static final String CUSTOMER_NAME = "FD_CUSTOMER_NAME";
	public static final String ZHYRCHM_USER_NAME = "ZHYRCHM_USER_NAME";
	
	/**
	 * 获取远程登陆地址
	 * @param backUrl
	 * @return
	 */
	public static final String getRemoteLoginUrl(String backUrl){
		return REMOTE_LOGIN_PAGE+"?backurl="+backUrl;
	}
	
	/**
	 * 判断当前是否存在登录用户
	 * @return 
	 */
	public static boolean isLogin(){
		boolean isLogin=false;
		//如果Session已存在用户信息就先删除登录用户
		if(!StringUtil.isBlank(getUserKey(SecurityUtil.USER_ID))){
			//验证用户cookie是否合法
			String newToken=getEncryptKey(getUserKey(USER_ID),getUserResources(),getUserActions(),getUserKey(UUID));
			isLogin=!StringUtil.isBlank(newToken)&&newToken.trim().equals(getUserKey(SECRET_TOKEN));
		}
		return isLogin;
	}
	/**
	 * 管理员登录系统
	 * @return
	 */
	public static void loginWithCookie(String userId,String userName,String realName,String resources,String actions){
		setUserKey(SecurityUtil.USER_ID,userId);
		setUserKey(SecurityUtil.USER_NAME,userName);
		setUserKey(SecurityUtil.USER_REALNAME,realName);
		setUserKey(SecurityUtil.USER_RESOURCE,resources);
		setUserKey(SecurityUtil.USER_ACTION,actions);
		setUserKey(SecurityUtil.UUID,"");
		setUserKey(SecurityUtil.SECRET_TOKEN,getEncryptKey(userId,resources,actions,""));
	}
	

	public static void loginWithCookie(String jobCode,String userId,String userName,String realName,String resources,String actions,String dbName,String dbPassword,String emCode,String accountId,String uuid){
		setUserKey(SecurityUtil.USER_ID,userId);
		setUserKey(SecurityUtil.USER_NAME,userName);
		setUserKey(SecurityUtil.USER_REALNAME,realName);
		setUserKey(SecurityUtil.USER_RESOURCE,resources);
		setUserKey(SecurityUtil.USER_ACTION,actions);
		setUserKey(SecurityUtil.USER_JOBCODE,jobCode);
		setUserKey(SecurityUtil.DB_USERNAME,dbName);
		setUserKey(SecurityUtil.DB_PASSWORD,dbPassword);
		setUserKey(SecurityUtil.EM_CODE,emCode);
		setUserKey(SecurityUtil.ACCOUNT_ID,accountId);
		setUserKey(SecurityUtil.UUID, uuid);
	//	setUserKey(SecurityUtil.USER_BACKURL,backurl);
		setUserKey(SecurityUtil.SECRET_TOKEN,getEncryptKey(userId,resources,actions,uuid));

	}
	

//	public static void loginWithCookie(String domain,String jobCode,String userId,String userName,String realName,String resources,String actions,String dbName,String dbPassword,String emCode,String accountId,String uuid){
//		setUserKey(domain,SecurityUtil.USER_ID,userId);
//		setUserKey(domain,SecurityUtil.USER_NAME,userName);
//		setUserKey(domain,SecurityUtil.USER_REALNAME,realName);
//		setUserKey(domain,SecurityUtil.USER_RESOURCE,resources);
//		setUserKey(domain,SecurityUtil.USER_ACTION,actions);
//		setUserKey(domain,SecurityUtil.USER_JOBCODE,jobCode);
//		setUserKey(domain,SecurityUtil.DB_USERNAME,dbName);
//		setUserKey(domain,SecurityUtil.DB_PASSWORD,dbPassword);
//		setUserKey(domain,SecurityUtil.EM_CODE,emCode);
//		setUserKey(domain,SecurityUtil.ACCOUNT_ID,accountId);
//		setUserKey(domain,SecurityUtil.UUID, uuid);
//	//	setUserKey(SecurityUtil.USER_BACKURL,backurl);
//		setUserKey(domain,SecurityUtil.SECRET_TOKEN,getEncryptKey(userId,resources,actions,uuid,getEmToken()));
//
//	}
	
	/**
	 * 用户退出登录
	 * @return
	 */
	public static boolean logout(){
		//如果Session已存在用户信息就先删除登录用户
		removeKey(SecurityUtil.USER_ID);
		removeKey(SecurityUtil.USER_NAME);
		removeKey(SecurityUtil.USER_REALNAME);
		removeKey(SecurityUtil.USER_RESOURCE);
		removeKey(SecurityUtil.SECRET_TOKEN);
		removeKey(SecurityUtil.USER_ACTION);
		removeKey(SecurityUtil.USER_JOBCODE);
		removeKey(SecurityUtil.USER_BACKURL);
		removeKey(SecurityUtil.DB_USERNAME);
		removeKey(SecurityUtil.DB_PASSWORD);
		removeKey(SecurityUtil.ACCOUNT_ID);
		removeKey(SecurityUtil.EM_CODE);
		removeKey(SecurityUtil.UUID);
		removeKey(SecurityUtil.USER_MENU_SELECTED_CODE);
		return true;
	}
	/**
	 * 获取当前登陆用户ID
	 * @return
	 */
	public static String getLoginUserId(){
		String id=null;
		if(isLogin()){
			id=getUserKey(SecurityUtil.USER_ID);
		}
		return id;
	}
	/**
	 * 修改用户登陆信息
	 * @param user
	 */
	public static void update(String userId,String userName,String realName,String jobCode){
		setUserKey(SecurityUtil.USER_ID,userId);
		setUserKey(SecurityUtil.USER_NAME,userName);
		setUserKey(SecurityUtil.USER_REALNAME,realName);
		setUserKey(SecurityUtil.USER_JOBCODE,jobCode);
		
	}
	/**
	 * 获取用户信息
	 * @param key
	 * @return
	 */
	public static String getUserKey(String key){
		String	keyValue="";
		Map<String, Http.Cookie> cookies=Request.current().cookies;
		if(null!=cookies&&cookies.containsKey(key)&&!StringUtil.isBlank(cookies.get(key).value)){
			keyValue=StringUtil.trim(cookies.get(key).value);
			keyValue=URLDecoder.decode(keyValue);
		}
		return keyValue;
	}
	/**
	 * 如果要实现菜单选中功能需要在页面参数中添加SecurityUtil.USER_MENU_SELECTED_CODE属性，并且在过滤器中调用此方法，SecurityMenu的ifSelected便会起效果
	 * 获取当前选中的菜单
	 * @return
	 */
	public static String getSelectedMenu(){
		String mid=Request.current().params.get(SecurityUtil.USER_MENU_SELECTED_CODE);
		if(!StringUtil.isBlank(mid)){
			setUserKey(SecurityUtil.USER_MENU_SELECTED_CODE,mid);
		}else{
			mid=getUserKey(SecurityUtil.USER_MENU_SELECTED_CODE);
		}
		return mid;
	}
	/**
	 * 从内存中获取所有菜单资源
	 * @return
	 */
	public static Map<SecurityMenu,List<SecurityMenu>> getResources() {
		ISecurityStore store=SecurityStoreFactory.getInstance().getStore(RESOURCE_STORE);
		Map<SecurityMenu,List<SecurityMenu>> resources=new LinkedHashMap<SecurityMenu,List<SecurityMenu>>();
		Map<String,List<SecurityMenu>> sampleResources=new LinkedHashMap<String,List<SecurityMenu>>();
		Map<String,SecurityMenu> rootMap=new LinkedHashMap<String,SecurityMenu>();
		List<String> keys = store.getAllKeys(resourcesFix);
		//获取用户的菜单
		JSONObject haveResources=JSONObject.fromObject(getUserResources());
		for(String key:keys){
			String[] codes=key.split(",");
			String rootCode=codes[0];
			SecurityMenu root=getResource(store,resouceDicFix,rootCode);
			if(null!=root){
				if(!rootMap.containsKey(root.code)&&haveResources.containsKey(rootCode)){
					rootMap.put(root.code,root);
				}
				List<SecurityMenu> nodeCodes = new ArrayList<SecurityMenu>();
				JSONArray menus = new JSONArray();
				if(codes.length>1 && haveResources.containsKey(rootCode)){
					menus = (JSONArray) haveResources.get(rootCode);
				}
				if(!menus.isEmpty() &&!StringUtil.isBlank(codes[1]) && menus.contains(codes[1])){
					SecurityMenu child = getResource(store,resouceDicFix,codes[1]);
					if(sampleResources.containsKey(root.code)){
						nodeCodes = sampleResources.get(root.code);
					}
					nodeCodes.add(child);
					sampleResources.put(root.code, nodeCodes);
				}
			}
		}
		for(Entry<String,SecurityMenu> root:rootMap.entrySet()){
			resources.put(root.getValue(),sampleResources.get(root.getKey()));
		}
		return resources;
	}
	/**
	 * 获取用户功能
	 * @return
	 */
	public static String getUserActions(){
		return getUserKey(SecurityUtil.USER_ACTION);
	}
	/**
	 * 获取用户菜单
	 * @return
	 */
	private static String getUserResources(){
		return getUserKey(SecurityUtil.USER_RESOURCE);
	}
	
	
	/**
	 * 设置用户信息
	 * @param key
	 * @param value
	 */
	private static void setUserKey(String key,String value){
		value=URLEncoder.encode(value);
		Response.current().setCookie(key, value, null,"/",null,false);
	}
	
//	private static void setUserKey(String domain,String key,String value){
//		value = URLEncoder.encode(value);
//		Response.current().setCookie(key, value,domain,"/",null,false);
//	}
	/**
	 * 删除用户信息
	 * @param key
	 */
	private static void removeKey(String key){
		Response.current().removeCookie(key);
	}
	
	private static SecurityMenu getResource(ISecurityStore store,String prefix,String code){
		Map<String,String> result = store.getAllByKeys(prefix+code);
		SecurityMenu menu=new SecurityMenu(MapUtil.getOrDefault(result,"id",""),MapUtil.getOrDefault(result,"url",""),MapUtil.getOrDefault(result,"name",""),MapUtil.getOrDefault(result,"code",""),MapUtil.getOrDefault(result,"app",""),MapUtil.getOrDefault(result,"attrs",""));
		String nowSelected=getSelectedMenu();
		//选中子菜单
		if(!StringUtil.isBlank(nowSelected)&&nowSelected.equals(menu.code.trim())){
			menu.ifSelected=true;
		}
		//选中父级菜单
		if(!StringUtil.isBlank(nowSelected)&&menu.isRootMenu()&&menu.code.trim().equals(SecurityMenu.getRootMenuCode(nowSelected))){
			menu.ifSelected=true;
		}
		if(!menu.isRootMenu()){
			menu.url=StringUtil.trim(menu.url);
		}else{
			menu.url="";
		} 
		return 	menu;
	}
	/**
	 * 获取用户资源内存数据库加载配置项
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private static String getResourceConfigByKey(String key,String defaultValue){
		Map<String,String> configs=(Map<String, String>) Fixtures.loadYamlAsMap(RESOURCE_STORE_CONFIG);
		return MapUtil.getOrDefault(configs, key, defaultValue);
	}

	/**
	 * 用户登陆身份有效性验证密匙
	 * @param userId
	 * @param resources
	 * @param actions
	 * @return
	 */
	private static String getEncryptKey(String userId, String resources, String actions){
		return MD5Util.md5Encode(userId+resources+actions+TOCKEN_KEY);
	}
	
	private static String getEncryptKey(String userId,String resources,String actions,String uuid){
		return MD5Util.md5Encode(userId+resources+actions+TOCKEN_KEY+uuid);
	}
	
	
	private static String getEncryptKey(String userId,String resources,String actions,String uuid,String token){
		return MD5Util.md5Encode(userId+resources+actions+token+uuid);
	}
	
	private static String getEmToken(){
		return ContextUtil.getProperty("em.application.secret", "");
	}
	public static boolean hasAdminPermission(){
		return SecurityUtil.getUserActions().indexOf(CFG_ADMIN_PREMISSION_CODE)>-1;
	}
	
	public static boolean hasAction(String actionCode){
		return SecurityUtil.getUserActions().indexOf(actionCode)>-1;
	}
	
	/**
	 * 设置客户信息
	 * @param key
	 * @param value
	 */
	private static void setCustomerKey(String key, String value){
		try {
			value=URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("　设置客户信息时异常", e);
		}
		Session.current().put(key, value);
	}
	
	/**
	 * 删除客户信息
	 * @param key
	 */
	private static void removeCustomerKey(String key){
		Session.current().remove(key);
	}
	
	public static void loginWithSession(String custId, String custUserName, String custName){
		setCustomerKey(CUSTOMER_ID, custId);
		setCustomerKey(CUSTOMER_NAME, custName);
		setCustomerKey(CUSTOMER_USER_NAME, custUserName);
	}
	
	/**
	 * 获取客户信息
	 * @param key
	 * @return
	 */
	public static String getCustomerKey(String key){
		String	keyValue = "";
		if (null != Session.current() && Session.current().contains(key) && !StringUtil.isBlank(Session.current().get(key))) {
			keyValue=StringUtil.trim(Session.current().get(key));
			try {
				keyValue=URLDecoder.decode(keyValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("获取客户信息时异常", e);
			}
		}
		return keyValue;
	}
	
	
	public static void customerLogout(){
		removeCustomerKey(CUSTOMER_ID);
		removeCustomerKey(CUSTOMER_NAME);
		removeCustomerKey(CUSTOMER_USER_NAME);
		String userName = getUserKey(ZHYRCHM_USER_NAME);
		if (!StringUtil.isBlank(userName)) {
			removeKey(ZHYRCHM_USER_NAME);
		}
	}
	
	public static boolean isCustomerLogin(){
		boolean isLogin = false;
		if(!StringUtil.isBlank(getCustomerKey(CUSTOMER_ID))){
			isLogin = true;
		}
		return isLogin;
	}
	
}
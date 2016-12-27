package framework.security.models;

import java.util.HashMap;
import java.util.Map;

import framework.utils.MapUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 菜单
 * @author 张科伟
 *
 */
public class SecurityMenu {
	public String id;
	public String url;
	public String name;
	public String code;
	public String app;
	public String attrs;
	public boolean ifSelected;
	private Map<String,String> extendAttrs;
	public SecurityMenu(){
		super();
	}
	public SecurityMenu(String id,String url,String name, String code,String attrs){
		this();
		this.url=url;
		this.id=id;
		this.name=name;
		this.code=code;
		this.ifSelected=false;
		this.attrs = attrs;
		Map<String,String> extendAttrs=new HashMap<String,String>();
		if(!StringUtil.isBlank(attrs)){
			String[] attrArraies=attrs.split("&");
			for(String attr:attrArraies){
				String[] element=attr.split("=");
				if(element.length>0){
					extendAttrs.put(element[0],element.length>1?element[1]:null);
				}
			}
		}
		this.extendAttrs=extendAttrs;
	}
	
	public SecurityMenu(String id,String url,String name, String code,String app,String attrs){
		this();
		this.url=url;
		this.id=id;
		this.name=name;
		this.code=code;
		this.app=app;
		this.attrs=attrs;
		this.ifSelected=false;
		Map<String,String> extendAttrs=new HashMap<String,String>();
		if(!StringUtil.isBlank(attrs)){
			String[] attrArraies=attrs.split("&");
			for(String attr:attrArraies){
				String[] element=attr.split("=");
				if(element.length>0){
					extendAttrs.put(element[0],element.length>1?element[1]:null);
				}
			}
		}
		this.extendAttrs=extendAttrs;
	}
	/**
	 * 获取扩展属性
	 * @param key
	 * @return
	 */
	public String getExtendByKey(String key){
		return MapUtil.getOrDefault(this.extendAttrs,key,"");
	}
	/**
	 * 获取扩展属性service
	 * @param key
	 * @return
	 */
	public String getService(){
		return MapUtil.getOrDefault(this.extendAttrs,"service","");
	}
	/**
	 * 获取扩展属性
	 * @param key
	 * @return
	 */
	public boolean hasExtendKey(String key){
		return this.extendAttrs.containsKey(key);
	}
	/**
	 * 判断此菜单是否是父级菜单
	 * @return
	 */
	public boolean isRootMenu(){
		return getRootMenuCode(this.code).equals(this.code);
	}
	/**
	 * 判断此菜单是否是父级菜单
	 * @return
	 */
	public static boolean isRootMenu(String code){
		return getRootMenuCode(code).equals(code);
	}
	/**	
	 * 根据某一资源菜单获取父级资源菜单
	 * @param code
	 * @return
	 */
	public  String getRootMenuCode(){
		return getRootMenuCode(this.code);
	}
	/**
	 * 根据某一资源菜单获取父级资源菜单
	 * @param code
	 * @return
	 */
	public static String getRootMenuCode(String code){
		code=StringUtil.trim(code,"");
		String rootCode=code.length()>SecurityUtil.RESOURCE_CODE_PREFIX_LENGTH-1?code.substring(0,SecurityUtil.RESOURCE_CODE_PREFIX_LENGTH):code;
		return rootCode;
	} 
}

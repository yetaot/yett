package models.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.sf.json.JSONObject;



import framework.base.BaseModel;
import framework.security.models.SecurityMenu;
import framework.utils.StringUtil;
/**
 * 系统资源
 * @author jinchaoyang
 *
 */
@Entity
@Table(name="T_RESOURCE")
public class Resource extends BaseModel {
	//资源编号
	public String code;
	//资源名称
	public String name;
	//资源地址
	public String url;
	//资源类型 ACTION:操作 MENU:菜单
	public String type;
	//资源类别 platform:运营管理平台 em:企业
	public String rType;
	//预留字段
	public String attr;
	
	
	public Resource(){
		
	}
	
	public Resource(String name,String code,String type,String rType){
		this.name = name;
		this.code = code;
		this.type = type;
		this.rType = rType;
	}
	
	public Resource(String name ,String code,String type,String rType,String url){
		this(name,code,type,rType);
		this.url = url;
	}
	
	/**
	 * 根据资源编号查询对应的资源
	 * @param code
	 * @return
	 */
	public static Resource findByCode(String code){
		String sql="from Resource a where a.code=?";
		return Resource.find(sql, code).first();
	}
	/**
	 * 查询所有的资源信息
	 * @return
	 */
	public static List<Resource> queryAll(){
		String sql="from Resource a  order by a.code,a.type asc";
		return Resource.find(sql).fetch();
	}
	/**
	 * 获取当前菜单的父级菜单
	 * @return
	 */
	public String getRootCode(){
		return SecurityMenu.getRootMenuCode(this.code);
	}
	
	public boolean isRoot(){
		return SecurityMenu.getRootMenuCode(this.code).equals(this.code);
	}
	
	public static Map<Resource,List<Resource>> queryResources(){
		Map<Resource,List<Resource>> result = new LinkedHashMap<Resource,List<Resource>>();
		Map<String,Resource> roots = new LinkedHashMap<String,Resource>();
		Map<String,List<Resource>> children = new HashMap<String,List<Resource>>();
		List<Resource>  allResources = queryAll();
		List<Resource> nodes = null;
		for(Resource r : allResources){
			String rootCode = r.getRootCode();
			if(r.code.equals(rootCode)){
				roots.put(rootCode,r);
				children.put(rootCode, new ArrayList<Resource>());
			}else{
				nodes = children.get(rootCode);
				if(null!=nodes){
					nodes.add(r);
					children.put(rootCode, nodes);
				}
			}
		}
		for(Entry<String,Resource> entry : roots.entrySet()){
			result.put(entry.getValue(),children.get(entry.getKey()));
		}
		return result;
	}
	
	public SecurityMenu parseToSecurityMenu(){
		SecurityMenu menu = new SecurityMenu(this.id+"",StringUtil.trim(this.url),StringUtil.trim(this.name),StringUtil.trim(this.code),StringUtil.trim(this.attr));
		return menu;
	}
	
	public JSONObject parseToJSON(){
		JSONObject obj = new JSONObject();
		obj.put("id", StringUtil.trim(this.id));
		obj.put("name", StringUtil.trim(this.name));
		obj.put("url", StringUtil.trim(this.url));
		obj.put("app", "");
		obj.put("rtype", StringUtil.trim(this.rType));
		obj.put("code", StringUtil.trim(this.code));
		obj.put("attrs", StringUtil.trim(this.attr));
		obj.put("type", StringUtil.trim(this.type));
		obj.put("isRoot",this.isRoot()?"1":"0");
		return obj;
	}
	
	public static Map<SecurityMenu,List<SecurityMenu>> querySecurityMenus(){
		Map<Resource,List<Resource>> resources = queryResources();
		Map<SecurityMenu,List<SecurityMenu>> menus = new LinkedHashMap<SecurityMenu,List<SecurityMenu>>();
		for(Entry<Resource,List<Resource>> resource : resources.entrySet()){
			List<SecurityMenu> children = new ArrayList<SecurityMenu>();
			for(Resource r : resource.getValue()){
				children.add(r.parseToSecurityMenu());
			}
			menus.put(resource.getKey().parseToSecurityMenu(), children);
		}
		return menus;
	}
	
}

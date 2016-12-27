package controllers.security;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import play.data.binding.ParamNode;
import play.data.validation.Valid;
import play.mvc.After;
import play.mvc.Controller;
import models.security.Permission;
import models.security.Resource;
import models.security.Role;
import models.security.vo.RoleQuery;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.utils.DicUtil;
import framework.utils.StringUtil;

public class Roles extends BaseController {
	/**
	 * 角色列表
	 * @param page
	 * @param query
	 */
	public static void index(Integer page,RoleQuery query){
		PagerRS<Role> rs =  (PagerRS<Role>) Role.findByPager(getQuery(query,RoleQuery.class), getPageNo(page), getPageSize());
		render(rs);
	}
	/**
	 * 新增角色
	 */
	public static void add(){
		Map<Resource,List<Resource>> resources = Resource.queryResources();
		Map<Resource,List<Resource>> menus = new LinkedHashMap<Resource,List<Resource>>();
		Map<Resource,List<Resource>> actions = new LinkedHashMap<Resource,List<Resource>>();
		String codes="";
		Resource r = null;
		for(Entry<Resource, List<Resource>> entry : resources.entrySet()){
			r = entry.getKey();
			if(Constant.RESOURCE_TYPE_MENU_NAME.equals(r.type)){
				menus.put(r, entry.getValue());
			}else{
				actions.put(r, entry.getValue());
			}
		}
		render(menus,actions,codes);
	}
	
	/**
	 * 保存角色
	 * @param role
	 */
	public static void create(@Valid Role role,long[] subNode){
		if(null==subNode){
			validation.addError("role.type", "请为角色分配权限");
		}
		if(validation.hasErrors()){
			String codes="";
			Map<Resource,List<Resource>> resources = Resource.queryResources();
			if(null!=subNode){
				for(long n : subNode){
					if(StringUtil.isBlank(codes)){
						codes=n+"";
					}else{
						codes=codes+","+n;
					}
				}
			}
			Map<Resource,List<Resource>> menus = new LinkedHashMap<Resource,List<Resource>>();
			Map<Resource,List<Resource>> actions = new LinkedHashMap<Resource,List<Resource>>();
			Resource r = null;
			for(Entry<Resource, List<Resource>> entry : resources.entrySet()){
				r = entry.getKey();
				if(Constant.RESOURCE_TYPE_MENU_NAME.equals(r.type)){
					menus.put(r, entry.getValue());
				}else{
					actions.put(r, entry.getValue());
				}
			}
			render("@add",menus,actions,codes);
		}else{
		  String loginerId = StringUtil.trim(getLoginUser().get("id"));
		  role.operatorId = Long.parseLong(loginerId);
		  role.assign(subNode);
		  saveUserLogAndNotice("角色【"+role.name+"】添加成功", true);
		  index(null,null);
		}
		
	}
	
	/**
	 * 修改角色
	 * @param id
	 */
	public static void edit(long id){
		Role role = Role.findById(id);
		Map<Resource,List<Resource>> resources = Resource.queryResources();
		Map<Resource,List<Resource>> menus = new LinkedHashMap<Resource,List<Resource>>();
		Map<Resource,List<Resource>> actions = new LinkedHashMap<Resource,List<Resource>>();
		Resource r = null;
		for(Entry<Resource, List<Resource>> entry : resources.entrySet()){
			r = entry.getKey();
			if(Constant.RESOURCE_TYPE_MENU_NAME.equals(r.type)){
				menus.put(r, entry.getValue());
			}else{
				actions.put(r, entry.getValue());
			}
		}
		String codes = role.assginedCodes();
		render(role,menus,actions,codes);
	}
	
	/**
	 * 更新角色
	 * @param id
	 */
	public static void update(long id,long[] subNode){
		Role role = Role.findById(id);
		role.edit(ParamNode.convert(params.all()), "role");
		if(null==subNode){
			validation.addError("role.type", "请为角色分配权限");
		}
		validation.valid(role);
		if(validation.hasErrors()){
			Map<Resource,List<Resource>> resources = Resource.queryResources();
			String codes = role.assginedCodes();
			Map<Resource,List<Resource>> menus = new LinkedHashMap<Resource,List<Resource>>();
			Map<Resource,List<Resource>> actions = new LinkedHashMap<Resource,List<Resource>>();
			Resource r = null;
			for(Entry<Resource, List<Resource>> entry : resources.entrySet()){
				r = entry.getKey();
				if(Constant.RESOURCE_TYPE_MENU_NAME.equals(r.type)){
					menus.put(r, entry.getValue());
				}else{
					actions.put(r, entry.getValue());
				}
			}
			render("@edit",role,menus,actions,codes);
		}else{
			String loginer = StringUtil.trim(getLoginUser().get("id"),"0");
			role.operatorId = Long.parseLong(loginer);
			role.updatedAt = new Date();
			role.assign(subNode);
			saveUserLogAndNotice("角色【"+role.name+"】修改成功", true);
			index(null,null);
		}
		
	}
	/**
	 * 角色删除
	 * @param id
	 */
	public static void destroy(long id){
		Role role = Role.findById(id);
		role.destroy();
		saveUserLogAndNotice("角色【"+role.name+"】删除成功", true);
		index(null,null);
	}
	/**
	 * 角色查看
	 * @param id
	 */
	public static void show(long id){
		Role role = Role.findById(id);
		Map<Resource,List<Resource>> resources = role.assginedResource();
		render(role,resources);
	}
	
    /**
     * 生命周期
     */
    @After(only={"security.Roles.create","security.Roles.update","security.Roles.destroy"})
    public static void lifeCycle(){
    	DicUtil.loadByDicName(Constant.DIC_ROLE_NAME);
    	DicUtil.loadByDicName(Constant.DIC_PLATFORM_ROLE_NAME);
    }
	
}

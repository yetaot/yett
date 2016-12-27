package models.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


import play.data.validation.Required;
import play.data.validation.Unique;

import framework.base.BaseModel;
import framework.utils.StringUtil;
/**
 * 用户角色
 * @author jinchaoyang
 *
 */
@Entity
@Table(name="T_ROLE")
public class Role extends BaseModel {
	//角色名
	@Required(message="角色名不能为空")
	@Unique(message="角色名已存在")
	public String name;
	//角色类型
	@Required(message="角色类型不能为空")
	public String type;
	//是否可用
	public int ifUse;
	//是否系统默认
	public int ifSystem;
	//创建时间
	public Date createdAt;
	//更新时间
	public Date updatedAt;
	//操作人ID
	public long operatorId;
	
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY)
	@JoinTable(name="T_ROLE_PERMISSION")
	public List<Permission> permissions = new ArrayList<Permission>();
	
	//角色
	@ManyToMany(mappedBy="roles")
	public List<User> users = new ArrayList<User>();
	
	public Role(){
		this.createdAt = new Date();
		this.updatedAt = new Date();
		this.ifSystem=0;
		this.ifUse=1;
	}
	
	
	
	
	
	public void destroy(){
		this.ifUse=0;
		this.permissions.clear();
		this.save();
	}
	
	/**
	 * 为角色分配权限
	 * @param resourceIds
	 */
	public void assign(long[] resourceIds){
		Set<Permission> permissions = new HashSet<Permission>();
		Resource resource=null;
		Permission permission=null;
		for(long rid : resourceIds){
			resource = Resource.findById(rid);
			if(null!=resource){
				permission = new Permission(resource.code,resource.type);
				permission.save();
				permissions.add(permission);
			}
		}
		this.permissions.clear();
		this.permissions.addAll(permissions);
		this.save();
	}
	/**
	 * 已被分配权限的编号
	 * @return
	 */
	public String assginedCodes(){
		List<Permission> permissions = this.permissions;
		Set<String> result = new HashSet<String>();
		for(Permission permission : permissions){
			result.add(permission.code);
		}
		String rs = "";
		for(String s : result){
			if(StringUtil.isBlank(rs)){
				rs =s; 
			}else{
				rs=rs+","+s;
			}
		}
		return rs;
	}
	/**
	 * 已被分配的资源对象
	 * @return
	 */
	public Map<Resource,List<Resource>> assginedResource(){
		List<Permission> permissions = this.permissions;
		LinkedHashMap<String,List<Resource>> nodes = new LinkedHashMap<String,List<Resource>>();
		LinkedHashMap<Resource,List<Resource>> result = new LinkedHashMap<Resource,List<Resource>>();
		Resource r = null;
		String rootCode = "";
		List<Resource> resources = null;
		for(Permission p : permissions){
			r = Resource.findByCode(p.code);
			rootCode = r.getRootCode();
			if(!nodes.containsKey(rootCode)){
				resources = new ArrayList<Resource>();
			}else{
				resources = nodes.get(rootCode);
			}
			resources.add(r);
			nodes.put(rootCode, resources);
		}
		for(Entry<String,List<Resource>> entry : nodes.entrySet()){
			r = Resource.findByCode(entry.getKey());
			if(null!=r){
				result.put(r, entry.getValue());
			}
		}
		return result;
		
	}
	
	public List<Permission> fetchPermissions(){
		List<Permission> ps = new ArrayList<Permission>();
		List<String> codes = new ArrayList<String>();
		Map<String,Permission> hash = new HashMap<String,Permission>();
		for(Permission p : this.permissions){
			codes.add(p.code);
			hash.put(p.code, p);
		}
		Collections.sort(codes);
		for(String code : codes){
			ps.add(hash.get(code));
		}
		return ps;
	}
	
	
	
	
}

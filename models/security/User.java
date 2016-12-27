package models.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.oval.constraint.MaxLength;
import models.utils.SecurityConstant;
import play.data.validation.Check;
import play.data.validation.CheckWith;
import play.data.validation.Equals;
import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import framework.base.BaseModel;
import framework.base.Constant;
import framework.utils.DicUtil;
import framework.utils.MD5Util;
import framework.utils.StringUtil;
import framework.utils.SecurityUtil;
/**
 * 系统用户
 * @author jinchaoyang
 *
 */
@Entity
@Table(name="T_USER")
public class User extends BaseModel {
	//用户名
	@Required(message="用户名不能为空")
	@CheckWith(value=MyUserNameCheck.class,message="用户名已存在")
	@MaxLength(value=16,message="用户名最长16个字符")
	public String  userName;
	//姓名
	@MaxLength(value=16,message="姓名最长16个字符")
	@Required(message="姓名不能为空")
	public String name;
	//密码
	@Required(message="密码不能为空")
	public String password;
	//密码确认
	@Transient
	@Equals(value="password",message="密码不一致")
	public String passwordConfirm;
	//联系方式
	@Match(value="[0-9]*",message="联系方式必须为数字")
	public String phone;
	//创建时间
	public Date createdAt;
	//更新时间
	public Date updatedAt;
	//上次登陆时间
	public Date lastLoginAt;
	//本次登陆时间
	public Date loginAt;
	//上次登陆IP
	public String lastLoginIp;
	//本次登陆IP
	public String loginIp;
	//是否系统默认
	public int ifSystem;
	//密钥
	public String secretKey;
	//是否可用
	public int ifUse;
	//创建人ID
	public Long creatorId;
	//角色
	@ManyToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
	@JoinTable(name="T_USER_ROLE")
	public List<Role> roles = new ArrayList<Role>();
	
	public User(){
		this.createdAt = new Date();
		this.updatedAt = new Date();
		this.ifUse=1;
		this.ifSystem=0;
	}
	public static class MyUserNameCheck extends Check{

		@Override
		public boolean isSatisfied(Object obj, Object value) {
			User user = null;
			User user2 = null;
			if(obj instanceof User){
				user = (User)obj;
			}
			if(user.id == null){
				user2 = User.find("userName=? and ifUse=1", value).first();
			}else{
				user2 = User.find("userName=? and ifUse=1 and id<>?", value,user.id).first();
			}
			if(user2 == null){
				return true;
			}else{
				return false;
			}
		}
		
	}
	/**
	 * 根据用户名查询用户对象
	 * @param userName
	 * @param password
	 * @return
	 */
	public static User findByUserName(String userName){
		String sql ="from User a where a.userName=?";
		return User.find(sql, userName).first();
	}

	
	public Map<String,String> allPermissions(){
		List<Role> roles  = this.roles;
		Set<Permission> permissions = new LinkedHashSet<Permission>();
		if(null!=roles){
			for(Role role : roles){
				if(role.ifUse==1){
					permissions.addAll(role.permissions);
				}
			}
		}
		Map<String,String> result = Permission.initResource(permissions);
		return  result;
	}

	
	 /**
     * 为用户选择角色
     */
	public void assignRoles(String[] rid) {
    	Set<Role> roles = new HashSet<Role>();
	    for(int i=0;i<rid.length;i++){
	    	if(!StringUtil.isBlank(rid[i])){
		    	Role r = Role.findById(Long.parseLong(rid[i]));
		    	if(null!=r){
		    		roles.add(r);
		    	}
	    	}
	    }
	    this.roles.clear();
	    this.roles.addAll(roles);
	}
	

	public void destroy(){
		this.ifUse=0;
		this.roles.clear();
		this.save();
	}
	
	public void login(String loginIp){
		this.lastLoginAt = this.loginAt;
		this.lastLoginIp = this.loginIp;
		this.loginAt = new Date();
		this.loginIp = loginIp;
		this.save();
	}
	
	public static void saveAgentUser(String name,String password,String phone, String[] rids){
		User user = new User();
		user.createdAt = new Date();
		user.ifSystem = 1;
		user.ifUse = 1;
		user.name = name;
		user.phone = phone;
		user.assignRoles(rids);
		user.updatedAt = new Date();
		user.userName = "admin";
		user.secretKey=UUID.randomUUID().toString();
		user.password = password;
		user.password=MD5Util.md5Encode(user.password.trim()+user.secretKey);
		String currUId = SecurityUtil.getLoginUserId();
		user.creatorId = Long.valueOf(currUId);
		user.create();
		DicUtil.loadByDicName(Constant.DIC_USER_NAME);
    	DicUtil.loadByDicName(SecurityConstant.DIC_ALL_USER_NAME);
	}
	
	public static void updateAgentUser(Long id, String name,String phone){
		User user = User.findById(id);
		if(null != user){
			user.name = name;
			user.phone = phone;
			user.updatedAt = new Date();
			user.save();
			DicUtil.loadByDicName(Constant.DIC_USER_NAME);
	    	DicUtil.loadByDicName(SecurityConstant.DIC_ALL_USER_NAME);
		}
	}
	
	public static User findUserByUserName(String userName){
		User user = User.find("userName=? and ifUse=1",userName).first();
		return user;
	}
}


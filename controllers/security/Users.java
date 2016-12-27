package controllers.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import models.security.Role;
import models.security.User;
import models.security.vo.UserQuery;
import models.utils.SecurityConstant;
import play.data.binding.ParamNode;
import play.data.validation.Valid;
import play.mvc.After;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.utils.CollectionUtil;
import framework.utils.DicUtil;
import framework.utils.MD5Util;
import framework.utils.StringUtil;

public class Users extends BaseController{
     /**
      * 查看页面
      * */
	public static void index(Integer page,UserQuery query){
		if(null == query){
			query = new UserQuery();
		}
		PagerRS<User> rs = (PagerRS<User>) User.findByPager(getQuery(query,UserQuery.class),getPageNo(page),getPageSize());
		render(rs);
	}
	/**
	 * 新增用户信息
	 * */
	public static void add(){
		Map editData=buildEditData(1,null,null);
		render(editData);
	}
	public static void create(User user,String[] rids){
		String loginerId = StringUtil.trim(getLoginUser().get("id"),"0");
		validation.valid(user);
		if(!validation.hasErrors()){
			user.secretKey=UUID.randomUUID().toString();
			user.password=MD5Util.md5Encode(user.password.trim()+user.secretKey);
			user.assignRoles(null!=rids?rids:new String[]{});
			user.creatorId = Long.valueOf(loginerId);
			user.save();
			saveUserLogAndNotice("用户【"+user.name+"】添加成功", true);
			index(null,null);
		}else{
			Map editData=buildEditData(1,user,rids);
			render("@add",editData);
		}
		
	}
	/**
	 * 修改用户信息
	 * */
	public static void edit(long id){
		User user=User.findById(id);
		Map editData=buildEditData(2,user,null);
		render(user,editData);
	}
	//修改操作
	public static void update(long id,String[] rids){
		User user = User.findById(id);
    	String dbPwd=user.password;
    	user.edit(ParamNode.convert(params.all()), "user");
    	if(!dbPwd.equals(user.password.trim())){
   		 	user.password=MD5Util.md5Encode(user.password.trim()+user.secretKey);
   		 	user.passwordConfirm=MD5Util.md5Encode(user.passwordConfirm.trim()+user.secretKey);
    	}
    	//验证表单信息
    	validation.valid(user);
    	if(validation.hasErrors()){
    		Map editData=buildEditData(3,user,rids);
        	render("@edit",user,editData);
    	}else{
	    	user.updatedAt=new Date();
	    	//为用户分配用户组，代码禁用多对多关系，以后如需要可开启
	    	user.assignRoles(null!=rids?rids:new String[]{});
		    user.save();
		    saveUserLogAndNotice("用户【"+user.name+"】修改成功", true);
	 		index(null,null);
    	}
	}
	
	
	
	/**
	 * 查看用户详情
	 * */
	public static void show(long id){
	    User user = User.findById(id);
	    render(user);
    }
	/**
	 * 删除用户
	 * @param id
	 */
	public static void destroy(long id){
		User user=User.findById(id);
		user.destroy();
		saveUserLogAndNotice("用户【"+user.name+"】删除成功", true);
		index(null,null);
	}
	/**
	 * @param buildType 1:添加 2：修改3：失败
	 * */
	private static Map buildEditData( int buildType,User user,String[] rids) {
		
		Map map=new HashMap();
		Map<String,String> roles = null;
		roles=DicUtil.getAllByDicName(Constant.DIC_PLATFORM_ROLE_NAME);
    	String rid="";
    	if(buildType==1||buildType==3){
    		rid=CollectionUtil.join(rids,",");
    	}else{
    		rid=CollectionUtil.joinEnitiy(user.roles,null,Role.class,"id");
    	}
    	if(!rid.equals("")){
    		map.put("rids",CollectionUtil.getMapFromKeys(roles,rid.split(","),String.class));
    		roles=CollectionUtil.removeByKeys(roles,rid.split(","),String.class);
    	}
    	map.put("roles", roles);
		return map;
	}
	
	/**
	 * 生命周期
	 */
	@After(only={"security.Users.create","security.Users.update","security.Users.destroy"})
	public static void lifeCycle(){
		DicUtil.loadByDicName(Constant.DIC_USER_NAME);
		DicUtil.loadByDicName(SecurityConstant.DIC_ALL_USER_NAME);
		DicUtil.loadByDicName(models.utils.SecurityConstant.dic_twounit);
	}
}

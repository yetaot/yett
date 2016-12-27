package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.Request;
import play.mvc.Scope.RenderArgs;

import java.util.*;

import framework.base.Constant;
import framework.security.models.SecurityMenu;
import framework.utils.MD5Util;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

import models.*;
import models.security.User;
import models.utils.SecurityConstant;
/*
 * 系统登陆控制
 */
public class Application extends Controller {
	/**
	 * 登陆页
	 */
    public static void index() {
    	if(SecurityUtil.isLogin()){
    		main();
    	}else{
    		render();	
    	}
    }
    /**
     * 登陆验证
     */
    public static void login(){
    	String userName = StringUtil.trim(params.get("userName"));
    	String password = StringUtil.trim(params.get("password"));
    	String reason="";
    	User user = null;
    	if(StringUtil.isBlank(userName) || StringUtil.isBlank(password)){
    		reason="用户名或密码不能为空";
    	}else{
    		user = User.findUserByUserName(userName);
    		if(null==user){
    			reason="用户名或密码不正确";
    		}else if(user.ifUse==0){
    			reason="该用户已被禁用";
    		}else{
    			//用户密码验证
    			String encryptStr = MD5Util.encodeToStr(StringUtil.trim(password)+StringUtil.trim(user.secretKey));
    			if(!encryptStr.equals(StringUtil.trim(user.password))){
    				reason="用户名或密码不正确";
    			}else{
    				Map<String,String> permissions = user.allPermissions();
    				String loginIp = request.remoteAddress;
    				user.login(loginIp);
    				SecurityUtil.loginWithCookie(user.id+"",user.userName, user.name, permissions.get(Constant.RESOURCE_TYPE_MENU_NAME), permissions.get(Constant.RESOURCE_TYPE_ACTION_NAME));
    			}
    			
    		}
    	}
    	if(!StringUtil.isBlank(reason)){
    		flash.error(reason);
    		render("@index");
    	}else{
    		main();
    	}
    }
    /**
     * 系统主页面
     */
    public static void main(){
    	render();
    }
    
    public static void logout(){
    	SecurityUtil.logout();
    	index();
    }
    

    @Before(only="Application.main")
    static void requireUser(){
    	if(!SecurityUtil.isLogin()){
    		index();
		}else{
			SecurityUtil.getSelectedMenu();
			Map<SecurityMenu,List<SecurityMenu>> resources=SecurityUtil.getResources();
			RenderArgs.current().put("resources",resources);
		}
    	
    }

}
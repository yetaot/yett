package framework.base;

import java.util.List;
import java.util.Map;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Scope.RenderArgs;
import framework.security.models.SecurityMenu;
import framework.utils.SecurityUtil;


/**
 * @author 张科伟
 * @Date：
 * 
 */
public class FrontController extends Controller{
	
	@Before(unless={"front.Application.logout","front.Application.toLogin", "front.Application.toRegister", "front.Application.randImg"})
	static void checkIfLogin(){
		if(SecurityUtil.isCustomerLogin()){
			RenderArgs.current().put("customerId", SecurityUtil.getCustomerKey(SecurityUtil.CUSTOMER_ID));
			RenderArgs.current().put("customerName", SecurityUtil.getCustomerKey(SecurityUtil.CUSTOMER_NAME));
		}
	}
}

package framework.base;

import java.util.List;
import java.util.Map;

import play.mvc.Before;
import play.mvc.Scope.RenderArgs;
import framework.security.models.SecurityMenu;
import framework.utils.SecurityUtil;


/**
 * @author 张科伟
 * @Date：
 * 
 */
public class AdminController extends BaseController{
	@Before
	public static void initMenu(){
		if(SecurityUtil.isLogin()){
			SecurityUtil.getSelectedMenu();
			Map<SecurityMenu,List<SecurityMenu>> resources=SecurityUtil.getResources();
			RenderArgs.current().put("resources",resources);
		}
	}
}

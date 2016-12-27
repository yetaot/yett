package controllers.interfaces;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import models.em.IPTable;
import models.resource.JobManager;
import models.security.User;
import models.utils.SecurityConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.modules.redis.Redis;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;
import framework.base.Constant;
import framework.logs.LogUtil;
import framework.utils.DBUtil;
import framework.utils.DicUtil;
import framework.utils.StringUtil;

public class EmInterface  extends Controller{
	
	private final static Logger logger = LogUtil.getInstance(EmInterface.class);
	
	@Before
	static void ipFilter(){
		String iptables =StringUtil.trim(IPTable.iptables());
		String remoteIp = request.remoteAddress;
		 if(iptables.indexOf(remoteIp)==-1){
			logger.error(String.format("[%s]: remoteIp<%s>, url<%s>","ipFilter",request.remoteAddress,request.url));
			 notFound();
		 }
	}
}
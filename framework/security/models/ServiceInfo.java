package framework.security.models;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import framework.utils.MapUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 服务
 * @author 张科伟
 *
 */
public class ServiceInfo {
	public String name;
	public String code;
	public String domain;
	public ServiceInfo(){
		super();
	}
	public ServiceInfo(String name,String code,String domain){
		this();
		this.name=name;
		this.code=code;
		this.domain=domain;
	}
	public static ServiceInfo fromJSON(String str){
		JSONObject _json=JSONObject.fromObject(str);
		if(null==_json||!_json.containsKey("name")){
			return null;
		}
		ServiceInfo info=new ServiceInfo();
		if(_json.containsKey("name")){
			info.name=_json.getString("name");
		}
		if(_json.containsKey("code")){
			info.code=_json.getString("code");
		}
		if(_json.containsKey("domain")){
			info.domain=_json.getString("domain");
		}
		return info;
	}
}

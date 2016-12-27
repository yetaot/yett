package controllers.security;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import models.security.Region;
import framework.utils.StringUtil;
import play.mvc.Controller;

public class Regions extends Controller {
	/**
	 * 根据省份查询所有子级城市
	 */
	public static void cities(){
		String proviceId = StringUtil.trim(params.get("proviceId"),"0");
		Region region = Region.findById(Long.parseLong(proviceId));
		JSONArray array = new JSONArray();
		if(null!=region){
			JSONObject obj = null;
			List<Region> regions = Region.queryByParent(region.code);
			for(Region r : regions){
				obj = new JSONObject();
				obj.put("id", r.id);
				obj.put("name", r.name);
				obj.put("code", r.code);
				array.add(obj);
			}
		}
		renderJSON(array);
	}
}

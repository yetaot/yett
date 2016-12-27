package controllers;

import java.util.Map;
import java.util.Map.Entry;

import models.utils.SecurityConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import framework.base.BaseController;
import framework.base.Constant;
import framework.logs.LogUtil;
import framework.utils.ArrayUtil;
import framework.utils.DicUtil;
import framework.utils.PinYinUtil;
import framework.utils.StringUtil;

public class AutoComplete extends BaseController {
	private static final Logger logger = LogUtil.getInstance(AutoComplete.class);
	
	public static void queryActiveUser(){
		String info = StringUtil.trim(params.get("q"));
		logger.info(String.format("[%s]:params<%s>", "queryActiveUser", info));
		JSONArray array = new JSONArray();
		Map<String, String> userList = DicUtil.getAllByDicName(Constant.DIC_USER_NAME);
		for (Entry<String, String> element : userList.entrySet()) {
			String name = element.getValue();
			String pyName = PinYinUtil.getSpell(StringUtil.trim(name));
			logger.info(String.format("[%s]:USER_ID<%s> ,USER_NAME<%s>, PY_USER_NAME<%s>", "queryActiveUser", element.getKey(), name, pyName));
			if (pyName.indexOf(info.toUpperCase()) > -1 || StringUtil.trim(name).indexOf(info) > -1) {
				JSONObject obj = new JSONObject();
				obj.put("id", element.getKey());
				obj.put("name", name);
				obj.put("value", element.getKey());
				array.add(obj);
			}
		}
		renderJSON(array);
	}
}

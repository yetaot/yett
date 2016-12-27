package framework.utils;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import framework.base.Constant;

public class SysParamUtil {
	
	/**
	 * 根据键值获取系统参数的值
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String typeCode, String key){
		return DicUtil.getValueByKey(Constant.SYSTEM_PARAMS_DICNAME, typeCode+"."+key);
	}
	
	/**
	 * 获取多个参数
	 * @param list
	 * @return
	 */
	public static Map getValues(Map<String, String> m){
		Map<String,String> map1 = new HashMap<String, String>();
		for(Entry<String,String> map: m.entrySet()){
			map1.put(map.getValue()+"."+map.getKey(), DicUtil.getValueByKey(Constant.SYSTEM_PARAMS_DICNAME,map.getValue()+"."+map.getKey()));
		}
		return map1;
	}
	
	public static Map<String,String> getAgentValues(){
		Map<String, String> map1 = new HashMap<String, String>();
		Map<String,String> mapAll = DicUtil.getAllByDicName(Constant.SYSTEM_PARAMS_DICNAME);
		
		for(Entry<String,String> m: mapAll.entrySet()){
			if(m.getKey().matches("^agentSet.*")){
				map1.put(m.getKey(), m.getValue());
			}
		}
		return map1;
	}
	
	   public static Map<String,String> getValuesByKey(String key){
           key = StringUtil.trim(key);
           String pattern = "^"+key+".*";
           Map<String, String> map1 = new HashMap<String, String>();
           Map<String,String> mapAll = DicUtil.getAllByDicName(Constant.SYSTEM_PARAMS_DICNAME);
           for(Entry<String,String> m: mapAll.entrySet()){
                   if(m.getKey().matches(pattern)){
                           map1.put(m.getKey(), m.getValue());
                   }
           }
           return map1;
   }
}

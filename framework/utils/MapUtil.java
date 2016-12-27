package framework.utils;

import java.util.Map;

public class MapUtil {
	
	public static String getOrDefault(Map map, String key, String defaultValue) {
		return map.containsKey(key) ? StringUtil.trim(map.get(key)) : defaultValue;
	}
}

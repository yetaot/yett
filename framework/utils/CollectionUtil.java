package framework.utils;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 集合工具类
 * @author zkevin
 *
 */
public class CollectionUtil {
	/**
	 * 实现类似js join的方法
	 * @param iterator
	 * @params joinStr
	 * @return  以 joinStr分割的字符串
	 */
	public static  String join(Collection collection,String joinStr){
		StringBuffer sb=new StringBuffer();
		Iterator iterator=collection.iterator();
		while(iterator.hasNext()){
			sb.append(joinStr+iterator.next());
		}
		return sb.toString().length()>0?sb.substring(joinStr.length()):"";
	}
	
	/**
	 * 生成size个已joinStr分割的fill
	 */
	public static  String join(int size,String fill,String joinStr){
		StringBuffer sb=new StringBuffer();
		joinStr=StringUtil.isBlank(joinStr)?",":joinStr;
		while(size>0){
			sb.append(joinStr+fill);
			size--;
		}
		return sb.toString().length()>0?sb.substring(joinStr.length()):"";
	}
	
	/**
	 * 实现类似js join的方法
	 * @param iterator
	 * @params joinStr
	 * @return  以 joinStr分割的字符串
	 */
	public static  String join(Object[] objs,String joinStr){
		return join(arrayToList(objs),joinStr);
	}
	
	/**
	 * 实现类似js join的方法
	 * @param iterator
	 * @params joinStr
	 * @return  以 joinStr分割的字符串
	 */
	public static  String joinEnitiy(Collection iterator,String joinStr,Class clazz,String field){
		return joinEnitiy(iterator,joinStr,clazz,field,"","");
	}
	/**
	 * 实现类似js join的方法
	 * @param iterator
	 * @params joinStr
	 * @return  以 joinStr分割的字符串
	 */
	public static  String joinEnitiy(Object[] objs,String joinStr,Class clazz,String field){
		return joinEnitiy(arrayToList(objs),joinStr,clazz,field,"","");
	}
	
	
	/**
	 * 实现类似js join的方法
	 * @param iterator
	 * @params joinStr
	 * @return  以 joinStr分割的字符串
	 */
	public static  String joinEnitiy(Object[] objs,String joinStr,Class clazz,String field,String prefix,String endfix){
		return joinEnitiy(arrayToList(objs),joinStr,clazz,field,prefix,endfix);
	}
	
	/**
	 * 实现类似js join的方法
	 * @param iterator
	 * @params joinStr
	 * @return  以 joinStr分割的字符串
	 */
	public static  String joinEnitiy(Collection collection,String joinStr,Class clazz,String field,String prefix,String endfix){
		StringBuffer sb=new StringBuffer();
		joinStr=StringUtil.isBlank(joinStr)?",":joinStr;
		Iterator iterator=collection.iterator();
		while(iterator.hasNext()){
			Object entity=iterator.next();
			sb.append(joinStr+prefix+ReflectUtil.getByColumn(entity,field)+endfix);
		}
		return sb.toString().length()>0?sb.substring(joinStr.length()):"";
	}
	
	public static Map removeByKeys(Map maps,Object[] keys,Class mapKeyClazz){
		for(Object key:keys){
			if(maps.containsKey(ReflectUtil.parseTo(key,mapKeyClazz))){
				maps.remove(ReflectUtil.parseTo(key, mapKeyClazz));
			}
		}
		return maps;
	}
	/**
	 * 从实体列表中取出两个的字段一个作为key一个作为value,转换成Map
	 * @param iterator
	 * @param clazz
	 * @param key
	 * @param value
	 * @return
	 */
	public static Map entitiesToMap(Collection collection,Class clazz,String keyField,String valueField){
		Iterator iterator=collection.iterator();
		Map map=new HashMap();
		while(iterator.hasNext()){
			Object entity=iterator.next();
			map.put(ReflectUtil.getByColumn(entity,keyField),ReflectUtil.getByColumn(entity,valueField));
		}
		return map;
	}
	/**
	 * 将实体集合转换为JSONObject数组
	 * @param iterator
	 * @param clazz
	 * @param keys key=>JSONObject的key  value=>clazz对应的字段名称
	 * @return
	 */
	public static JSONArray entitiesToJSONArray(Collection collection,Class clazz,Map<String,String> keys){
		Iterator iterator=collection.iterator();
		JSONArray array=new JSONArray();
		while(iterator.hasNext()){
			Object entity=iterator.next(); 
			JSONObject object=new JSONObject();
			for(Entry<String,String> entry:keys.entrySet()){
				object.put(entry.getKey(),ReflectUtil.getByColumn(entity,entry.getValue()));
			}
			array.add(object);
		}
		return array;
	}
	
	/**
	 * 将实体集合转换为JSONObject数组-方法增强
	 * @param iterator
	 * @param clazz
	 * @param keys key=>JSONObject的key  value=>String[]:[0]要展示的字符串 [1]prefix [2]subfix [3]..->需反射的clazz对应的字段名称
	 * @return
	 */
	public static JSONArray entitiesToJSONArrayWithKeys(Collection collection,Class clazz,Map<String,String[]> keys){
		Iterator iterator=collection.iterator();
		JSONArray array=new JSONArray();
		while(iterator.hasNext()){
			Object entity=iterator.next(); 
			JSONObject object=new JSONObject();
			for(Entry<String,String[]> entry:keys.entrySet()){
				String value="";
				if(entry.getValue().length>0){
					value=entry.getValue()[0];
					for(int i=3;i<entry.getValue().length;i++){
						value=value.replace(entry.getValue()[1]+entry.getValue()[i]+entry.getValue()[2],String.valueOf(ReflectUtil.getByColumn(entity,entry.getValue()[i])));
					}
				}
				object.put(entry.getKey(),value);
			}
			array.add(object);
		}
		return array;
	}
	/**
	 * 将数组转换为Collection
	 * @param objs
	 * @return
	 */
	public static List arrayToList(Object[] objs){
		List list=new ArrayList();
		if(null!=objs&&objs.length>0){
			for(Object obj:objs){
				list.add(obj);
			}
		}
		return list;
	}
	
	/**
	 * 从map中取出keys数组中数据作为key的map，提供mapKeyClazz的目的就是要将keys类型转换
	 * @param map
	 * @param keys
	 * @param keysClazz keys的clazz类型
	 * @return
	 */
	public static Map getMapFromKeys(Map map,Object[] keys,Class keysClazz){
		Map value=new HashMap();
		for(Object key:keys){
			if(map.containsKey(ReflectUtil.parseTo(key,keysClazz))){
				value.put(key,map.get(ReflectUtil.parseTo(key,keysClazz)));
			}
		}
		return value;
	}
	
	/**
	 * 将json字符串转换为map
	 * @param jsonStr
	 * @return
	 */
	public static Map<String,String> jsonObjectToMap(String jsonStr){
		Map<String,String> map=new HashMap<String,String>();
		try{
			JSONObject jsonObj=JSONObject.fromObject(jsonStr);
			Set<String> keySet=jsonObj.keySet();
			for(String key:keySet){
				map.put(key,jsonObj.getString(key));
			}
		}catch(Exception e){
			
		}
		return map;
	}
}

package framework.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import play.modules.redis.Redis;
import play.test.Fixtures;
import framework.dictionary.BaseDictionary;
import framework.dictionary.DictionaryFactory;
import framework.dictionary.DictionaryStoreFactory;
import framework.dictionary.IDictionary;
import framework.dictionary.IDictionaryStore;

/**
 * 数据字典辅助类
 * @author jinchaoyang
 *
 */
public class DicUtil {
	
	private final static String DIC_CONFIG_KEY = "dic.config";
	
	private final static String DEFAULT_DIC_CONFIG_FILE="dic/config.yml";
	
	private final static String CONFIG_DIC_KEY ="dictionaries";
	
	private final static String CONFIG_DIC_STORE_KEY="store";
	private final static String CONFIG_DIC_NAME_KEY="name";
	
	
	private final static Map<String,Map<String,String>> DIC_CONFIGS = new HashMap<String,Map<String,String>>();
	
	private static IDictionaryStore store;
	
	private final static String DEFAULT_STORE_TYPE="redis";
	private final static String DEFAULT_DIC_TYPE="SQL";
	
	
	

	/**
	 * @throws SQLException 
	 * 所有数据文件全部重新加载
	 * @throws  
	 */
	public static void load(){
		String filePath = ContextUtil.getProperty(DIC_CONFIG_KEY, DEFAULT_DIC_CONFIG_FILE);
	    Map<String,Object> config = (Map<String, Object>) Fixtures.loadYamlAsMap(filePath);
	    String type = (String)config.get(CONFIG_DIC_STORE_KEY);
		if(null==store){
		    store = DictionaryStoreFactory.getInstance().getStore(type);
		}
	    List<Map<String,String>> dictionarys = (List<Map<String, String>>) config.get(CONFIG_DIC_KEY);
	    if(null!=dictionarys&&!dictionarys.isEmpty()){
	    	clear(dictionarys); //清空内存数据库中的数据
		    for(Map<String,String> dic : dictionarys){
		    	DIC_CONFIGS.put(dic.get(CONFIG_DIC_NAME_KEY),dic);
		    	IDictionary dictionary  = DictionaryFactory.getInstance().getDictionary(dic,store);
		    	dictionary.load();
		    }
	    }
	}
	
	/**
	 * 企业账号数据字典加载
	 * @param dic
	 */
	public static void loadByDic(String name,String sql){
		if(null == store){
			store = DictionaryStoreFactory.getInstance().getStore(DEFAULT_STORE_TYPE);
		}
		Map<String,String> dic = new HashMap<String,String>();
		dic.put("name", name);
		dic.put("type",DEFAULT_DIC_TYPE);
		dic.put("data", sql);
		IDictionary dictionary = DictionaryFactory.getInstance().getDictionary(dic, store);
		dictionary.load();
	}
	
	/**
	 * 删除所有的加载数据
	 */
	private static void clear(List<Map<String,String>> dictionarys){
		for(Map<String,String> dics:dictionarys){
			if(dics.containsKey(CONFIG_DIC_NAME_KEY)){
				Set<String> dicNames = store.dicNames(dics.get(CONFIG_DIC_NAME_KEY)+"*");
				store.del(dicNames.toArray(new String[0]));
			}
		}
	}
	/**
	 * 删除指定的字典信息
	 * @param dicName
	 */
	private static void clearByDicName(String dicName){
		Set<String> dicNames = store.dicNames("*"+dicName+"*");
		store.del(dicNames.toArray(new String[0]));
	}
	
	/**
	 * 根据字典名称获取相应的字典信息
	 * @param dicName
	 * @return
	 */
	private static IDictionary getDictionary(String dicName){
		String dicConfig = dicName;
		if(dicName.contains(".")){
			dicConfig = dicConfig.substring(0,dicConfig.indexOf("."));
		}
		Map<String,String> config = DIC_CONFIGS.get(dicConfig);
		if(null==config||!config.containsKey(CONFIG_DIC_NAME_KEY)){
			config=new HashMap<String,String>();
			config.put(CONFIG_DIC_NAME_KEY,dicName);
		}
		return DictionaryFactory.getInstance().getDictionary(config, store);
	}
	
	/**
	 * 
	 * 重新加载所有的数据字典
	 * 重新加载会读取内存中的字典配置,如果配置不存在会导致加载失败
	 */
	public static void reload(){
		clear((List<Map<String, String>>) DIC_CONFIGS.get(CONFIG_DIC_KEY));
		if(DIC_CONFIGS.isEmpty()){
			load();
		}else{
			Set<String> dicNames = DIC_CONFIGS.keySet();
			for(String dicName : dicNames){
				loadByDicName(dicName);
			}
			
		}
	}
	

	
	/**
	 * 单独加载某个数据字典
	 * 重新加载会读取内存中的字典配置,如果配置不存在会导致加载失败
	 * @param dicName
	 */
	public static void loadByDicName(String dicName){
		IDictionary dictionary = getDictionary(dicName);
		clearByDicName(dicName);
		dictionary.load();
	}
	
	
	/**
	 * 删除某个字典下的所有数据
	 * @param key
	 */
	public static long delDic(String dicName){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.del();
	}
	
	/**
	 * 删除某个字典项中某个KEY
	 * @param dicName
	 * @param key
	 * @return
	 */
	public static long delDicKey(String dicName,String key){
		dicName = StringUtil.trim(dicName, "");
		key =  StringUtil.trim(key,"");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.delKey(key);
	}
	
	/**
	 * 获取某个字典中所有的KEY
	 * @param dicName
	 * @return
	 */
	public static Set<String> getKeys(String dicName){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.getKeys();
	}
	
	public static Set<String> getKeys(String dicConfig,String dicName){
		dicConfig = StringUtil.trim(dicConfig,"");
		dicName = StringUtil.trim(dicName, ""); 
		IDictionary dictionary = getDictionary(dicConfig);
		return dictionary.getKeys(dicName);
		
	}
	
	
	/**
	 * 获取某个字典中所有的VALUE
	 * @param dicName
	 * @return
	 */
	public static List<String> getValues(String dicName) {
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return  dictionary.getValues();
	}	
	
	/**
	 * 根据字典名称和key获得 key对应的value
	 * @return
	 */
	public static String  getValueByKey(String dicName,String key){
		dicName = StringUtil.trim(dicName, "");
		key =  StringUtil.trim(key,"");
		IDictionary dictionary = getDictionary(dicName);
		if(dicName.contains(".")){
			return dictionary.getItem(dicName,key);
		}else{
			return dictionary.getItem(key);
		}
	}
	/**
	 * 根据字典名称获取字典key-value
	 * @return
	 */
	public static Map<String,String> getAllByDicName(String dicName){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.getAll();
	}
	
	/**
	 * 根据字典名称获取字典key-value
	 * @param dicConfig 字典配置中的名称
	 * @param dicName   实际的字典名
	 * @return
	 */
	public static Map<String,String> getAllByDicName(String dicConfig,String dicName){
		dicConfig = StringUtil.trim(dicConfig,"");
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicConfig);
		return dictionary.getAll(dicName);
	}
	
	
	/**
	 * 获取所有的字典名称
	 * @param pattern
	 * @return
	 */
	public static Set<String> getDicNames(String pattern){
		return store.dicNames(pattern);
	}
	
	public static boolean exists(String dicName){
		dicName = StringUtil.trim(dicName);
		return store.exists(dicName);
	}
	
	
	public static boolean hexists(String dicName,String key){
		dicName = StringUtil.trim(dicName, "");
		key = StringUtil.trim(key,"");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.hexists(dicName, key);
	}
	
	public static long setValueByKey(String dicName,String key,String value){
		dicName = StringUtil.trim(dicName, "");
		key = StringUtil.trim(key,"");
		value = StringUtil.trim(value);
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.setItem(key, value);
	}
	
	
	public static long rpush(String dicName,String value){
		dicName = StringUtil.trim(dicName, "");
		value = StringUtil.trim(value);
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.rpush(dicName, value);
	
	}
	public static long lpush(String dicName,String value){
		dicName = StringUtil.trim(dicName, "");
		value = StringUtil.trim(value);
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lpush(dicName, value);
	}
	public static String rpop(String dicName){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.rpop(dicName);
	}
	public  static String lpop(String dicName){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lpop(dicName);
	}
	public static long llen(String dicName){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.llen(dicName);
	}
	public static  List<String> lrange(String dicName,long start,long end){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lrange(dicName,start,end);
	}

	public static long lrem(String dicName,long count,String value){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lrem(dicName,count,value);
	}
	public static String lset(String dicName,long index,String value){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lset(dicName,index,value);
	}

	public static String lindex(String dicName,long index){
		dicName = StringUtil.trim(dicName, "");
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lindex(dicName,index);
	}
	
	public static int lindexOf(String dicName,String key){
		dicName = StringUtil.trim(dicName);
		key = StringUtil.trim(key);
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.lindexOf(dicName,key);
	}
	
	public  static String hmset (String dicName,Map<String,String> elements){
		dicName = StringUtil.trim(dicName);
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.hmset(dicName,elements);
	}
	
	public static List<String> hmget (String dicName,String[] keys){
		dicName = StringUtil.trim(dicName);
		IDictionary dictionary = getDictionary(dicName);
		return dictionary.hmget(dicName, keys);
	}

	
	
	
}

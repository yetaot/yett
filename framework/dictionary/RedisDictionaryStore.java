package framework.dictionary;

import java.util.HashMap;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.Synchronize;

import framework.base.Constant;
import framework.utils.StringUtil;

import play.modules.redis.Redis;

public  class RedisDictionaryStore implements IDictionaryStore {
	
	private static RedisDictionaryStore store;
	
	private RedisDictionaryStore() {
		
	}
	
	public static RedisDictionaryStore getInstance() {
		if (store == null) {
			store = new RedisDictionaryStore();
		}
		return store;	
	}
	
	
	public  long setItem(String dicName, String key, String value) {
	  String indexName = Constant.DIC_INDEX+dicName;
	  List<String> keys = Redis.lrange(indexName, 0, -1);
	  if(!keys.contains(key)){
		  Redis.rpush(indexName, key);
	  }
	  return Redis.hset(Constant.DIC_PREFIX+dicName, key, value);		
	}
	
	public  String getItem(String dicName,String key){
		return Redis.hget(Constant.DIC_PREFIX+dicName, key);
	}
	
	public  List<String> getValues(String dicName){
		return Redis.hvals(Constant.DIC_PREFIX+dicName);
	}
	
	public  Set<String> getKeys(String dicName){
		return Redis.hkeys(Constant.DIC_PREFIX+dicName);
	}
	
	public  Map<String,String> getAll(String dicName){
		Map<String,String> result = new LinkedHashMap<String,String>();
		long len = Redis.llen(Constant.DIC_INDEX+dicName);
		//以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素
		//假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表
		List<String> keys = Redis.lrange(Constant.DIC_INDEX+dicName, 0,len-1);
		Map<String,String> elements = Redis.hgetAll(Constant.DIC_PREFIX+dicName);
		for(String key:keys){
			result.put(key, elements.get(key));
		}
		return 	result;
	}
	
	public  long delKey(String dicName,String key){
		return Redis.hdel(Constant.DIC_PREFIX+dicName, key);
	}
	
	
	
	
	public  long del(String dicName){
		String[] dicNames = {dicName};
		return del(dicNames);
	}
	
	public  long del(String[] dicNames){
		List<String> indexList = new ArrayList<String>();
		for(int i=0;i<dicNames.length;i++){
			if(!dicNames[i].startsWith(Constant.DIC_PREFIX)&&!dicNames[i].startsWith(Constant.DIC_INDEX)){
				String _dicName = dicNames[i];
				dicNames[i]=Constant.DIC_PREFIX+_dicName;
				indexList.add(Constant.DIC_INDEX+_dicName);
			}else{
				indexList.add(dicNames[i].replace(Constant.DIC_PREFIX, Constant.DIC_INDEX));
			}
		}
		
		if(dicNames.length==0){
			return 0;
		}else{
			if(!indexList.isEmpty()){
				Redis.del(indexList.toArray(new String[0]));
			}
			return Redis.del(dicNames);
		}
	}
	
	public  Set<String> dicNames(String pattern) {
		return Redis.keys(Constant.DIC_BASE_PREFIX+pattern);
	}

	public   boolean hexists(String dicName,String key){
		return Redis.hexists(Constant.DIC_PREFIX+dicName, key);
	}
	/**
	 * 判断字典是否存在
	 * @param dicName
	 * @return
	 */
	public boolean exists(String dicName){
		dicName = StringUtil.trim(dicName);
		return Redis.exists(dicName);
	}
	
	
	
	@Override
	public long rpush(String dicName, String value) {
		return Redis.rpush(Constant.DIC_INDEX+dicName, value);
	}

	@Override
	public  long lpush(String dicName, String value) {
		return Redis.lpush(Constant.DIC_INDEX+dicName, value);
	}

	@Override
	public String rpop(String dicName) {
		return Redis.rpop(Constant.DIC_INDEX+dicName);
	}

	@Override
	public  String lpop(String dicName) {
		return Redis.lpop(Constant.DIC_INDEX+dicName);
	}

	@Override
	public  long llen(String dicName) {
		return Redis.llen(Constant.DIC_INDEX+dicName);
	}

	@Override
	public  List<String> lrange(String dicName, long start, long end) {
		return Redis.lrange(Constant.DIC_INDEX+dicName, start, end);
	}

	@Override
	public  long lrem(String dicName, long count, String value) {
		return Redis.lrem(Constant.DIC_INDEX+dicName, count, value);

	}

	@Override
	public  String lset(String dicName, long index, String value) {
		return Redis.lset(Constant.DIC_INDEX+dicName, index, value);
	}

	@Override
	public  String lindex(String dicName, long index) {
		return Redis.lindex(Constant.DIC_INDEX+dicName, index);
	}

	@Override
	public  int lindexOf(String dicName, String key) {
		List<String> elements = lrange(dicName,0,-1);
		return elements.indexOf(key);
	}
	
	public  String hmset (String dicName,Map<String,String> elements){
		Set<String> keys = elements.keySet();
		for(String key : keys ){
			String indexName = Constant.DIC_INDEX+dicName;
			List<String> names = Redis.lrange(indexName, 0, -1);
			if(!names.contains(key)){
				Redis.rpush(indexName, key);
			}
		}
		return Redis.hmset(Constant.DIC_PREFIX+dicName,elements);
	}
	
	public  List<String> hmget (String dicName,String[] keys){
		return Redis.hmget(Constant.DIC_PREFIX+dicName, keys);
	}
}

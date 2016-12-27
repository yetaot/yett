package framework.dictionary;

import java.util.List;
import java.util.Map;
import java.util.Set;

import play.modules.redis.Redis;

import framework.utils.StringUtil;

public abstract class BaseDictionary implements IDictionary {
	
	protected IDictionaryStore store;
	
	protected String name;
	
	public BaseDictionary(){
		
	}
	
	public BaseDictionary(IDictionaryStore store,String name){
		this.store = store;
		this.name = name;
	}
	/**
	 * 加载字典数据
	 */
	public void load(){
		
	};	

	/**
	 * 删除某个字典下的所有数据
	 * @param key
	 */
	public long del(){
		return store.del(this.name);
	}
	
	/**
	 * 删除某个字典项中某个KEY
	 * @param dicName
	 * @param key
	 * @return
	 */
	public  long delKey(String key){
		key =  StringUtil.trim(key,"");
		return store.delKey(this.name, key);
	}
	
	/**
	 * 获取某个字典中所有的KEY
	 * @param dicName
	 * @return
	 */
	public  Set<String> getKeys(){
		return store.getKeys(this.name);
	}
	
	public  Set<String> getKeys(String dicName){
		return store.getKeys(dicName);
	}

	/**
	 * 获取某个字典中所有的VALUE
	 * @param dicName
	 * @return
	 */
	public  List<String> getValues() {
		return  store.getValues(this.name);
	}	
	
	public  List<String> getValues(String dicName) {
		return  store.getValues(dicName);
	}	
	
	/**
	 * 根据字典名称和key获得 key对应的value
	 * @return
	 */
	public  String  getItem(String key){
		key =  StringUtil.trim(key,"");
		return store.getItem(this.name,key);
	}
	/**
	 * 根据字典名称和key获得 key对应的value
	 * @param dicName 字典名
	 * @param key 字典的KEY
	 * @return
	 */
	public String getItem(String dicName,String key){
		dicName = StringUtil.trim(dicName,"");
		key =  StringUtil.trim(key,"");
		return store.getItem(dicName,key);

	}
	
	/**
	 * 根据字典名称获取字典key-value
	 * @return
	 */
	public  Map<String,String> getAll(){
		return store.getAll(this.name);
	}
	/**
	 *  根据字典名称获取字典key-value
	 * @param dicName
	 */
	public  Map<String,String> getAll(String dicName){
		return store.getAll(dicName);
	}
	/**
	 * 为字典的某个KEY设置VALUE
	 * @param key
	 * @param value
	 * @return
	 */
	public long setItem(String key,String value){
		return store.setItem(this.name, key, value);
	}
	
	@Override
	public long rpush(String dicName, String value) {
		return store.rpush(dicName, value);
	}

	@Override
	public long lpush(String dicName, String value) {
		return store.lpush(dicName, value);
	}

	@Override
	public String rpop(String dicName) {
		return store.rpop(dicName);
	}

	@Override
	public String lpop(String dicName) {
		return store.lpop(dicName);
	}

	@Override
	public long llen(String dicName) {
		return store.llen(dicName);
	}

	@Override
	public List<String> lrange(String dicName, long start, long end) {
		return store.lrange(dicName, start, end);
	}

	@Override
	public long lrem(String dicName, long count, String value) {
		return store.lrem(dicName, count, value);

	}

	@Override
	public String lset(String dicName, long index, String value) {
		return store.lset(dicName, index, value);
	}

	@Override
	public String lindex(String dicName, long index) {
		return store.lindex(dicName, index);
	}
	
	@Override
	public boolean hexists(String dicName,String key){
		return store.hexists(dicName, key);
	}
	@Override
	public int lindexOf(String dicName,String key){
		return store.lindexOf(dicName, key);
	}

	public String hmset (String dicName,Map<String,String> elements){
		return store.hmset(dicName,elements);
	}
	
	public List<String> hmget (String dicName,String[] keys){
		return store.hmget(dicName, keys);
	}
	
}

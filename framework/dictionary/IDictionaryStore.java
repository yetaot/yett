package framework.dictionary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.modules.redis.Redis;

public interface IDictionaryStore {
	/**
	 *  设置数据字典的值
	 * @param dicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long setItem(String dicName, String key, String value);
	
	/**
	 *  获取字典某个KEY的值
	 * @param dicName
	 * @param key
	 * @return
	 */
	public String getItem(String dicName,String key);
	
	/**
	 *  获取某个字典中所有的值
	 * @param dicName
	 * @return
	 */
	public List<String> getValues(String dicName);
	
	/**
	 *  获取某个字典项中所有的KEY值
	 * @param dicName
	 * @return
	 */
	 
	public Set<String> getKeys(String dicName);
	
	/**
	 * 获取某个字典项中所有的KEY和VALUE
	 * @param dicName
	 * @return
	 */
	public Map<String,String> getAll(String dicName);
	
	
	 /**
	  *  所有的KEY值
	  * @param pattern
	  * @return
	  */
	public Set<String> dicNames(String pattern);
	
	/**
	 * 判断字典是否存在
	 * @param dicName
	 * @return
	 */
	public boolean exists(String dicName);
	
	 /**
	  * 删除某个字典项
	  * @param dicName
	  * @return
	  */
	public long del(String dicName);
	 
	/**
	 *  删除某些KEY值
	 * @param dicNames
	 * @return
	 */
	public long del(String[] dicNames);
	
	/**
	 *  删除某个字典项中的KEY
	 * @param dicName
	 * @param key
	 * @return
	 */
	public long delKey(String dicName,String key);
	
	public boolean hexists(String dicName,String key);

	
	/**
	 * 向LIST末端增加值
	 * @param dicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long rpush(String dicName,String value);
	/**
	 * 向List首部增加值
	 * @param dicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long lpush(String dicName,String value);
	/**
	 * 移除并返回列表的尾元素
	 * @param dicName
	 * @return
	 */
	public String rpop(String dicName );
	/**
	 * 移除并返回列表的头元素
	 * @param dicName
	 * @return
	 */
	public String lpop(String dicName);
	/**
	 * 返回列表的长度
	 * @param dicName
	 * @return
	 */
	public long llen(String dicName);
	/**
	 * 返回列表中指定区间内的元素
	 * @param dicName
	 * @return
	 */
	public List<String> lrange(String dicName,long start,long end);

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
	 * count = 0 : 移除表中所有与 value 相等的值。
	 * @param dicName
	 * @param value
	 * @return
	 */
	public long lrem(String dicName,long count,String value);
	/**
	 * 将列表下标为 index 的元素的值设置为 value
	 * @param dicName
	 * @param index
	 * @param value
	 * @return
	 */
	public String lset(String dicName,long index,String value);
	/**
	 * 返回列表下标为index的元素
	 * @param dicName
	 * @param index
	 * @return
	 */
	public String lindex(String dicName,long index);
	
	
	public int lindexOf(String dicName,String key);
	
	public String hmset (String dicName,Map<String,String> elements);
	
	public List<String> hmget (String dicName,String[] keys);	
	
	
}

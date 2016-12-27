package framework.dictionary;

import java.util.List;
import java.util.Map;
import java.util.Set;

import play.modules.redis.Redis;

import framework.utils.StringUtil;


public interface IDictionary {
	
	/**
	 * 加载字典数据
	 */
	public void load();
	
	/**
	 * 删除某个字典下的所有数据
	 * @param key
	 */
	public long del();
	
	
	/**
	 * 删除某个字典项中某个KEY
	 * @param dicName
	 * @param key
	 * @return
	 */
	public  long delKey(String key);
	
	/**
	 * 获取某个字典中所有的KEY
	 * @param dicName
	 * @return
	 */
	public  Set<String> getKeys();
	
	public Set<String> getKeys(String dicName);
	/**
	 * 获取某个字典中所有的VALUE
	 * @param dicName
	 * @return
	 */
	public  List<String> getValues();
	
	public List<String> getValues(String dicName);
	
	/**
	 * 根据字典名称获取字典key-value
	 * @return
	 */
	public  Map<String,String> getAll();
	
	/**
	 * 根据字典名称获取字典key-value
	 * @return
	 */
	public  Map<String,String> getAll(String dicName);
	
	/**
	 * 根据字典名称和key获得 key对应的value
	 * @return
	 */
	public String getItem(String key);
	/**
	 * 根据字典名称和key获得 key对应的value
	 */
	 public String getItem(String dicName,String key);
	
	public long setItem(String key ,String value);
	
	
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
	
	/**
	 * 返回列表key元素的下标
	 * @param dicName
	 * @param key
	 * @return
	 */
	public int lindexOf(String dicName,String key);
	
			
	public String hmset (String dicName,Map<String,String> elements);
	
	public List<String> hmget (String dicName,String[] keys);	
}

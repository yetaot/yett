package framework.cache;

public interface Cache {
	
	public String hget(String key);
	
	public void hset(String key, String value);
	
	public void hdel(String key);

}

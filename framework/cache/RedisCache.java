package framework.cache;

import play.modules.redis.Redis;
import play.modules.redis.RedisConnectionManager;
import redis.clients.jedis.Jedis;

public class RedisCache implements Cache {
	
	
	private String name;
	
	public RedisCache(String name) {
		this.name = name;
	}

	@Override
	public String hget(String key) {
		return Redis.hget(this.name, key);
	}

	@Override
	public void hset(String key, String value) {
		Redis.hset(this.name, key, value);
	}
	
	public void hdel(String key) {
		Redis.hdel(this.name, key);
	}

}

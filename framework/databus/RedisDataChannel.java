package framework.databus;

import play.modules.redis.Redis;


import redis.clients.jedis.Jedis;

public class RedisDataChannel implements DataChannel {
	
	private String channelId;
	
	public RedisDataChannel(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String pop() {
		return  Redis.lpop(channelId);
		
}

	@Override
	public void push(String value) {
		Redis.rpush(channelId, value);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}

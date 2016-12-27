package framework.security.store;

import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import framework.security.models.SecurityMenu;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

import play.modules.redis.Redis;

public class RedisStore implements ISecurityStore  {
	private static RedisStore store;
	private RedisStore() {
		
	}
	public static RedisStore getInstance() {
		if (store == null) {
			store = new RedisStore();
		}
		return store;	
	}

	@Override
	public List<String> getAllKeys(String key) {
		return Redis.lrange(key,0,-1);
	}

	@Override
	public Map<String, String> getAllByKeys(String key) {
		return Redis.hgetAll(key);
	}
	@Override
	public void storeResource(Map<SecurityMenu,List<SecurityMenu>> resources) {
		clearResource();
		for(Entry<SecurityMenu, List<SecurityMenu>> entry:resources.entrySet()){
			SecurityMenu resource=entry.getKey();
				Redis.hset(SecurityUtil.resouceDicFix+resource.code,"id",resource.id);
				Redis.hset(SecurityUtil.resouceDicFix+resource.code,"url",resource.url);
				Redis.hset(SecurityUtil.resouceDicFix+resource.code,"name",resource.name);
				Redis.hset(SecurityUtil.resouceDicFix+resource.code,"code",resource.code);
				Redis.hset(SecurityUtil.resouceDicFix+resource.code,"app",StringUtil.trim(resource.app));
				Redis.hset(SecurityUtil.resouceDicFix+resource.code,"attrs",resource.attrs);
			if(null!=entry.getValue()&&!entry.getValue().isEmpty()){
				for(SecurityMenu node:entry.getValue()){
						Redis.hset(SecurityUtil.resouceDicFix+node.code,"name",node.name);
						Redis.hset(SecurityUtil.resouceDicFix+node.code,"id",node.id);
						Redis.hset(SecurityUtil.resouceDicFix+node.code,"url",node.url);
						Redis.hset(SecurityUtil.resouceDicFix+node.code,"code",node.code);
						Redis.hset(SecurityUtil.resouceDicFix+node.code,"app",StringUtil.trim(node.app));
						Redis.hset(SecurityUtil.resouceDicFix+node.code,"attrs",node.attrs);
						Redis.rpush(SecurityUtil.resourcesFix,resource.code+","+node.code);
				}
			}else{
				Redis.rpush(SecurityUtil.resourcesFix,resource.code+",");
			}
		}
	}

	@Override
	public void clearResource() {
		Set<String> keys=Redis.keys(SecurityUtil.RESOURCE_STORE_PREFIX+"*");
		if(!keys.isEmpty()){
			Redis.del(keys.toArray(new String[0]));
		}
		Set<String> services=Redis.keys(SecurityUtil.serviesFix+"*");
		if(!services.isEmpty()){
			Redis.del(services.toArray(new String[0]));
		}
	}


}

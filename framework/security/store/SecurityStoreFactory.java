package framework.security.store;

import java.util.List;




import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import play.modules.redis.Redis;

public class SecurityStoreFactory {
public final static String REDIS_STORE = "REDIS";
	
	private static SecurityStoreFactory store;
			
	private SecurityStoreFactory(){
		
	}
	
	public  ISecurityStore getStore(String type){
		if (type.toUpperCase().equals(REDIS_STORE)) {
			return RedisStore.getInstance();
		}			
		return null;
	}
	
	public static SecurityStoreFactory getInstance() {
		if (null == store){
			store = new SecurityStoreFactory();
		}
		return store;
	}
}

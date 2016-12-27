package framework.dictionary;

public class DictionaryStoreFactory {
	
	public final static String REDIS_STORE = "REDIS";
	
	private static DictionaryStoreFactory dictionaryStore;
			
	private DictionaryStoreFactory(){
		
	}
	
	public  IDictionaryStore getStore(String type){
		if (type.toUpperCase().equals(REDIS_STORE)) {
			return RedisDictionaryStore.getInstance();
		}			
		return null;
	}
	
	public static DictionaryStoreFactory getInstance() {
		if (null == dictionaryStore){
			dictionaryStore = new DictionaryStoreFactory();
		}
		return dictionaryStore;
	}

}

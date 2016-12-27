package framework.dictionary;

import java.util.Map;

public class DictionaryFactory {
	
	private final static String DIC_SQL="SQL";
	private final static String DIC_YAML="YAML";
	private final static String DIC_TYPE="type";
	private final static String DIC_DB="DBDICTIONARY";
	
	private static  DictionaryFactory dictionaryFactory;
	
	
	private  DictionaryFactory() {
		
	}
	
	
	public  IDictionary getDictionary(Map<String,String> config,IDictionaryStore store){
		if(null!=config&&config.containsKey(DIC_TYPE)){
			String dicType = config.get(DIC_TYPE);
			if(dicType.toUpperCase().trim().equals(DIC_SQL)){
				return new SQLDictionary(config,store);
			}else if(dicType.toUpperCase().trim().equals(DIC_YAML)){
				return new YAMLDictionary(config,store);
			}else if(dicType.toUpperCase().trim().equals(DIC_DB)){
				return new DBDictionary(config,store);
			}
		}else if(null!=config&&!config.containsKey(DIC_TYPE)){
			return new ReadOnlyDictionary(config,store);
		}
		return new ReadOnlyDictionary(store);
	}
	
	public static DictionaryFactory getInstance() {
		if (null == dictionaryFactory) {
			dictionaryFactory =  new DictionaryFactory();
		}
		return dictionaryFactory;
		
	}
	
	

}

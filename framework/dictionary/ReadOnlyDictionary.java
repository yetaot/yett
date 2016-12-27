package framework.dictionary;

import java.util.Map;

import framework.dictionary.exception.NotSupportOperationException;

public class ReadOnlyDictionary extends BaseDictionary {
	private final static String DIC_NAME_KEY="name";
	public ReadOnlyDictionary(IDictionaryStore store) {
		this.store = store;
	}
	public ReadOnlyDictionary(Map<String,String> config,IDictionaryStore store) {
		super(store,config.get(DIC_NAME_KEY));
	}
	
	/**
	 * 加载字典数据
	 */
	public void load(){
		throw new NotSupportOperationException();
	}
}

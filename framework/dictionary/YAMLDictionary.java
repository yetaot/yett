package framework.dictionary;

import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import framework.base.Constant;

import net.sf.json.JSONObject;
import play.test.Fixtures;

public class YAMLDictionary extends BaseDictionary {
	private final static String FILE_NAME_KEY="data";
	private final static String DIC_NAME_KEY ="name";
	//文件名
	private String fileName;
	
	public YAMLDictionary() {
		
	}
	
	public YAMLDictionary(Map<String,String> config,IDictionaryStore store) {
		super(store,config.get(DIC_NAME_KEY));
		this.fileName = config.get(FILE_NAME_KEY);
		
	}
	
	public void load() {
		Map<String,Object> result =  (Map<String,Object>) Fixtures.loadYamlAsMap(fileName);
		if(null!=result){
			Set<Entry<String, Object>> dataSet  = result.entrySet();
			for(Entry<String,Object> e : dataSet){
				String dicName = e.getKey();
				List<LinkedHashMap<String,String>> dataList = (List<LinkedHashMap<String,String>>)e.getValue();
				for(LinkedHashMap<String,String> o :dataList){
					String key = o.get("K");
					String value = o.get("V");
					store.setItem(this.name+"."+dicName, key, value);
				}
			}
		}
		//List<LinkedHashMap<String,String>> dataList = (List<LinkedHashMap<String,String>>) result.get(this.name);
//		for(LinkedHashMap<String,String> o : dataList){
//			String key = o.get("K");
//			String value = o.get("V");
//			store.setItem(this.name, key, value);
//		}
		
		
	}



}

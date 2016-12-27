package framework.dictionary;

import java.sql.ResultSet;



import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import play.modules.redis.Redis;
import framework.base.Constant;
import framework.dictionary.exception.DictionaryException;
import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;
import framework.security.models.Sequence;
import framework.utils.DBUtil;
import framework.utils.StringUtil;
/**
 * 数据库配置数据字典，字典首先通过配置文件预加载到数据库，对字典项的更新与变动，首先保存到数据库，再更新到内存数据库
 * @author 张科伟
 * @time 2013-03-22 11:25
 */
public class DBDictionary extends BaseDictionary{
	
	private static final Logger logger = LogUtil.getInstance(DBDictionary.class);
	private final static String DIC_NAME_KEY="name";
	private final static String DIC_MODEL_KEY ="data";
	

	//对应的数据库模型
	private String model;
	
	
	public DBDictionary() {
		
	}
	
	public DBDictionary(Map<String,String> config,IDictionaryStore store) {
		super(store,config.get(DIC_NAME_KEY));
		this.model = config.get(DIC_MODEL_KEY);
	}	

	@Override
	/**
	 * 加载字典数据
	 */
	public void load() {
		try{
			//加载指定数据模型的字典
			if(model.equals("DicParam")){
				String rootCodeSql="select new Map(dicCode,dicName,ifSystem) from DicParam where parentCode is null or parentCode='' or parentCode=null ";
				String childCodeSql="select new Map(dicCode,dicName,ifSystem) from DicParam where parentCode=? order by orderBy asc";
				//求跟元素
				List<Map<String,String>> roots=Sequence.find(rootCodeSql).fetch();
				for(Map<String,String> root:roots){
					String dicCode=root.get("0");
					String keyRoot=this.name+"."+dicCode;
					List<Map<String,String>> children=Sequence.find(childCodeSql,dicCode).fetch();
					//求子元素
					for(Map<String,String> child:children){
						String childCode=child.get("0");
						String childName=child.get("1");
						List<Map<String,String>> nodes=Sequence.find(childCodeSql,childCode).fetch();
						store.setItem(keyRoot,childCode,childName);
						//求末元素
						String key=this.name+"."+dicCode+"."+childCode;
						for(Map<String,String> node:nodes){
							String nodeCode=node.get("0");
							String nodeName=node.get("1");
							store.setItem(key,nodeCode,nodeName);
						}
					}
				}
			}
		}catch(Exception e){
			ExceptionHandler.throwRuntimeException(e,logger);
		}
	}
		
	
	
}

package framework.dictionary;

import java.sql.ResultSet;


import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import play.modules.redis.Redis;
import framework.base.Constant;
import framework.dictionary.exception.DictionaryException;
import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;
import framework.utils.DBUtil;
import framework.utils.StringUtil;

public class SQLDictionary extends BaseDictionary{
	
	private static final Logger logger = LogUtil.getInstance(SQLDictionary.class);
	private final static String DIC_NAME_KEY="name";
	private final static String DIC_SQL_KEY ="data";
	

	//对应的SQL
	private String sql;
	
	
	public SQLDictionary() {
		
	}
	
	public SQLDictionary(Map<String,String> config,IDictionaryStore store) {
		super(store,config.get(DIC_NAME_KEY));
		this.sql = config.get(DIC_SQL_KEY);
	}	

	@Override
	/**
	 * 加载字典数据
	 */
	public void load() {
		 	ResultSet rs = DBUtil.query(sql);
			try {
				while(rs.next()){
					String dicName = rs.getString("T");
					String key = rs.getString("K");
					String value = rs.getString("V");
					dicName = StringUtil.trim(dicName,"");
					key = StringUtil.trim(key,"");
					value = StringUtil.trim(value,"");
					if(!(StringUtil.isBlank(key)&&StringUtil.isBlank(value))){
						if(StringUtil.isBlank(dicName)||StringUtil.isBlank(key)){
							throw new DictionaryException("所要加载的数据字典的dicName或key为空");
						}
						if(!this.name.equals(dicName)){
							dicName = this.name+"."+dicName;
						}
						store.setItem(dicName, key, value);
					}
				}
			} catch(DictionaryException de) {
				ExceptionHandler.throwRuntimeException(de,logger);
			} catch (SQLException e) {
				ExceptionHandler.throwRuntimeException(e, "数据字典加载SQL异常",logger);
			}
	}
		
	
	
}

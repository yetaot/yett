package models.utils;

import java.lang.annotation.Annotation;

import javax.persistence.Table;

import org.hibernate.EmptyInterceptor;

public class QueryResInterceptor extends EmptyInterceptor {
	
	public String tableName;
	public String  name;
	
	public  QueryResInterceptor(String name,String tableName){
		this.tableName = tableName;
		this.name= name;
	}

	@Override
	public String onPrepareStatement(String sql) {
		name = name.toLowerCase();
		sql = sql.replace(name, tableName);
		return sql;
	}

	
	
	
	
}

package framework.base;

import java.io.Serializable;
import java.util.List;

import play.db.jpa.GenericModel.JPAQuery;

public abstract class BaseQuery implements Serializable{
	
	public BaseQuery(){
		
	}
	
	public abstract String toQuery();
	
	public abstract List<Object> toCountQuery();

	
	

	
}

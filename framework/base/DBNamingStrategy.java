package framework.base;

import org.hibernate.annotations.common.AssertionFailure;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.util.StringHelper;

public class DBNamingStrategy extends ImprovedNamingStrategy {
	
	private static final long serialVersionUID = 1L;

	public String foreignKeyColumnName( String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName ) {
		String header = propertyName != null ? StringHelper.unqualify( propertyName ) : propertyTableName;
		if (header == null) throw new AssertionFailure("NamingStrategy not properly filled");
		return columnName( header+"_id" ); 
	}
	
	public String propertyToColumnName(String propertyName) {
		if (propertyName.endsWith("__")) {
	        return propertyName.replace("__", "");  
	    }
	    return addUnderscores(StringHelper.unqualify(propertyName));
	}
	
}

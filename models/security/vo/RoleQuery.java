package models.security.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import framework.base.BaseQuery;
import framework.base.Constant;
import framework.utils.ArrayUtil;
import framework.utils.DicUtil;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class RoleQuery extends BaseQuery {
	//角色名
	public String name;
	//角色类型
	public String type;
	
	
	public RoleQuery(){
		
	}
	
	public String toString(){
		return UrlUtil.toUrl(this, this.getClass());
	}

	@Override
	public String toQuery() {
		List<Object> result = toCountQuery();
		String sql = (String) result.get(0);
		sql = sql+" order by a.createdAt desc";
		return sql;
	}

	@Override
	public List<Object> toCountQuery() {
		List<Object> queryParams = new ArrayList<Object>();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("from Role a where a.ifUse = ? and a.ifSystem=? ");
			args.add(1);
			args.add(0);
		if (!StringUtil.isBlank(name)) {
			sql.append(" and a.name like ?");
			args.add("%"+name+"%");
		}
		if (!StringUtil.isBlank(type)) {
			sql.append(" and a.type = ?");
			args.add(type);
		}
		
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}

}
package models.em.vo;

import java.util.ArrayList;
import java.util.List;

import framework.base.BaseQuery;
import framework.utils.StringUtil;

public class IPTableQuery extends BaseQuery {
	
	public String ip;

	@Override
	public String toQuery() {
		List<Object> result = toCountQuery();
		String sql = (String) result.get(0);
		sql = sql + " ORDER BY config.createdAt DESC";
		return sql;
	}

	@Override
	public List<Object> toCountQuery() {
		List<Object> params = new ArrayList<Object>();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("FROM IPTable config WHERE config.ifUse = ?");
		args.add(1);
		if (!StringUtil.isBlank(ip)) {
			sql.append(" AND config.ip = ?");
			args.add(ip);
		}
		params.add(sql.toString());
		params.add(args);
		return params;
	}

}

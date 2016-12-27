package models.sms.vo;

import java.util.ArrayList;
import java.util.List;

import framework.base.BaseQuery;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class SmsTemplateQuery extends BaseQuery {
	public String name;
	public String status;
	public String orderBy;
	@Override
	public String toQuery() {
		List<Object> result=toCountQuery();
		String sql = (String) result.get(0);
		if(!StringUtil.isBlank(orderBy) && "0".equals(orderBy)){
			sql = sql+" order by submittedAt asc";
		}else{
			sql = sql+" order by createdAt desc";
		}
		return sql;
	}
	public String toString(){
		return UrlUtil.toUrl(this, this.getClass());
	}
	
	@Override
	public List<Object> toCountQuery() {
		List<Object> queryParams = new ArrayList<Object>();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql=new StringBuffer(" from SmsTemplate where ifUse=?");
		args.add(1);
		if(!StringUtil.isBlank(name)){
			sql.append(" and name like ?");
			args.add("%"+name+"%");
		}
		if(!StringUtil.isBlank(status)){
			sql.append(" and status = ?");
			args.add(status);
		}
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}

}

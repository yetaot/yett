package models.security.vo;

import java.util.ArrayList;
import java.util.List;

import framework.base.BaseQuery;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class UserQuery extends BaseQuery{
    public String userName;
    public String name;
    public String phone;
    public String ifUse;
	@Override
	public String toQuery() {
		List<Object> result=toCountQuery();
		String sql = (String) result.get(0);
		sql = sql+" order by createdAt desc";
		return sql;
	}
	public String toString(){
		return UrlUtil.toUrl(this, this.getClass());
	}
	
	@Override
	public List<Object> toCountQuery() {
		List<Object> queryParams = new ArrayList<Object>();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql=new StringBuffer(" from User where ifUse=? and ifSystem=? ");
		args.add(1);
		args.add(0);
		if(!StringUtil.isBlank(userName)){
			sql.append(" and userName=?");
			args.add(userName);
		}
		if(!StringUtil.isBlank(name)){
			sql.append(" and name= ?");
			args.add(name);
		}
		if(!StringUtil.isBlank(phone)){
			sql.append(" and phone=?");
			args.add(phone);
		}
		if(!StringUtil.isBlank(ifUse)){
			sql.append(" and ifUse=?");
			args.add(Integer.parseInt(ifUse));
		}
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}
	
}
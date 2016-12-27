package models.task.vo;

import java.util.ArrayList;
import java.util.List;

import framework.base.BaseQuery;
import framework.utils.DateUtil;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class TaskQuery extends BaseQuery  {
	
	//开始时间
	public String beginAt;
	//结束时间
	public String endAt;
	//状态
	public String status;
	//创建人ID
	public  String creatorId;

	@Override
	public String toQuery() {
		List<Object> result = toCountQuery();
		String sql = (String) result.get(0);
		sql = sql+" order by a.createdAt desc";
		return sql;
	}
	public String toString(){
		return UrlUtil.toUrl(this, this.getClass());
	}
	@Override
	public List<Object> toCountQuery() {
		List<Object> queryParams = new ArrayList<Object>();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" from Task a where a.ifUse=? ");
		args.add(1);
		if(!StringUtil.isBlank(beginAt)){
			sql.append(" and a.createdAt>=? ");
			args.add(DateUtil.getDate(beginAt+" 00:00:00", DateUtil.FORMAT_TYPE_ALL));
		}
		if(!StringUtil.isBlank(endAt)){
			sql.append(" and a.createdAt<=? ");
			args.add(DateUtil.getDate(endAt+" 23:59:59", DateUtil.FORMAT_TYPE_ALL));
		}
		
		if(!StringUtil.isBlank(creatorId)){
			sql.append(" and a.creatorId=? ");
			args.add(creatorId);
		}
		
		if(!StringUtil.isBlank(status)){
			sql.append(" and a.status=? ");
			args.add(status);
		}
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}
}

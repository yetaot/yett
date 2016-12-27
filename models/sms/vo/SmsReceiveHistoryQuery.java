package models.sms.vo;

import java.util.ArrayList;
import java.util.List;

import framework.base.BaseQuery;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class SmsReceiveHistoryQuery extends BaseQuery {
	//接收人
	public String acceptor;
	//发送人
	public String sender;
	//发送时间
	public String beginTime;
	public String endTime;
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
		StringBuffer sql=new StringBuffer(" from SmsReceiveHistory where 1=1 ");
		if(!StringUtil.isBlank(acceptor)){
			sql.append(" and acceptor like ?");
			args.add("%"+acceptor+"%");
		}
		if(!StringUtil.isBlank(sender)){
			sql.append(" and sender like ?");
			args.add("%"+sender+"%");
		}
		if (!StringUtil.isBlank(beginTime)) {
			sql.append(" and revicedAt >= ?");
			args.add(beginTime+"00:00:00");
		}
		if (!StringUtil.isBlank(endTime)) {
			sql.append(" and revicedAt <= ?");
			args.add(endTime+" 23:59:59");
		}
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}

}

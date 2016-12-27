package models.sms.vo;

import java.util.ArrayList;
import java.util.List;

import models.utils.SecurityConstant;

import framework.base.BaseQuery;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class SmsSendHistoryQuery extends BaseQuery {
	//接收人
	public String acceptor;
	//发送人
	public String sender;
	//发送时间
	public String beginTime;
	public String endTime;
	//发送结果
	public String sendResult;
	//发送源
	public String source;
	//审核状态
	public String auditStatus;
	//组织结构
	public String orgCode;
	
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
		StringBuffer sql=new StringBuffer(" from SmsSendHistory where 1=1 ");
		if(!StringUtil.isBlank(acceptor)){
			sql.append(" and acceptor like ?");
			args.add("%"+acceptor+"%");
		}
		if(!StringUtil.isBlank(sender)){
			sql.append(" and sender like ?");
			args.add("%"+sender+"%");
		}
		if (!StringUtil.isBlank(beginTime)) {
			sql.append(" and sendAt >= ?");
			args.add(beginTime+" 00:00:00");
		}
		if (!StringUtil.isBlank(endTime)) {
			sql.append(" and sendAt <= ?");
			args.add(endTime+" 23:59:59");
		}
		if(!StringUtil.isBlank(sendResult)){
			sql.append(" and sendResult=?");
			args.add(sendResult);
		}
		if(!StringUtil.isBlank(source)){
			sql.append(" and source=?");
			args.add(source);
		}
		if (!StringUtil.isBlank(auditStatus)) {
			sql.append(" and auditStatus = ? ");
			args.add(StringUtil.trim(auditStatus));
		}
		if (!StringUtil.isBlank(orgCode)) {
			if (orgCode.length() == 3) {
				sql.append(" and operator.unitTotal=? ");
				args.add(StringUtil.trim(orgCode));
			} else if (orgCode.length() == 6) {
				sql.append(" and operator.unitBranch=? ");
				args.add(StringUtil.trim(orgCode));
			} else {
				sql.append(" and operator.unitMiddle=? ");
				args.add(StringUtil.trim(orgCode));
			}
		}
		
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}

}

package models.sms.vo;

import java.util.ArrayList;
import java.util.List;

import framework.base.BaseQuery;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class SmsBatchSendLogQuery extends BaseQuery {
	//发送时间
	public String beginTime;
	public String endTime;
	//使用模板
	public String templateId;
	//审核状态
	public String auditStatus;
	//组织结构
	public String orgCode;
	//标志
	public String flag;
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
		StringBuffer sql=new StringBuffer(" from SmsBatchSendLog where 1=1 ");
		if (!StringUtil.isBlank(beginTime)) {
			sql.append(" and sendAt >= ?");
			args.add(beginTime+" 00:00:00");
		}
		if (!StringUtil.isBlank(endTime)) {
			sql.append(" and sendAt <= ?");
			args.add(endTime+" 23:59:59");
		}
		if (!StringUtil.isBlank(templateId)) {
			sql.append(" and templateId = ?");
			args.add(templateId);
		}
		if (!StringUtil.isBlank(auditStatus)) {
			sql.append(" and auditStatus = ? ");
			args.add(StringUtil.trim(auditStatus));
		}
		
		if (!StringUtil.isBlank(orgCode)) {
			if (!StringUtil.isBlank(flag) && "audit".equals(flag)) {
				if (orgCode.length() == 3) {
					sql.append(" and operator.unitTotal = ? ");
					args.add(StringUtil.trim(orgCode));
				} else if (orgCode.length() == 6) {
					sql.append(" and operator.unitMiddle like ? ");
					args.add(StringUtil.trim(orgCode) + "___");
				}
			} else {
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
		}
		
		queryParams.add(sql.toString());
		queryParams.add(args);
		return queryParams;
	}

}

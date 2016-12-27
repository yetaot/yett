package models.order.vo;

import java.util.ArrayList;
import java.util.List;

import models.utils.SecurityConstant;
import models.utils.XmlUtil;
import framework.base.BaseQuery;
import framework.utils.DateUtil;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

public class ReportOrderQuery extends BaseQuery  {
	public String reOption;
	public String reValue;
	public String twounit;
	public String linkphone;
	public String formId;
	public String username;
	public String sqlb;
	public String jjcd;
	public String beginAt;
	public String endAt;
	public String statusId;
	public String bs;
	public String twoUnit;
	@Override
	public String toQuery() {
		List<Object> result = toCountQuery();
		String sql = (String) result.get(0);
		sql = sql + " ORDER BY r.sendDate DESC";
		return sql;
	}
	public String toString(){
		return UrlUtil.toUrl(this, this.getClass());
	}
	@Override
	public List<Object> toCountQuery() {
		List<Object> params = new ArrayList<Object>();
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("FROM ReportOrder r where 1=1 ");
		if(!StringUtil.isBlank(linkphone)){
			sql.append(" AND r.linkphone = ?");
			args.add(linkphone);
		}
		if(!StringUtil.isBlank(formId)){
			sql.append(" AND r.formId = ?");
			args.add(formId);
		}
		if(!StringUtil.isBlank(username)){
			sql.append(" AND r.username = ?");
			args.add(username);
		}
		if(!StringUtil.isBlank(bs)){
			sql.append(" AND r.bs = ?");
			args.add(bs);
		}

		if(!StringUtil.isBlank(statusId)){
			sql.append(" AND r.statusId = ?");
			args.add(statusId);
		}
		if(!StringUtil.isBlank(sqlb)){
			sql.append(" AND r.sqlb = ?");
			args.add(sqlb);
		}
		if(!StringUtil.isBlank(jjcd)){
			sql.append(" AND r.jjcd = ?");
			args.add(jjcd);
		}
		if(!StringUtil.isBlank(beginAt)){
			sql.append(" and r.sendDate>=? ");
			args.add(beginAt+" 00:00:00");
		}
		if(!StringUtil.isBlank(endAt)){
			sql.append(" and r.sendDate<=? ");
			args.add(endAt+" 23:59:59");
		}
		if(!StringUtil.isBlank(twounit)){
			if(twounit.equals(XmlUtil.admin)){
				
			}else if(twounit.equals("系统管理员")){
				sql.append(" AND r.twoUnit is null");
			}else{
				sql.append(" AND r.twoUnit like ?");
				args.add("%"+twounit+"%");
			}
		}
		System.out.println("reValue="+reValue+"=reOption="+reOption);
		if (!StringUtil.isBlank(reOption)) {
			if(reOption.equals("assignee")){
				if(StringUtil.isBlank(twounit)){
					sql.append(" AND r.assignee = ? and r.statusId!='11'");
					args.add(reValue);
				}else {
					sql.append(" AND r.assignee = ?");
					args.add(reValue);
				}
			}
			if(reOption.equals("statusId")){
				sql.append(" AND r.statusId = ?");
				args.add(reValue);
			}
		}
		params.add(sql.toString());
		params.add(args);
		return params;
	}

}

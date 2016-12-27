package models.resource.vo;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Required;
import play.data.validation.Unique;

import framework.base.BaseQuery;
import framework.utils.StringUtil;
import framework.utils.UrlUtil;

/**
 * @author lenzhao 
 * @email zhaosl1017@gmail.com
 * @date  2014-11-13 下午5:26:54
 */
public class JobManageQuery extends BaseQuery {
	public String jobName;
	public Integer ifUse;
	public String roleType;
	public String toQuery() {
	       List<Object> result=toCountQuery();
	       String sql=(String) result.get(0);
	       sql=sql+" order by createdAt desc";
			return sql;
		}
		public String toString(){
			return UrlUtil.toUrl(this, this.getClass());
		}
		@Override
		public List<Object> toCountQuery() {
			List<Object> queryParams=new ArrayList<Object>();
			List<Object> args=new ArrayList<Object>();
			StringBuffer sql=new StringBuffer(" from JobManager where 1=1");
			if(!StringUtil.isBlank(jobName)){
				sql.append(" and jobName like ?");
				args.add("%"+jobName+"%");
			}
			if(null != ifUse){
				sql.append(" and ifUse=?");
				args.add(ifUse);
			}
			if(!StringUtil.isBlank(roleType)){
				sql.append(" and roleType=?");
				args.add(roleType);
			}
			queryParams.add(sql.toString());
			queryParams.add(args);
			return queryParams;
		}

}

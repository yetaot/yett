package framework.base;

import java.util.ArrayList;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import framework.base.Pager;
import framework.security.models.Sequence;
import play.db.jpa.GenericModel;
import play.db.jpa.GenericModel.JPAQuery;
/**
 * 基础Model类
 * @author 张科伟
 *
 */
public abstract class  BaseGenericeModel extends GenericModel {
	  /**
	   * 根据查询条件返回相应的查询结果 User用于指定查询的Model类
	   * @param params 查询参数
	   * @param pageNo 当前页
	   * @param pageSize 每页显示的条目
	   * @return
	   */
	  public static PagerRS<?> findByPager(BaseQuery params, int pageNo, int pageSize){
		  	  List<Object> countObj = params.toCountQuery();
		  	  String querySql = params.toQuery();
		  	  List<Object> args = (List<Object>)countObj.get(1);
		  	  JPAQuery query =Sequence.find(querySql, args.toArray());
		  	  List<?> result = query.fetch(pageNo, pageSize);
		  	  String countSql = (String)countObj.get(0);
			  long totalCount = Sequence.count(countSql, args.toArray());
			  Pager pager = new Pager(totalCount, pageNo, pageSize,params);
			  return new PagerRS(result, pager);
	  }
	  
	  /**
	   * 获取所有的查询结果集
	   * @param params
	   * @return
	   */
	  public static List<?> findByQuery(BaseQuery params){
		  List<Object> countObj = params.toCountQuery();
		  String querySql = params.toQuery();
		  List<Object> args = (List<Object>)countObj.get(1);
		  List<?> result = Sequence.find(querySql,args.toArray()).fetch();
		  return result;
		 
	  }
}

package framework.base;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;


import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import framework.base.Pager;
import framework.logs.LogUtil;
import framework.security.models.Sequence;
import framework.utils.SecurityUtil;

import play.db.DB;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import play.db.jpa.GenericModel.JPAQuery;
/**
 * 基础Model类
 * @author jinchaoyang
 *
 */
public abstract class  BaseModel extends Model {
	
	private final  static Logger logger = LogUtil.getInstance(BaseModel.class);
	
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
	   * 根据查询条件返回相应的查询结果 Sequence用于指定查询的Model类
	   * @param params
	   * @param pageNo
	   * @param pageSize
	   * @param tableName 表名
	   * @return
	   */
	  public static PagerRS<?> findByPager(BaseQuery params,int pageNo, int pageSize,Session session){
	  	  List<Object> countObj = params.toCountQuery();
	  	  String querySql = params.toQuery();
	  	  List<Object> args = (List<Object>)countObj.get(1);
	  	  List<?> result = new ArrayList();
	  	  Pager pager = null;
	  	  try{
	  	  session.beginTransaction();
	      Query q = session.createQuery(querySql);
	  	  for(int i=0;i<args.size();i++){
	  		  q.setParameter(i, args.get(i));
	  	  }
	  	  long totalCount = q.list().size();
	  	  q.setFirstResult((pageNo-1)*pageSize);
	  	  q.setMaxResults(pageSize);
	  	  result = q.list();
	  	  pager = new Pager(totalCount, pageNo, pageSize,params);
	  	  }catch(Exception e){
	  		  pager = new Pager(0,1,pageSize,params);
	  		  logger.info( "话单分表执行分页查询时出错",e);
	  	  }finally{
	  		  session.getTransaction().commit();
	  		  session.close();
	  	  }
		  return new PagerRS(result, pager);
  }
	  
	  
	  /**
	   * 获取所有的查询结果集
	   * @param params
	   * @return
	   */
	  public static List<?> findByQuery(BaseQuery params,Session session){
		  List<Object> countObj = params.toCountQuery();
		  String querySql = params.toQuery();
		  List<?> result = new ArrayList();
		  List<Object> args = (List<Object>)countObj.get(1);
		  try{
			  session.beginTransaction();
			  Query q = session.createQuery(querySql);
			  for(int i=0;i<args.size();i++){
				  q.setParameter(i, args.get(i));
			  }
			  result = q.list();
		  }catch(Exception e){
			 if(null==result){
				 result = new ArrayList();
			 }
	  			logger.info("话单分表执行查询时出错",e);
		  }finally{
			  session.getTransaction().commit();
			  session.close();
		  }
		  return result;
		 
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
	  /**
	   * 获取当前查询的JPAQuery对象
	   * @param params
	   * @return
	   */
	  public static JPAQuery getJPAQuery(BaseQuery params){
		  List<Object> countObj = params.toCountQuery();
		  String querySql = params.toQuery();
		  List<Object> args=(List<Object>)countObj.get(1);
		  JPAQuery query = Sequence.find(querySql, args.toArray());
		  return query;
	  }
	  
	  
	  
}

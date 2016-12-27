package framework.utils;

import java.util.List;

import framework.base.PagerRS;

import play.db.jpa.GenericModel.JPAQuery;

public class JPAUtil {
	
	 /* public PagerRS<?> getResultsByPager(JPAQuery query, int pageNo, int pageSize){
		  List<?> result =  query.fetch(pageNo, pageSize);
		  long totalCount = getResultsCount(query);		  
		  Pager pager = new Pager(totalCount, pageNo, pageSize);
		  return new PagerRS(result, pager);
	  }
	  
	  public long getResultsCount(JPAQuery query){
		  List<?> result =  query.s;
		  Pager pager = new Pager(pageNo, pageSize);
		  return new PagerRS(result, pager);
	  }	  */
}

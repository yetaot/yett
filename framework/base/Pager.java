package framework.base;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;
import framework.utils.ArrayUtil;
import framework.utils.StringUtil;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.Http;

public class Pager implements Serializable {
	
	private Logger logger = LogUtil.getInstance(Pager.class);
	   //每页显示的条目
	   public int pageSize;
	   //当前页
	   public int pageNo;
	   //上一页
	   public int prePage;
	   //下一页
	   public int nextPage;
	   //起始页
	   public int firstPage;
	   //末页
	   public int lastPage;
	   
	   public int totalPage;
	   
	   public long totalRow;
	   
	   private BaseQuery query;

	   private String path;
	   
	   public Pager(){
	   
	   }
	   
	   public Pager(long totalRow,int pageNo,int pageSize,BaseQuery query){
		   this.totalRow = totalRow;
		   this.pageNo = pageNo;
		   this.firstPage=1;
		   
		   this.totalPage = (int) ((totalRow+pageSize-1)/pageSize);
		   this.lastPage = totalPage;
		   this.prePage = pageNo-1<1?1:(pageNo-1);
		   this.nextPage = pageNo+1>=totalPage?totalPage:(pageNo+1);
		   this.pageSize = pageSize;
		   this.query=query;
	   }
	   

	 /**
	  * 获取每页相应的URL地址
	  * @param pageNum
	  * @return
	  */
	 public String getUrl(int pageNum){
		  return this.path+query.toString()+StringUtil.decode(query.toString().endsWith("?"), "", "&")+"page="+pageNum+"&pageSize="+pageSize;
	  }
	
	 public Map<String,String> allParams(){
		 String queryString = query.toString().replace("?","");
		 Map<String,String> params = new HashMap<String,String>();
		 String[] parameters = queryString.split("&");
		 if(!ArrayUtil.isEmpty(parameters)){
			 for(String element : parameters){
				 if(element.equals("pageSize")||StringUtil.isBlank(element)){
					 continue;
				 }else{
					 String[] vals = element.split("=");
					 String value="";
					 if(vals.length>1){
						 value = vals[1];
					 }
				 params.put(vals[0], value);
				 }
			 }
		 }
		 return params;
	 }
	 
	 
	 public Pager setPath(String path){
		this.path = path;
		return this;
	 }
	 
	  
	  
}

package controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import models.task.Task;
import models.task.vo.TaskQuery;
import framework.base.BaseController;
import framework.base.PagerRS;
import framework.exceptions.ExceptionHandler;
import play.mvc.Controller;

/**
 * 下载任务
 * @author liuwei
 */
public class Tasks extends BaseController{
	
	/**
	 * 全部下载列表
	 * @param page
	 * @param query
	 */
	public static void all(Integer page,TaskQuery query){
		PagerRS<Task> rs = (PagerRS<Task>) Task.findByPager(getQuery(query,TaskQuery.class),getPageNo(page),getPageSize());
		render(rs);
	}
	
	/**
	 * 个人下载列表
	 * @param page
	 * @param query
	 */
	public static void one(Integer page,TaskQuery query){
		if (null == query) {
			query = new TaskQuery();
		}
		query.creatorId = getLoginUser().get("id");
		PagerRS<Task> rs = (PagerRS<Task>) Task.findByPager(getQuery(query,TaskQuery.class),getPageNo(page),getPageSize());
		render(rs);
	}
	
	/**
	 * 文件下载
	 * @param id
	 */
	public static void download(long id){
		Task task = Task.findById(id);
		if(null!=task){
			task.incr();
			String filePath = task.filePath;
			File f = new File(filePath);
			try {
				response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(f.getName(),"UTF-8").toLowerCase());
			} catch (UnsupportedEncodingException e) {
				ExceptionHandler.throwRuntimeException(e, "下载文件名编码格式错误", logger);
			}
			renderBinary(f);
		}else{
			notFound("文件已删除或不存在！");
		}
	}
	
	public static void destroy(long id){
		Task task = Task.findById(id);
		task.destroy();
		saveUserLogAndNotice("任务删除成功", true);
		all(null,null);
		
	}
	
	public static void del(long id){
		Task task = Task.findById(id);
		task.destroy();
		one(null,null);
	}
}

package controllers.resource;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

import models.resource.JobManager;
import models.resource.vo.JobManageQuery;
import models.utils.SecurityConstant;
import play.data.binding.ParamNode;
import play.data.validation.Valid;
import play.db.helper.SqlQuery.Concat;
import play.modules.redis.Redis;
import framework.base.BaseController;
import framework.base.PagerRS;
import framework.utils.DicUtil;
import framework.utils.StringUtil;

/**
 * @author lenzhao 
 * @email zhaosl1017@gmail.com
 * @date  2014-11-13 下午5:20:15
 */
public class JobManages extends BaseController {
	public static void index(Integer page,JobManageQuery query) {
		PagerRS<JobManager> rs=(PagerRS<JobManager>) JobManager.findByPager(getQuery(query, JobManageQuery.class), getPageNo(page), getPageSize());
		render(rs);
	}
	
	public static void add() {
		render();
	}
	
	public static void create(@Valid JobManager job) {
		String loginerId = StringUtil.trim(getLoginUser().get("id"),"0");
		if(!validation.hasErrors()){
			job.jobName = StringUtil.trim(job.jobName);
			job.createdAt = new Date();
			job.updatedAt= new Date();
			job.creator = Long.valueOf(loginerId);
			job.save();
			saveUserLogAndNotice("任务【"+job.jobName+"】添加成功", true);
			job.reloadRedis();
			index(null, null);
		} else {
			render("@add","");
		}
	}
	public static void edit(long id) {
		JobManager job = JobManager.findById(id);
		render(job);
	}
	
	public static void update(long id) {
		JobManager job = JobManager.findById(id);
		String jobName = job.jobName;
		String roleType = job.roleType;
		job.edit(ParamNode.convert(params.all()), "job");
		validation.valid(job);
		if(!validation.hasErrors()){
			job.jobName = StringUtil.trim(job.jobName);
			job.updatedAt = new Date();
			job.save();
			saveUserLogAndNotice("任务【"+job.jobName+"】修改成功", true);
			Redis.hdel(SecurityConstant.PLAYFORM_JOB_MANAGE, roleType+"_"+jobName);
			job.reloadRedis();
			index(null, null);
		} else {
			render("@edit",job);
		}
	}
	public static void del(String ids){
		String[]  strs = ids.split(",");
		try{
			for(String str : strs){
				JobManager job = JobManager.findById(Long.valueOf(str));
				job.delRedis();
				job.delete();
			}
			renderJSON(true);
		}catch(Exception e){
			logger.error(String.format("[%s], Message<%s>", "del","批量删除任务出现异常"));
			renderJSON(false);
		}
	}
	
	public static void show(long id) {
		JobManager job = JobManager.findById(id);
		render(job);
	}
	public static void start(long id){
		JobManager job = JobManager.findById(id);
		job.updatedAt = new Date();
		job.ifUse = 1;
		job.save();
		job.reloadRedis();
		saveUserLogAndNotice("任务【"+job.jobName+"】启用成功", true);
		index(null, null);
	}
	public static void stop(long id){
		JobManager job = JobManager.findById(id);
		job.updatedAt = new Date();
		job.ifUse = 0;
		job.save();
		job.reloadRedis();
		saveUserLogAndNotice("任务【"+job.jobName+"】禁用成功", true);
		index(null, null);
	}
}

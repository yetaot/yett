package models.resource;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.utils.SecurityConstant;
import play.data.validation.Required;
import play.modules.redis.Redis;
import framework.base.BaseModel;

/**
 * 任务管理
 * @author lenzhao 
 * @email zhaosl1017@gmail.com
 * @date  2014-11-13 下午5:00:32
 */
@Entity
@Table(name="T_JOB_MANAGER")
public class JobManager extends BaseModel {
	//名称
	@Required(message="任务名称不能为空")
	public String jobName;
	//状态  启用：1  禁用：0
	public int ifUse;
	//创建时间
	public Date createdAt;
	//更新时间
	public Date updatedAt;
	//创建人
	public long creator;
	//用途
	@Column(name="C_DESC")
	public String desc;
	//角色类型
	@Required(message="角色类型不能为空")
	public String roleType;
	public JobManager(){
		this.createdAt = new Date();
	}
	public static void stopJob(JobManager job){
		job.ifUse = 0;
		job.updatedAt = new Date();
		job.save();
		job.reloadRedis();
	}
	public static JobManager findByTypeAndName(String prefix,String jobName){
		return JobManager.find("roleType=? and jobName=?", prefix,jobName).first();
	}
	/**
	 * 重新加载redis
	 */
	public void reloadRedis(){
		Redis.hset(SecurityConstant.PLAYFORM_JOB_MANAGE, this.roleType+"_"+this.jobName, String.valueOf(this.ifUse));
	}
	/**
	 * 删除指定key的field字典
	 */
	public void delRedis(){
		Redis.hdel(SecurityConstant.PLAYFORM_JOB_MANAGE, this.roleType+"_"+this.jobName);
	}
	/**
	 *  更新JOB
	 * @param ifUse
	 */
	public void updateJob(String ifUse){
		this.ifUse  = Integer.valueOf(ifUse);
		this.updatedAt = new Date();
		this.save();
	}
	public static List<JobManager> queryJobs(){
		return JobManager.findAll();
	}
}

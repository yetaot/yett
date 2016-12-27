package models.jobs;

import java.util.List;
import java.util.UUID;

import models.resource.JobManager;
import models.security.Permission;
import models.security.Resource;
import models.security.Role;
import models.security.User;
import models.utils.ResourceUtil;
import models.utils.SecurityConstant;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.modules.redis.Redis;
import framework.security.store.SecurityStoreFactory;
import framework.utils.MD5Util;
import framework.utils.SecurityUtil;

@OnApplicationStart
public class SystemInit extends Job {

	@Override
	public void doJob() throws Exception {
		doProcess();
		reloadJobRedis();
	}
	
	private static void doProcess(){
		ResourceUtil.initResources();
		SecurityStoreFactory.getInstance().getStore(SecurityUtil.RESOURCE_STORE).storeResource(Resource.querySecurityMenus());
		if(User.count()==0){
			initUser();
		}
	}
	
	private static void initUser(){
		User admin = new User();
		String uuid = UUID.randomUUID().toString();
		admin.secretKey = uuid;
		admin.userName = "admin";
		admin.name = "管理员";
		admin.password = MD5Util.encodeToStr("admin"+admin.secretKey);
		admin.save();
		List<Permission> permissions = Permission.queryByRType(SecurityConstant.RESOURCE_TYPE_PLATFORM_NAME);
		Role role = new Role();
		role.name="超级管理员";
		role.permissions.addAll(permissions);
		role.ifUse=1;
		role.type="platform";
		role.ifSystem=1;
		role.save();
		admin.roles.add(role);
		admin.save();
	}
	/**
	 * 每次启动项目检测Redis中是否存在JOB控制记录，若不存在则添加
	 */
	public static void reloadJobRedis(){
		List<JobManager> jobs = JobManager.queryJobs();
		for(JobManager job : jobs){
			boolean ifExists = Redis.hexists(SecurityConstant.PLAYFORM_JOB_MANAGE, job.roleType+"_"+job.jobName);
			if(!ifExists){
				job.reloadRedis();
			}
		}
	}
}

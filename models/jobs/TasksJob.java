package models.jobs;

import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import framework.base.Constant;
import framework.logs.LogUtil;
import framework.utils.DateUtil;
import framework.utils.DicUtil;
import framework.utils.StringUtil;
import models.resource.JobManager;
import models.task.Task;
import models.utils.SecurityConstant;
import play.db.jpa.JPAPlugin;
import play.db.jpa.NoTransaction;
import play.jobs.Job;
import play.jobs.On;
import play.modules.redis.Redis;
//@On("* * 0-23 * * ?")
public class TasksJob extends Job {
	private static final Logger logger = LogUtil.getInstance(TasksJob.class);
	@NoTransaction
	public void doJob() throws Exception{
		JobManager job = JobManager.findByTypeAndName(SecurityConstant.DIC_PLATFORM_PREFIX, TasksJob.class.getSimpleName());
		//logger.info(String.format("[%s]: ifRun<%s>","TasksJob",SecurityConstant.ifRunJob(TasksJob.class)));
		if(SecurityConstant.ifRunJob(TasksJob.class)){
			String dicName = DicUtil.lpop(Constant.DIC_TASK_QUEUE);
			if (!StringUtil.isBlank(dicName)) {
				JPAPlugin.startTx(false);
				try {
					doProcess(dicName);
				} catch (Exception e) {
					String msg = String.format("[%s]:dicName<%s>:::push task into queue failed", "doJob", dicName);
					JobManager.stopJob(job);
					logger.error(msg);
				} finally {
					JPAPlugin.closeTx(false);
				}
			}else{
				Thread.currentThread().sleep(1000);
			}
		}
	}
	
	public static void doProcess(String dicName){
		JSONObject obj = JSONObject.fromObject(dicName);
		String url = StringUtil.trim(obj.getString("url"));
		String creatorId = StringUtil.trim(obj.getString("creatorId"));
		String _createdAt = StringUtil.trim(obj.getString("createdAt"));
		Date createdAt = DateUtil.getDate(_createdAt, DateUtil.FORMAT_TYPE_ALL);
		String params = StringUtil.trim(obj.getString("params"));
		Task task = new Task(url, creatorId, createdAt);
		task.params = params;
		task.save();
		obj.put("id", task.id);
		DicUtil.rpush(Constant.DIC_DOWNLOAD_QUEUE, obj.toString());
	}
}

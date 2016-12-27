package models.jobs;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import models.resource.JobManager;
import models.task.Task;
import models.utils.SecurityConstant;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import framework.base.Constant;
import framework.logs.LogUtil;
import framework.message.MessageBox;
import framework.utils.DicUtil;
import framework.utils.StringUtil;
import play.db.jpa.JPAPlugin;
import play.db.jpa.NoTransaction;
import play.jobs.Job;
import play.jobs.On;
import play.libs.F;
import play.libs.WS;
import play.modules.redis.Redis;
//@On("* * 0-23 * * ?")
public class TasksHandler extends Job {
	private static final Logger logger = LogUtil.getInstance(TasksHandler.class); 
	@NoTransaction
	public void doJob() throws Exception{
		JobManager job = JobManager.findByTypeAndName(SecurityConstant.DIC_PLATFORM_PREFIX, TasksHandler.class.getSimpleName());
		//logger.info(String.format("[%s]: ifRun<%s>","TasksHandler",SecurityConstant.ifRunJob(TasksHandler.class)));
		if(SecurityConstant.ifRunJob(TasksHandler.class)){
			String dicName = DicUtil.lpop(Constant.DIC_DOWNLOAD_QUEUE);
			if (!StringUtil.isBlank(dicName)) {
				//true:readonly false:autosubmit
				JPAPlugin.startTx(false);
				try {
					doProcess(dicName);
				} catch (Exception e) {
					String msg = String.format("[%s]:dicName<%s>:::Task Running Failed...", "doJob", dicName);
					JobManager.stopJob(job);
					logger.error(msg);
				} finally {
					//true:canceled by close  false:commited by close
					JPAPlugin.closeTx(false);
				}
			} else {
				Thread.currentThread().sleep(500);
			}
		}
	}
	
	public static void doProcess(String dicName){
		JSONObject obj = JSONObject.fromObject(dicName);
		String url = StringUtil.trim(obj.getString("url"));
		String creatorId = StringUtil.trim(obj.getString("creatorId"));
		String params = StringUtil.trim(obj.getString("params"));
		JSONObject _params = JSONObject.fromObject(params);
		_params.put("creatorId", creatorId);
		long taskId = StringUtil.strToLong(obj.getString("id"));
		Task task = Task.findById(taskId);
		if (null != task) {
			task.run();
		}
		if (StringUtil.isBlank(url)) {
			if (null != task) {
				task.error();
			}
		} else {
			sendRequest(url, _params, task);
		}
	}
	
	public static void sendRequest(String url, JSONObject params, Task task){
		String message = String.format("[%s]:url<%s>, params<%s>", "sendRequest", url, params.toString());
		logger.info(message);
		params.put("_down_sign", true);
		String port = SecurityConstant.PROJECT_PORT;
		F.Promise<WS.HttpResponse> resp = WS.url("http://127.0.0.1:"+port+url).timeout("300s").params(params).getAsync();
		JsonElement element = null;
		String filePath = null;
		String fileName = null;
		int rcord = 0;
		String msg = null;
		try {
			element = resp.get().getJson();
			JsonObject respObj = element.getAsJsonObject();
			filePath = null != respObj.get("filePath") ? respObj.get("filePath").getAsString() : "";
			fileName = null != respObj.get("fileName") ? respObj.get("fileName").getAsString() : "";
			if (StringUtil.isBlank(filePath)) {
				rcord = 1;
				msg = "服务器端返回空下载路径";
			} else {
				if (StringUtil.isBlank(fileName)) {
					fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
				}
			}
		} catch (InterruptedException e) {
			rcord = 1;
			msg = "任务被强制打断";
			logger.error(String.format("[%s]::Task was Interrupted", "sendRequest"));
		} catch (ExecutionException e) {
			rcord = 1;
			msg = "任务执行出现异常";
			logger.error(String.format("[%s]::Execut Task Failed", "sendRequest"));
		} catch (Exception e) {
			rcord = 1;
			msg = "任务执行出现异常,服务器端错误";
			logger.error(String.format("[%s]::Servers Error", "sendRequest"));
		}
		task.complete(rcord, filePath, fileName, msg);
		/**消息**/
		Map<String, String> templates = DicUtil.getAllByDicName(Constant.DIC_TEMPLATE_CFG_NAME);
		String taskTemplate = templates.get("taskDownload");
		JSONObject taskInfo = JSONObject.fromObject(taskTemplate);
		String title = taskInfo.getString("title")+task.fileName;
		String content = taskInfo.getString("content");
		if (1 == rcord) {
			content = msg;
		} else {
			content = content.replace("{name}", task.fileName).replace("{url}", "<a href='javascript:void(0);' class='attachment_file_down' data-handle='false' url='http://127.0.0.1:8090/task/"+task.id+"/download'>点击下载</a>");
		}
		String msType = taskInfo.getString("type");
		MessageBox box = new MessageBox(title, content, msType, task.creatorId, "task.service");
		box.publish(task.creatorId);
	}
}

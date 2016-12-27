package models.jobs;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.resource.JobManager;
import models.security.User;
import models.sms.SmsBatchSendLog;
import models.sms.SmsSendHistory;
import models.utils.SecurityConstant;
import models.utils.sms.SmsConstant;
import models.utils.sms.SmsUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import play.db.jpa.JPAPlugin;
import play.jobs.Job;
import play.jobs.On;
import play.modules.redis.Redis;
import framework.base.Constant;
import framework.logs.LogUtil;
import framework.message.MessageBox;
import framework.utils.DBUtil;
import framework.utils.DateUtil;
import framework.utils.DicUtil;
import framework.utils.StringUtil;
/**
 * 消息发送任务
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-2-4 下午4:35:08
 */
//@On("* * 0-23 * * ?")
public class SendMessageJob extends Job {
	private static final Logger logger = LogUtil.getInstance(SendMessageJob.class);
	public void doJob() throws Exception{
		JobManager job = JobManager.findByTypeAndName(SecurityConstant.DIC_PLATFORM_PREFIX, SendMessageJob.class.getSimpleName());
		if(SecurityConstant.ifRunJob(SendMessageJob.class)){
			String dicName = DicUtil.lpop(Constant.DIC_MESSAGE_QUEUE_NAME);
			if (!StringUtil.isBlank(dicName)) {
				//true:readonly false:autosubmit
				JPAPlugin.startTx(false);
				try {
					doProcess(dicName);
				} catch (Exception e) {
					String msg = String.format("[%s]:dicName<%s>:::SmsSendMessageJob Running Failed...", "doJob", dicName);
					JobManager.stopJob(job);
					logger.error(msg,e);
				}finally{
					//true:canceled by close  false:commited by close
					JPAPlugin.closeTx(false);
				}
			} else {
				Thread.currentThread().sleep(500);
			}
		}
	}
	
	public static void doProcess(String dicName){
		JSONObject json = JSONObject.fromObject(dicName);
		logger.info(json);
		MessageBox box = MessageBox.convert(json);
		String source = box.source;
		if(Constant.MESSAGE_SMS_TYPE_NAME.equals(source)){
			sendSms(box);
		}
	}
	
	/**
	 * 处理发送短信
	 * @param dicName
	 */
	public static void sendSms(MessageBox box){
		String content = box.content;
		if(!StringUtil.isBlank(content)){
			String batchNo = box.title;
			String mobiles = box.acceptor;
			SmsBatchSendLog log = SmsBatchSendLog.findByBatchNo(batchNo);
			List<SmsSendHistory> historys = SmsSendHistory.queryByBatchNo(batchNo);
			if(null != log){
				log.toSaveLog(DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL), SmsUtil.convert2List(mobiles).size(), 0, 0, null, null);
			}
			for(SmsSendHistory history : historys){
				history.toSaveHistory("processing", DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL), null, null);
			}
			String userId = null;
			String pwd = null;
			userId = StringUtil.trim(SmsConstant.SMS_USER_ID, "");
			pwd = StringUtil.trim(SmsConstant.SMS_PASSWORD, "");
			JSONObject result = JSONObject.fromObject(SmsUtil.sendMessage(mobiles, content, userId, pwd));
			if(result.has("returnstatus") && result.getString("returnstatus").equals(SmsConstant.SMS_SEND_FAILD)){
				if(null != log){
					log.toSaveLog(null, null, SmsUtil.convert2List(mobiles).size(), 0, result.getString("taskID"), result.getString("message"));
				}
				for(SmsSendHistory history : historys){
					history.toSaveHistory("failure", null, result.getString("taskID"), result.getString("message"));
				}
			}else{
				if(null != log){
					log.toSaveLog(null, null, 0, SmsUtil.convert2List(mobiles).size(), result.getString("taskID"), null);
				}
				for(SmsSendHistory history : historys){
					history.toSaveHistory("success", null, result.getString("taskID"), null);
				}
			}
		}else{
			String ids = box.acceptor;
			String[] strIds = ids.split(",");
			Set<String> batchNos = new HashSet<String>();
			for(String id : strIds){
				SmsSendHistory history = SmsSendHistory.findById(Long.valueOf(id));
				if(null != history){
					history.toSaveHistory("processing", DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL), null, null);
					String userId = StringUtil.trim(SmsConstant.SMS_USER_ID);
					String pwd = StringUtil.trim(SmsConstant.SMS_PASSWORD);
					JSONObject result = JSONObject.fromObject(SmsUtil.sendMessage(history.acceptor, history.content, userId, pwd));
					if(result.has("returnstatus") && result.getString("returnstatus").equals(SmsConstant.SMS_SEND_FAILD)){
						history.toSaveHistory("failure", null, result.getString("taskID"), result.getString("message"));
					} else {
						history.toSaveHistory("success", null, result.getString("taskID"), null);
					}
					batchNos.add(history.batchNo);
				}
			}
			for(String batchNo : batchNos){
				long succ = SmsSendHistory.getSuccCount(batchNo);
				long fail = SmsSendHistory.getFailCount(batchNo);
				SmsBatchSendLog log = SmsBatchSendLog.findByBatchNo(batchNo);
				if(null != log){
					log.failCount = Integer.valueOf(fail+"");
					log.succCount = Integer.valueOf(succ+"");
					if(log.sendCount == 0){
						log.sendCount = log.failCount + log.succCount;
					}
					log.sendAt = DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL);
					log.updatedAt = new Date();
					log.save();
				}
			}
		}
	}

		

}

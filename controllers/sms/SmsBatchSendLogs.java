package controllers.sms;

import java.util.Date;
import java.util.List;

import models.security.User;
import models.sms.SmsBatchSendLog;
import models.sms.SmsSendHistory;
import models.sms.vo.SmsBatchSendLogQuery;
import models.sms.vo.SmsSendHistoryQuery;
import models.utils.SecurityConstant;
import models.utils.sms.SmsUtil;
import net.sf.json.JSONObject;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.message.MessageBox;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 短信批量发送历史
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-2-2 下午3:08:39
 */
public class SmsBatchSendLogs extends BaseController {
	/**
	 * 列表页
	 * @param page
	 * @param query
	 */
	public static void index(Integer page,SmsBatchSendLogQuery query){
		if( null == query){
			query = new SmsBatchSendLogQuery();
		}
		String loginId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
	  	PagerRS<SmsBatchSendLog> rs = (PagerRS<SmsBatchSendLog>) SmsBatchSendLog.findByPager(getQuery(query,SmsBatchSendLogQuery.class),getPageNo(page),getPageSize());
	    render(rs);
	}
	/**
	 * 查看
	 * @param id
	 */
	public static void show(long id){
		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		render(log);
	}
	/**
	 * 发送
	 * @param id
	 */
	public static void send(long id){
		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		MessageBox box = new MessageBox(log.batchNo, StringUtil.trim(log.content,""), SecurityConstant.SMS_SOURCE_TYPE_MANUAL, log.acceptor, Constant.MESSAGE_SMS_TYPE_NAME, null);
		box.publish();
		saveUserLogAndNotice("已放入短信发送队列,等待发送", true);
		index(null,null);
	}
	/**
	 * 再次发送
	 * @param id
	 */
	public static void resend(long id){
		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		MessageBox box = new MessageBox(log.batchNo, StringUtil.trim(log.content,""), SecurityConstant.SMS_SOURCE_TYPE_MANUAL, log.acceptor, Constant.MESSAGE_SMS_TYPE_NAME, null);
		box.publish();
		saveUserLogAndNotice("已放入短信发送队列,等待发送", true);
		index(null,null);
	}
	/**
	 * 删除
	 * @param id
	 */
	public static void delete(long id){
		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		if(null != log){
			log.delete();
		}
		 saveUserLogAndNotice("短信批量发送记录删除成功", true);
		 index(null,null);
	}
	/**
	 * 修改页面
	 * @param id
	 */
	public static void edit(long id){
		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		render(log);
	}
	/**
	 * 修改操作
	 * @param id
	 */
	public static void update(long id){
		String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		String content = params.get("content");
		if(StringUtil.isBlank(content)){
			validation.addError("content", "短信内容不能为空");
		}
		if(validation.hasErrors()){
			render("@edit",log);
		}else{
			log.content = content;
			log.updatedAt = new Date();
			log.operator.id = Long.valueOf(currUserId);
			log.submittedAt = new Date();
			log.auditedAt = null;
			log.auditor = null;
			log.auditReason = null;
			log.auditStatus = "pending";
			log.save();
		}
		List<SmsSendHistory> historys = SmsSendHistory.queryByBatchNo(log.batchNo);
		for(SmsSendHistory history : historys){
			history.content = log.content;
			history.operator  = log.operator;
			history.auditedAt = null;
			history.auditor = null;
			history.auditReason = null;
			history.auditStatus = "pending";
			history.save();
		}
		saveUserLogAndNotice("修改短信批次修改成功", true);
		index(null,null);
	}
	
	/**
 	 * 审核列表
 	 * @param page
 	 * @param query
 	 */
 	public static void auditQuery(Integer page, SmsBatchSendLogQuery query){
 		if(null == query){
 			query = new SmsBatchSendLogQuery();
 		}
 		query.auditStatus = "pending";
		query.flag = "audit";
 		PagerRS<SmsBatchSendLog> rs = (PagerRS<SmsBatchSendLog>) SmsBatchSendLog.findByPager(getQuery(query,SmsBatchSendLogQuery.class),getPageNo(page),getPageSize());
 		render(rs);
 	}
 	
 	/**
 	 * 审核页面
 	 * @param id
 	 */
 	public static void audit(long id){
 		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
    	render(log);
 	}
 	
 	/**
 	 * 审核操作
 	 * @param id
 	 * @param result
 	 * @param auditReason
 	 */
 	public static void auditAction(long id, String result, String auditReason){
 		SmsBatchSendLog log = SmsBatchSendLog.findById(id);
		String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		log.auditStatus = result;
		log.auditor = Long.valueOf(currUserId);
		log.auditReason = auditReason;
		log.auditedAt = new Date();
		log.save();
		List<SmsSendHistory> historys = SmsSendHistory.queryByBatchNo(log.batchNo);
		for(SmsSendHistory history : historys){
			history.auditStatus = result;
			history.auditor = Long.valueOf(currUserId);
			history.auditReason = auditReason;
			history.auditedAt = new Date();
			history.save();
		}
		saveUserLogAndNotice("短信批次审核完成", true);
		auditQuery(null,null);
 	}
	
}

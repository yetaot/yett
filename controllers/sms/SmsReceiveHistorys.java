package controllers.sms;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.sms.SmsBatchSendLog;
import models.sms.SmsReceiveHistory;
import models.sms.SmsSendHistory;
import models.sms.vo.SmsReceiveHistoryQuery;
import models.utils.SecurityConstant;
import models.utils.sms.SmsConstant;
import models.utils.sms.SmsUtil;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.message.MessageBox;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 短信接收列表
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-2-2 下午2:35:21
 */
public class SmsReceiveHistorys extends BaseController {
	/**
	 * 列表页
	 * @param page
	 * @param query
	 */
	public static void index(Integer page,SmsReceiveHistoryQuery query){
		if( null == query){
			query = new SmsReceiveHistoryQuery();
		}
	  	PagerRS<SmsReceiveHistory> rs = (PagerRS<SmsReceiveHistory>) SmsSendHistory.findByPager(getQuery(query,SmsReceiveHistoryQuery.class),getPageNo(page),getPageSize());
	    render(rs);
	}
	/**
	 * 详情页
	 * @param id
	 */
	public static void show(long id){
		SmsReceiveHistory history = SmsReceiveHistory.findById(id);
		render(history);
	}
	/**
	 * 单个删除
	 * @param id
	 */
	public static void delete(long id){
		SmsReceiveHistory history = SmsReceiveHistory.findById(id);
		if(null != history){
			history.delete();
		}
		 saveUserLogAndNotice("短信接收记录删除成功", true);
		 index(null,null);
	}
	/**
	 * 批量删除
	 * @param ids
	 */
	public static void batchDelete(String ids){
		String[] idStrs = ids.split(",");
		for(String idStr : idStrs){
			SmsReceiveHistory history = SmsReceiveHistory.findById(Long.valueOf(idStr));
			if(null != history){
				history.delete();
			}
		}
		renderJSON(true);
	}
	/**
	 * 回复
	 * @param id
	 */
	public static void reply(long id){
		SmsReceiveHistory history = SmsReceiveHistory.findById(id);
		String mobiles = history.sender;
		render(mobiles);
	}
	/**
	 * 批量回复
	 * @param ids
	 */
	public static void batchReply(String ids){
		String[] strs = ids.split(",");
		String mobiles = "";
		Set<String> mobileList = new HashSet<String>();
		for(String str : strs){
			SmsReceiveHistory history = SmsReceiveHistory.findById(Long.valueOf(str));
			mobileList.add(history.sender);
		}
		mobiles = SmsUtil.convert2Str(mobileList);
		render("@reply", mobiles);
	}
	/**
	 * 回复操作
	 * @param ifSend
	 * @param mobiles
	 * @param content
	 */
	public static void send(Integer ifSend, String mobiles, String content){
		String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		String agentId = StringUtil.trim(SecurityUtil.getUserKey(SecurityUtil.AGENT_ID), "0");
		List<String> phones = SmsUtil.convert2List(mobiles);
		String batchNo = StringUtil.batchNoGenerator(SmsConstant.DIC_SMS_BATCH_NO_GENERATOR, "0", 5);
		for(String phone : phones){
			SmsSendHistory history = new SmsSendHistory(null, phone, batchNo, null,null, content, null, SecurityConstant.SMS_SOURCE_TYPE_MANUAL, currUserId);
			if(ifSend == 0){
				history.sendResult = "notSend";
			}else{
				history.sendResult = "await";
			}
			history.create();
		}
		SmsBatchSendLog log = new SmsBatchSendLog(null, mobiles, batchNo, null, currUserId, null, content, null, null);
		log.create();
		if(ifSend == 1){
			MessageBox box = new MessageBox(batchNo, log.content, SecurityConstant.SMS_SOURCE_TYPE_MANUAL, mobiles, Constant.MESSAGE_SMS_TYPE_NAME, null);
			box.publish();
		}
		if(ifSend == 0){
			saveUserLogAndNotice("提交成功", true);
		}else{
			saveUserLogAndNotice("已放入短信发送队列,等待发送", true);
		}
		index(null,null);
	}
}

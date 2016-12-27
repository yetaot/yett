package controllers.sms;

import java.util.Date;

import org.hibernate.Hibernate;

import models.security.User;
import models.sms.SmsSendHistory;
import models.sms.vo.SmsBatchSendLogQuery;
import models.sms.vo.SmsReceiveHistoryQuery;
import models.sms.vo.SmsSendHistoryQuery;
import models.utils.SecurityConstant;
import models.utils.sms.SmsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.message.MessageBox;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 发送短信列表
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-2-2 上午10:56:47
 */
public class SmsSendHistorys extends BaseController {
	/**
	 * 列表页
	 * @param page
	 * @param query
	 */
	public static void index(Integer page,SmsSendHistoryQuery query){
		if( null == query){
			query = new SmsSendHistoryQuery();
		}
	  	PagerRS<SmsSendHistory> rs = (PagerRS<SmsSendHistory>) SmsSendHistory.findByPager(getQuery(query,SmsSendHistoryQuery.class),getPageNo(page),getPageSize());
	    render(rs);
	}
	/**
	 * 查看
	 * @param id
	 */
	public static void show(long id){
		SmsSendHistory history = SmsSendHistory.findById(id);
		render(history);
	}
	/**
	 * 重新发送
	 * @param id
	 */
	public static void resend(long id){
		SmsSendHistory history = SmsSendHistory.findById(id);
		MessageBox box = new MessageBox("", "", SecurityConstant.SMS_SOURCE_TYPE_MANUAL, StringUtil.trim(history.id), Constant.MESSAGE_SMS_TYPE_NAME, null);
		box.publish();
		history.sendResult = "await";
		history.save();
		saveUserLogAndNotice("已放入短信发送队列,等待发送", true);
		index(null,null);
	}
	/**
	 * 批量重新发送短信
	 * @param ids
	 */
	public static void batchResend(String ids){
		MessageBox box = new MessageBox("", "", SecurityConstant.SMS_SOURCE_TYPE_MANUAL, ids, Constant.MESSAGE_SMS_TYPE_NAME, null);
		box.publish();
		String[] strs = ids.split(",");
		for(String str : strs){
			SmsSendHistory history = SmsSendHistory.findById(Long.valueOf(str));
			history.sendResult = "await";
			history.save();
		}
		renderJSON(true);
	}
	/**
	 * 单个删除
	 * @param id
	 */
	public static void delete(long id){
		SmsSendHistory history = SmsSendHistory.findById(id);
		if(null != history){
			history.delete();
		}
		 saveUserLogAndNotice("短信发送记录删除成功", true);
		 index(null,null);
	}
	/**
	 * 批量删除
	 * @param ids
	 */
	public static void batchDelete(String ids){
		String[] idStrs = ids.split(",");
		for(String idStr : idStrs){
			SmsSendHistory history = SmsSendHistory.findById(Long.valueOf(idStr));
			if(null != history){
				history.delete();
			}
		}
		renderJSON(true);
	}
	
}

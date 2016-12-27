package controllers.sms;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.mvc.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import models.sms.SmsBatchSendLog;
import models.sms.SmsSendHistory;
import models.sms.SmsTemplate;
import models.utils.SecurityConstant;
import models.utils.sms.SmsConstant;
import models.utils.sms.SmsUtil;
import framework.base.BaseController;
import framework.base.Constant;
import framework.message.MessageBox;
import framework.utils.DateUtil;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 发送短信
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-2-2 下午4:33:17
 */
public class SendSms extends BaseController {
	
	public static void index(){
		List<SmsTemplate> tempList = new ArrayList<SmsTemplate>();
		tempList = SmsTemplate.findTemps();
		render(tempList);
	}
	
	/**
	 * 提交或发送短信
	 * @param ifSend
	 * @param templateId
	 * @param content
	 * @param mobileSource
	 * @param mobileNos
	 * @param dataFile
	 */
	public static void send(Integer ifSend, String templateId, String content, String mobileSource, String mobileNos, File dataFile){
		String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		if(StringUtil.isBlank(content)){
			validation.addError("content","短信内容不能为空");
		}
		if((null == dataFile && StringUtil.isBlank(mobileNos))){
			validation.addError("mobileNos","号码不能为空");
		}
		if (StringUtil.isBlank(templateId)) {
			validation.addError("templateId", "短信模板不能为空");
		}
		if  (!StringUtil.isBlank(content) && !SmsUtil.checkMessage(content)) {
			validation.addError("content", "短信内容违反相关规定");
		}
		if(validation.hasErrors()){
			List<SmsTemplate> tempList = new ArrayList<SmsTemplate>();
			tempList = SmsTemplate.findTemps();
			render("@index",  tempList);
		}else{
			List<String> phones = new ArrayList<String>();;
			String destPath = "";
			if(null != dataFile){
				String fileName = dataFile.getName();
				logger.info(String.format("[%s]:fileName<%s>", "impData", fileName));
				if(fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx")){
					if (fileName.toLowerCase().endsWith(".xls")) {
						List<String> phoneList = SmsUtil.convertXLS(dataFile);
						if (null != phoneList && phoneList.size() > 0) {
							phones.addAll(phoneList);
						}
					} else if (fileName.toLowerCase().endsWith(".xlsx")){
						List<String> phoneList = SmsUtil.convertXLSX(dataFile);
						if (null != phoneList && phoneList.size() > 0) {
							phones.addAll(phoneList);
						}
					}
				}else if(fileName.toLowerCase().endsWith(".txt")){
					List<String> phoneList = SmsUtil.readTxtFile(dataFile);
					if (null != phoneList && phoneList.size() > 0) {
						phones.addAll(phoneList);
					}
				}
				destPath = SmsUtil.copyFile(dataFile, SecurityConstant.SMS_MOBILENO_FILEPATH);
			}
			if (!StringUtil.isBlank(mobileNos)) {
				List<String> phoneList = SmsUtil.convert2List(mobileNos);
				if (null != phoneList && phoneList.size() > 0) {
					phones.addAll(phoneList);
				}
			}
			if(null != phones && !phones.isEmpty()){
				boolean isPhone = true;
				for(String phone : phones){
					if(!SmsUtil.isMobileNO(phone)){
						isPhone = false;
						break;
					}
				}
				if(!isPhone){
					validation.addError("mobileNos", "存在号码格式不正确");
					render("@index");
				}
				String batchNo = StringUtil.batchNoGenerator(SmsConstant.DIC_SMS_BATCH_NO_GENERATOR, "0", 5);
				for(String phone : phones){
					SmsSendHistory history = new SmsSendHistory(currUserId, phone, batchNo, null,null, content, templateId, SecurityConstant.SMS_SOURCE_TYPE_MANUAL, currUserId);
					if(ifSend == 0){
						history.sendResult = "notSend";
						history.auditStatus = "pending";
						history.submittedAt = new Date();
					}else{
						history.sendResult = "await";
					}
					history.create();
				}
				SmsBatchSendLog log = new SmsBatchSendLog(currUserId, SmsUtil.convert2Str(phones), batchNo, mobileSource, currUserId, null, content, templateId, null);
				if(!StringUtil.isBlank(destPath)){
					log.filePath = destPath;
				}
				log.auditStatus = "pending";
				log.submittedAt = new Date();
				log.create();
				if(ifSend == 1){
					MessageBox box = new MessageBox(batchNo, log.content, SecurityConstant.SMS_SOURCE_TYPE_MANUAL, SmsUtil.convert2Str(phones), Constant.MESSAGE_SMS_TYPE_NAME,null);
					box.publish();
				}
				if(ifSend == 0){
					saveUserLogAndNotice("提交成功", true);
				}else{
					saveUserLogAndNotice("已放入短信发送队列,等待发送", true);
				}
			}else{
				saveUserLogAndNotice("未检索到号码信息", true);
			}
		}
		index();
	}
	
	/**
	 * 获取模板内容
	 * @param tempId
	 */
	public static void getTempContent(long tempId){
		SmsTemplate temp = SmsTemplate.findById(tempId);
		String agentId = StringUtil.trim(SecurityUtil.getUserKey(SecurityUtil.AGENT_ID), "0");
		String content = "";
		if(null != temp){
			content = SmsUtil.getRenderContent(temp.content, agentId);
		}
		JSONObject json = new JSONObject();
		json.put("content", content);
		renderJSON(json);
	}
}

package models.sms;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.security.User;
import models.utils.sms.SmsConstant;
import framework.base.BaseModel;
import framework.utils.StringUtil;

@Entity
@Table(name="T_SMS_SEND_HISTORY")
public class SmsSendHistory extends BaseModel {
	/**
	 * 发送人
	 */
	public String sender;
	/**
	 * 接收人
	 */
	public String acceptor;
	/**
	 * 发送批次
	 */
	public String batchNo;
	/**
	 * 操作人
	 */
	@ManyToOne
	public User operator;
	/**
	 * 创建时间
	 */
	public Date createdAt;
	/**
	 *  发送时间
	 */
	public String sendAt;
	/**
	 * 发送结果
	 */
	public String sendResult;
	/**
	 * 发送内容
	 */
	public String content;
	/**
	 * 使用的模板
	 */
	public String templateId;
	/**
	 *             manual:手动发送
	 *发送源	project: 预览外呼
	 *				phone: 电话快捷键
	 */
	public String source;
	/**
	 * 返回数据
	 */
	public String rCode;
	/**
	 * 错误描述
	 */
	public String rDesc;
	//审核时间
	public Date auditedAt;
	//审核备注
	public String auditReason;
	//审核人
	public Long auditor;
	//提交审核时间
	public Date submittedAt;
	//审核备注
	public String auditStatus;
	//时间戳
	public Date dateTimp;
	
	public SmsSendHistory(){
		this.createdAt = new Date();
	}
	public SmsSendHistory(String sender, String acceptor,String batchNo, String sendAt, String sendResult, String content, String templateId, String source, String operatorId){
		String  signature = SmsConstant.SMS_SIGNATURE;
		this.sender = sender;
		this.acceptor = acceptor;
		this.batchNo = batchNo;
		this.sendAt = sendAt;
		this.sendResult = sendResult;
		this.content = content + "【"+signature+"】";
		this.templateId = templateId;
		this.source = source;
		this.operator = User.findById(Long.valueOf(operatorId));
		this.createdAt = new Date();
	}
	public static List<SmsSendHistory> queryByBatchNo(String batchNo){
		return SmsSendHistory.find("batchNo=?", batchNo).fetch();
	}
	public void toSaveHistory(String sendResult, String sendAt,String rCode, String rDesc){
		this.sendResult = sendResult;
		if(!StringUtil.isBlank(sendAt)){
			this.sendAt = sendAt;
		}
		this.rCode = rCode;
		this.rDesc = rDesc;
		this.save();
	}
	public static long getSuccCount(String batchNo){
		return SmsSendHistory.count("batchNo=? and sendResult=?", batchNo,"success");
	}
	public static long getFailCount(String batchNo){
		return SmsSendHistory.count("batchNo=? and sendResult=?", batchNo,"failure");
	}
}

package models.sms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.security.User;
import models.utils.sms.SmsConstant;
import framework.base.BaseModel;
import framework.utils.StringUtil;
/** 
 *短信批量发送日志
 */
@Entity
@Table(name="T_SMS_BATCH_SEND_LOG")
public class SmsBatchSendLog extends BaseModel {
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
	 * 号码来源
	 */
	public String mobileSource;
	/**
	 * 操作人
	 */
	@ManyToOne
	public User operator;
	/**
	 * 操作时间
	 */
	public Date updatedAt;
	/**
	 * 创建时间
	 */
	public Date createdAt;
	/**
	 * 发送时间
	 */
	public String sendAt;
	/**
	 * 发送内容
	 */
	public String content;
	/**
	 * 使用模板
	 */
	public String templateId;
	/**
	 * 文件路径
	 */
	public String filePath;
	/**
	 * 发送数量
	 */
	public int sendCount;
	/**
	 * 成功数量
	 */
	public int succCount;
	/**
	 * 失败数量
	 */
	public int failCount;
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
	
	/**
	 * 默认构造方法
	 */
	public SmsBatchSendLog(){
		this.createdAt = new Date();
	}
	public SmsBatchSendLog(String sender, String acceptor,String batchNo,String mobileSource,String operatorId,String sendAt,String content,String templateId
			,String filePath){
		String  signature = SmsConstant.SMS_SIGNATURE;
		this.sender = sender;
		this.acceptor = acceptor;
		this.batchNo = batchNo;
		this.mobileSource = mobileSource;
		this.operator = User.findById(Long.valueOf(operatorId));
		this.sendAt = sendAt;
		this.content = content + "【"+signature+"】";
		this.templateId = templateId;
		this.filePath = filePath;
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
	
	public static SmsBatchSendLog findByBatchNo(String batchNo){
		return SmsBatchSendLog.find("batchNo=?", batchNo).first();
	}
	
	public void toSaveLog(String sendAt, Integer sendCount, Integer failCount, Integer succCount, String rCode, String rDesc){
		if(null != sendCount){
			this.sendCount = sendCount;
		}
		if(!StringUtil.isBlank(sendAt)){
			this.sendAt = sendAt;
		}
		this.rCode = rCode;
		this.rDesc = rDesc;
		this.failCount = failCount;
		this.succCount = succCount;
		this.save();
	}
	
}

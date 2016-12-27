package models.sms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import framework.base.BaseModel;
/**
 * 短信接收历史
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-1-28 下午1:31:05
 */
@Entity
@Table(name="T_SMS_RECEIVE_HISTORY")
public class SmsReceiveHistory extends BaseModel {
	/**
	 * 发送人
	 */
	public String sender;
	/**
	 * 接收人
	 */
	public String acceptor;
	/**
	 * 短信内容
	 */
	public String content;
	/**
	 * 接收时间
	 */
	public String revicedAt;
	/**
	 * 创建时间
	 */
	public Date createdAt;
	
	public SmsReceiveHistory(){
		this.createdAt = new Date();
	}
	
	public SmsReceiveHistory(String sender, String acceptor, String content, String revicedAt){
		this.sender = sender;
		this.acceptor = acceptor;
		this.content = content;
		this.revicedAt = revicedAt;
		this.createdAt = new Date();
	}
}

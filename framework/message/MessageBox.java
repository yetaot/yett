package framework.message;

import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;

import framework.base.Constant;
import framework.utils.DateUtil;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 对外提供的消息接口
 * @author jinchaoyang
 *
 */
public class MessageBox {
	
	
	//消息标题
	public String title;
	//消息内容
	public String content;
	//消息类型
	public String messageType;
	//消息来源
	public String source;
	//发送人
	public String sender;
	//发送时间
	public String sendAt;
	//接收人
	public String acceptor;
	//接受用户组
	public String groups;
	//创建时间
	public String createdAt;
	//提醒时间
	public String remindAt;
	//发送人姓名
	public String senderName;

	
	
	public MessageBox(){
		
	}
	
	
	public MessageBox(String title,String content,String messageType,String acceptor,String source){
		this.title = title;
		this.content = content;
		this.messageType = messageType;
		this.acceptor = acceptor;	
		this.source = source;
	}
	
	public MessageBox(String title,String content,String messageType,String acceptor,String source,String remindAt){
		this(title, content, messageType, acceptor,source);
		this.remindAt = remindAt;
	}
	public MessageBox(String title,String content,String messageType,String acceptor,String source,String groups,boolean sign){
		this(title, content, messageType, acceptor,source);
		this.groups = groups;
	}
	
	public MessageBox(String title,String content,String messageType,String acceptor,String source,String groups,String remindAt,boolean sign){
		this(title, content, messageType, acceptor,source,groups,sign);
		this.remindAt = remindAt;
	}
	/**
	 * 消息发布
	 * @return
	 */
	public long publish(){
		if(StringUtil.isBlank(this.sender)){
			this.sender = SecurityUtil.getLoginUserId();
		}
		this.createdAt = DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL);
		JSONObject obj = JSONObject.fromObject(this);
		 return DicUtil.rpush(Constant.DIC_MESSAGE_QUEUE_NAME, obj.toString());
	}
	
	public long publish(String sender){
		this.sender = sender;
		this.createdAt=DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL);
		JSONObject obj = JSONObject.fromObject(this);
		return DicUtil.rpush(Constant.DIC_MESSAGE_QUEUE_NAME, obj.toString());
	}
	
	public long publishToQueue(String queueName){
		if(StringUtil.isBlank(this.sender)){
			this.sender = SecurityUtil.getLoginUserId();
		}
		this.createdAt=DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL);
		JSONObject obj = JSONObject.fromObject(this);
		return DicUtil.rpush(queueName,obj.toString());
	}
	
	public long publishToQueue(String queueName,String sender){
		this.sender = sender;
		this.createdAt=DateUtil.dateFormat(new Date(), DateUtil.FORMAT_TYPE_ALL);
		JSONObject obj = JSONObject.fromObject(this);
		return DicUtil.rpush(queueName,obj.toString());
	}
	
	/**
	 * 将JSON转换成MessagBox对象
	 * @param params
	 * @return
	 */
	public static MessageBox convert(JSONObject params){
		String title = params.getString("title");
		String content = params.getString("content");
		String messageType = params.getString("messageType");
		String source = params.getString("source");
		String sender = params.getString("sender");
		String sendAt = params.getString("sendAt");
		String acceptor = params.getString("acceptor");
		String groups = params.getString("groups");
		String createdAt = params.getString("createdAt");
		String remindAt = params.getString("remindAt");
		String senderName="";
		if(params.containsKey("senderName")){
		   senderName = params.getString("senderName");
		}
		MessageBox box = new MessageBox(title, content, messageType, acceptor, source);
		box.createdAt = createdAt;
		box.remindAt = remindAt;
		box.groups = groups;
		box.sendAt = sendAt;
		box.sender = sender;
		box.senderName = senderName;
	    return box;
	}
	
}

package models.sms;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Check;
import play.data.validation.CheckWith;
import play.data.validation.Required;
import framework.base.BaseModel;
import framework.utils.DateUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;
/**
 *短信模板模型
 */
@Entity
@Table(name="T_SMS_TEMPLATE")
public class SmsTemplate extends BaseModel {
	/**
	 * 模板名称
	 */
	@Required(message="模板名称不能为空")
	@CheckWith(value=SmsTemplateNameCheck.class,message="模板名称不能重复")
	public String name;
	/**
	 * 模板编号
	 */
	public String templateCode;
	/**
	 * 模板内容
	 */
	public String content;
	/**
	 * 创建人ID
	 */
	public long creatorId;
	/**
	 * 创建时间
	 */
	public Date createdAt;
	/**
	 * 是否使用
	 */
	public int ifUse;
	/**
	 * 最近操作人
	 */
	public long operatorId;
	/**
	 * 更新时间
	 */
	public Date updatedAt;
	/**
	 * 审核时间
	 */
	public Date auditedAt;
	/**
	 * 审核备注
	 */
	public String auditReason;
	/**
	 * 审核人
	 */
	public Long auditor;
	/**
	 * 提交审核时间
	 */
	public Date submittedAt;
	/**
	 * 状态
	 */
	public String status;
	/**
	 * 默认构造方法
	 */
	public SmsTemplate(){
		this.createdAt = new Date();
	}
	public String getCode(){
		String code = "";
		if(!StringUtil.isBlank(this.templateCode)){
			code = this.templateCode.substring(2);
		}
		return code;
	}
	public static class SmsTemplateNameCheck extends Check{
	    public boolean isSatisfied(Object obj,Object value){
	    	SmsTemplate template = null;
	    	SmsTemplate template2 = null;
			if(obj instanceof SmsTemplate){
				template = (SmsTemplate)obj;
			}
			if(!template.isPersistent()){
				template2 = SmsTemplate.find("name=? and ifUse=?", StringUtil.trim(value), 1).first();
			}else{
				template2 = SmsTemplate.find("name=? and ifUse=? and id<>?", StringUtil.trim(value),1,template.id).first();
			}
			if(template2 == null){
				return true;
			}else{
				return false;
			}
	    }
	}
	public static List<SmsTemplate> findTemps(){
		return SmsTemplate.find("ifUse=? and status=?",1, "succAudit").fetch();
	}
}

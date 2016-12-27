package models.sms;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import framework.base.BaseModel;
/**
 *短信模板配置模板
 *
 */
@Entity
@Table(name="T_SMS_TEMPLATE_CONFIG")
public class SmsTemplateConfig  extends BaseModel{
	/**
	 * 字段名称
	 */
	public String columnName;
	/**
	 * 字段类型,system:系统字段
	 */
	public String columnType;
	/**
	 * 字段对应的值
	 */
	public String columnValue;
	/**
	 * 所属模板
	 */
	public long templateId;
	
	public static List<SmsTemplateConfig> findTemplateByTempId(long templateId){
		return SmsTemplateConfig.find("templateId=?", templateId).fetch();
	}
	public SmsTemplateConfig(String columnName, String columnValue, String columnType, long templateId){
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnValue = columnValue;
		this.templateId = templateId;
	}
	public static void deleteByTemplateId(long templateId){
		 List<SmsTemplateConfig> configs = findTemplateByTempId(templateId);
		 for(SmsTemplateConfig config : configs){
			 config.delete();
		 }
	}
}

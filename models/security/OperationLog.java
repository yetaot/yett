package models.security;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import framework.base.BaseModel;

/**
 * 操作日志
 * @author lenzhao 
 * @email zhaosl1017@gmail.com
 * @date  2014-11-20 上午11:14:16
 */
@Entity
@Table(name="T_OPERATION_LOG")
public class OperationLog extends BaseModel {
	//日志明细
	public String content;
	//操作人Id
	public long creatorId;
	//操作时间
	public Date createdAt;
	//操作人所在ip
	public String ip;
	//操作对象
	public long objectId;
	//操作对象类型
	public String objectType;
	
	public OperationLog(){
		this.createdAt = new Date();
	}
}

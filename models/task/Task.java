package models.task;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import framework.base.BaseModel;

@Entity
@Table(name="T_TASK")
public class Task extends BaseModel{
	//创建人
	public String creatorId;
	//状态
	public String status;
	//下载次数
	public int count;
	//请求链接
	public String url;
	//文件路径
	public String filePath;
	//文件名称
	public String fileName;
	//更新时间
	public Date updatedAt;
	//备注
	public String note;
	//是否可用
	public int ifUse;
	//参数
	public String params;
	//创建时间
	public Date createdAt;
	
	private final static String READY_STATUS_NAME="READY";
	private final static String BEGIN_STATUS_NAME="BEGIN";
	private final static String COMPLETE_STATUS_NAME="COMPLETE";
	private final static String ERROR_STATUS_NAME="ERROR";
	public Task(String url,String creatorId, Date createdAt) {
		super();
		this.url = url;
		this.ifUse=1;
		this.creatorId = creatorId;
		this.createdAt = createdAt;
	}
	public Task() {
		super();
		this.ifUse=1;
	}
	public void error(){
		this.status=ERROR_STATUS_NAME;
		this.updatedAt = new Date();
		this.save();
	}
	
	public void run(){
		this.status = BEGIN_STATUS_NAME;
		this.updatedAt = new Date();
		this.save();
	}
	
	public void complete(int rcode,String filePath,String fileName,String note){
		if(rcode==0){
			this.status = COMPLETE_STATUS_NAME;
		}else{
			this.status = ERROR_STATUS_NAME;
		}
		this.filePath = filePath;
		this.fileName = fileName;
		this.note = note;
		this.updatedAt = new Date();
		this.save();
		
	}
	
	public void incr(){
		this.count = this.count+1;
		this.save();
	}
	
	public void destroy(){
		this.ifUse = 0;
		this.updatedAt = new Date();
		this.save();
	}
}

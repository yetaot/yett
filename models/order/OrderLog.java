package models.order;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import framework.base.BaseModel;

/**    
 * @Description:TODO(办理流程)   
 * @author: 李双喜
 * @email : 15110256812@QQ.com   
 * @date  : 2016年6月12日 上午11:42:54   
 *      
 */ 
@Entity
@Table(name="T_ORDER_LOG")
public class OrderLog  extends BaseModel {
	public String gzmc;  //工作名称
	public String bldw;  //办理单位
	public String bljg;  //办理结果
	public String blqk;  //办理情况
	public String wcsj;  //完成时间
	public String thyy;  //退回原因
	public String thyj;  //退回意见
	public String statusId; //状态
	@ManyToOne
	public ReportOrder report;
}

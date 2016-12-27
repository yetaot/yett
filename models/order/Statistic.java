package models.order;

import framework.base.BaseModel;

/**    
 * @Description:TODO(来电人)   
 * @author: 李双喜
 * @email : 15110256812@QQ.com   
 * @date  : 2016年6月12日 上午11:42:54   
 *      
 */ 

public class Statistic  extends BaseModel {
	public String twoname;	//二级单位
	public String red;	//过期量
	public String yellow;	//待处理
	public String green;	//合计量
}

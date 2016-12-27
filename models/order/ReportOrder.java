package models.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.utils.SecurityConstant;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import framework.base.BaseModel;
import framework.utils.DateUtil;
import framework.utils.DicUtil;

/**    
 * @Description:TODO(举报工单)   
 * @author: 李双喜
 * @email : 15110256812@QQ.com   
 * @date  : 2016年6月12日 上午10:40:28   
 *      
 */ 
@Entity
@Table(name="T_REPORT_ORDER")
public class ReportOrder extends BaseModel{
	public String formId;	//表单id
	public String taskId;	//任务id
	public String assignee;	//是否签收
	public String delayType;	//限办时间类型
	public String sqlb;	//诉求类别
	public String sourceid;	//投诉举报方式
	public String createtime;	//创建日期/来电时间
	public String laidianid;	//来电人ID
	public String scsj;	//上车时间
	public String xcsj;	//下车时间
	public String lc;	//里程
	public String dhsj;	//等候时间
	public String jine;	//票现金额
	public String hyid;	//行业
	public String tslbid;	//类别
	public String cph;	//车牌号
	public String zjzh;	//准驾证号
	public String jsyxm;	//驾驶员姓名
	public String czcgs;	//出租车公司
	public String jjcd;	//紧急程度14普通15加急
	public String scdq;	//上车地区
	public String scdd;	//上车地点
	public String cxdd;	//下车地区
	public String xcdd;	//下车地点
	public String fsdq;	//发生地区
	public String fsdd;	//发生地点
	public String tsbs;	//投诉标识
	public String tssj;	//投诉时间
	public String state;	//工单状态
	public String djyh;	//登记用户
	public String czcgsdh;	//出租车公司电话
	public String fphm;	//发票号码
	public String fpmm;	//发票密码
	public String tssx;	//投诉事项
	public String fpdw;	//发票单位
	public String gdbh;	//工单编号
	public String gdbz;	//质检状态
	public String sfysgzh;	//收费员上岗证号
	public String parkplace;	//停车场地址
	public String isDel;	//是否删除default0
	public String attr1;	//公交线路号
	public String attr2;	//公交车辆自编号
	public String attr3;	//公交行驶方向
	public String attr4;	//售票员编号
	public String attr5;	//驾驶员编号
	public String attr6;	//一卡通卡号
	public String toWhere;	//行驶方向去哪
	public String scz;	//上车站
	public String xcz;	//下车站
	public String clcsfs;	//车身辅色
	public String clcszs;	//车身主色
	public String clcx;	//车型
	public String cllhh;	//车辆立户号
	public String clqiye;	//车辆所属企业
	public String clquxian;	//车辆所属区县
	public String clssgsdh;	//车辆所属公司电话
	public String tsyy;	//投诉原因(对应投诉标识)
	public String lastupdatetime;	//最后修改时间
	public String modifyuser;	//修改人
	public String recordfile;	//语音文件
	public String username;	//来电人
	public String issecrecy;	//是否保密
	public String linkman;	//联系人
	public String linkphone;	//联系电话
	public String callid;	//来电号码
	public String tousuid;	//投诉主键
	public String sessionid;	//calllog中的关联字段
	public String jblbid;	//类别
	public String clph;	//车辆牌号
	public String cxsj;	//出现时间
	public String parkName;	//停车场名称
	public String chexing;	//车型
	public String clys;	//车辆颜色
	public String ddbz;	//是否有顶灯
	public String area;	//所在区县
	public String tollName;	//收费员姓名
	public String sfysgbh;	//占道收费上岗证编号
	public String bjbdx;	//被举报对象
	public String jbsx;	//举报事项
	public String wfqk;	//举报内容
	public String jylbid;	//类别
	public String userunits;	//所属单位
	public String zh;	//车辆自编号
	public String dwdh;	//电话
	public String fssj;	//发生时间
	public String bylbid;	//类别
	public String xlh;	//线路号
	public String xsfx;	//行驶方向
	public String ygbh;	//员工编号
	public String zxlbid;	//类别
	public String qylhh;	//立户号
	public String ssqy;	//所属企业/企业所属区县/单位所属区县
	public String content;	//内容
	public String bzf;	//标识符1非辖属2无效3其他
	public String qtlbid;	//类别
	public String ssdw;	//所属单位
	public String sendDate;	//派单时间
	public String limitTime;	//限办时间
	public String limitDate;	//限办日期
	public String dealMessage;	//办理意见
	@OneToMany(mappedBy="report")
	public List<OrderLog> logs;
	public String bz;   //备注
	public String twoUnit; //二级单位
	public String statusId; //状态
	
	public String twoUnitAt; //二级单位签收时间
	public String twoUnitHfAt;//二级单位回复时间
	public String twoUnitHfnr; //二级单位回复内容
	public String hfyq; //回复要求
	public String bs; //标识是否过期
	public String fzdealMessage;	//办理意见
//	public String cqyy;   //过期原因
	
	
	public void saveBs(){
		if(this.statusId.equals("5") || this.statusId.equals("10") || this.statusId.equals("8")){
			System.out.println("办结通过"+this.formId);
		}else{
			long day=0; 
			Date date = new Date();
			SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			Date beginDate; 
			Date endDate; 
			try 
			{ 
				beginDate = format.parse(DateUtil.dateFormat(date, DateUtil.FORMAT_TYPE_ALL)); 
				Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
				
				if(this.sqlb.equals("24")){//投诉
					cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
					int w = cal.get(Calendar.DAY_OF_WEEK)-1;
					if(w==1){
						cal.add(Calendar.DAY_OF_MONTH, 19);//取当前日期的前一天.
					}else if(w==2 || w==3 || w==4 || w==5 || w==6 || w==0){
						cal.add(Calendar.DAY_OF_MONTH, 21);//取当前日期的前一天.
					}
//					cal.add(Calendar.DAY_OF_MONTH, 15);//取当前日期的前一天.
					System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
					System.out.println("X="+day); 
					if(day>=5){
						this.bs="green";
					}else if(day<5&&day>0){
						this.bs="yellow";
					}else{
						this.bs="red";
//				this.cqyy="签收超期";
					}
				}else if(this.sqlb.equals("27")||this.sqlb.equals("26")){//表扬  建议
					cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
					int w = cal.get(Calendar.DAY_OF_WEEK)-1;
					if(w==1){
						cal.add(Calendar.DAY_OF_MONTH, 14);//取当前日期的前一天.
					}else if(w==2 || w==3 || w==4 || w==5 || w==6 || w==0){
						cal.add(Calendar.DAY_OF_MONTH, 16);//取当前日期的前一天.
					}
//					cal.add(Calendar.DAY_OF_MONTH, 10);//取当前日期的前一天.
					System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
					System.out.println("X="+day); 
					if(day>=3){
						this.bs="green";
					}else if(day<3&&day>0){
						this.bs="yellow";
					}else{
						this.bs="red";
//				this.cqyy="签收超期";
					}
				}else if(this.sqlb.equals("28")){// 咨询
					cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
					int w = cal.get(Calendar.DAY_OF_WEEK)-1;
					if(w==1){
						cal.add(Calendar.DAY_OF_MONTH, 9);//取当前日期的前一天.
					}else if(w==2 || w==3 || w==4 || w==5 || w==6 || w==0){
						cal.add(Calendar.DAY_OF_MONTH, 11);//取当前日期的前一天.
					}
//					cal.add(Calendar.DAY_OF_MONTH, 5);//取当前日期的前一天.
					System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
					if(this.formId.equals("20160711142600370658"))
						System.out.println("X==="+day); 
					if(day>=2){
						this.bs="green";
					}else if(day<2&&day>0){
						this.bs="yellow";
					}else{
						this.bs="red";
//				this.cqyy="签收超期";
					}
				}else{
					cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
					int w = cal.get(Calendar.DAY_OF_WEEK)-1;
					if(w==1){
						cal.add(Calendar.DAY_OF_MONTH, 19);//取当前日期的前一天.
					}else if(w==2 || w==3 || w==4 || w==5 || w==6 || w==0){
						cal.add(Calendar.DAY_OF_MONTH, 21);//取当前日期的前一天.
					}
//					cal.add(Calendar.DAY_OF_MONTH, 15);//取当前日期的前一天.
					System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
					day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
					System.out.println("X="+day); 
					if(day>=8){
						this.bs="green";
					}else if(day<8&&day>0){
						this.bs="yellow";
					}else{
						this.bs="red";
//				this.cqyy="签收超期";
					}
				}
				//System.out.println("X="+day); 
			} catch (ParseException e) 
			{ 
				// TODO 自动生成 catch 块 
				e.printStackTrace(); 
			} 
		}
		
	}
	public String getBsColor(){
		String str=null;
		long day=0; 
		Date date = new Date();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		java.util.Date beginDate; 
		java.util.Date endDate; 
		try 
		{ 
		beginDate = format.parse(DateUtil.dateFormat(date, DateUtil.FORMAT_TYPE_ALL)); 
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		
		if(this.sqlb.equals("24")){//投诉
			cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
			cal.add(Calendar.DAY_OF_MONTH, 10);//取当前日期的前一天.
			System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
			endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
			day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
			System.out.println("X="+day); 
			if(day>=3){
				this.bs="green";
				return "green";
			}else if(day<3&&day>0){
				return "yellow";
			}else{
				this.bs="red";
//				this.cqyy="签收超期";
				return "red";
			}
		}else if(this.sqlb.equals("26")){//建议
			cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
			cal.add(Calendar.DAY_OF_MONTH, 10);//取当前日期的前一天.
			System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
			endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
			day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
			System.out.println("X="+day); 
			if(day>=3){
				this.bs="green";
				return "green";
			}else if(day<3&&day>0){
				this.bs="yellow";
				return "yellow";
			}else{
				this.bs="red";
//				this.cqyy="签收超期";
				this.save();
				return "red";
			}
		}else if(this.sqlb.equals("27")||this.sqlb.equals("28")){//表扬 咨询
			cal.setTime(DateUtil.getDate(this.sendDate,  "yyyy-MM-dd"));
			cal.add(Calendar.DAY_OF_MONTH, 5);//取当前日期的前一天.
			System.out.println(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
			endDate= format.parse(DateUtil.dateFormat(cal.getTime(), "yyyy-MM-dd"));
			day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
			System.out.println("X="+day); 
			if(day>=2){
				this.bs="green";
				return "green";
			}else if(day<2&&day>0){
				this.bs="yellow";
				return "yellow";
			}else{
				this.bs="red";
//				this.cqyy="签收超期";
				this.save();
				return "red";
			}
		}
		//System.out.println("X="+day); 
		} catch (ParseException e) 
		{ 
		// TODO 自动生成 catch 块 
		e.printStackTrace(); 
		} 
		
		return str; 
	}
	
	/**
	 * 办结通过和办结成功的单子将黄色跟新为绿色
	 */
	public static void changeBsColor(){
		List<ReportOrder> reportOrders = ReportOrder.findAll();
		for(ReportOrder reportOrder : reportOrders){
			if(reportOrder.statusId.equals("8") ||reportOrder.statusId.equals("5") || reportOrder.statusId.equals("10")){
				if("yellow".equals(reportOrder.bs)){
					reportOrder.bs="green";
					reportOrder.save();
				}
			}
		}
	}
	
	public static String [] titles = {"来电时间","原工单编号","二级单位签收时间","二级单位回复时间","二级单位回复内容","办理状态","派单办理意见","责任单位","回复要求","乘客姓名","投诉内容","来电号码","联系方式","线路","投诉标识"};
	public static String [] cols ={"createtime","formId","twoUnit","tbackAt","tbackNr","blzt","dealMessage","zrdw","hfyq","username","tssx","callid","linkphone","attr1","tsbs"};
	public static HSSFWorkbook createExcel(List<Long> ids){
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<String> value = new ArrayList<String>();
		for(Long id:ids){
			ReportOrder order=ReportOrder.findById(id);
			if(order!=null){
				value.add(order.createtime);
				value.add(order.formId);
				value.add(order.twoUnitAt);
				value.add(order.twoUnitHfAt);
				value.add(order.twoUnitHfnr);
				value.add(DicUtil.getValueByKey(SecurityConstant.dic_orderId,order.statusId));
				value.add(order.dealMessage);
				value.add(order.twoUnit);
				value.add(order.hfyq);
				value.add(order.username);
				value.add(order.tssx);
				value.add(order.callid);
				value.add(order.linkphone);
				value.add(order.attr1);
				value.add(DicUtil.getValueByKey(SecurityConstant.dic_tsbs,order.tsbs));
			}
			dataList.add(value);
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(); 
		//创建标题
		HSSFRow title = sheet.createRow(0);
		for(int i=0;i<titles.length;i++){
			title.createCell(i).setCellValue(titles[i]);
		}
		return wb;
	}
}

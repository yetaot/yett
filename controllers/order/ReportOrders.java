package controllers.order;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.Play;
import play.data.binding.ParamNode;
import play.db.DB;
import models.order.OrderLog;
import models.order.ReportOrder;
import models.order.Statistic;
import models.order.vo.ReportOrderQuery;
import models.security.User;
import models.utils.ExportUtil;
import models.utils.SecurityConstant;
import models.utils.XmlUtil;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.exceptions.ExceptionHandler;
import framework.message.MessageBox;
import framework.utils.DateUtil;
import framework.utils.DicUtil;
import framework.utils.ExcelUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

public class ReportOrders extends BaseController {
	public static void index(Integer page,ReportOrderQuery query) {
		ReportOrder.changeBsColor();
		System.out.println("idnex====");
		String loginId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		User user = User.findById(Long.parseLong(loginId));
		if(null == query){
			query = new ReportOrderQuery();
		}
		if(user.name.equals(XmlUtil.admin)){
		}else {
			query.twounit=user.name;
		}
		System.out.println("user::name::"+user.name);
		PagerRS<ReportOrder> rs=(PagerRS<ReportOrder>) ReportOrder.findByPager(getQuery(query, ReportOrderQuery.class), getPageNo(page), getPageSize());
		int dqs=ReportOrder.find("assignee=? and statusId!=?", "0","11").fetch().size();
		int dcl=ReportOrder.find("statusId=?", "7").fetch().size();
		int yqsh=ReportOrder.find("statusId=?", "9").fetch().size();
		int thsh=ReportOrder.find("statusId=?", "8").fetch().size();
		int wcsh=ReportOrder.find("statusId=?", "10").fetch().size();
		if(!StringUtil.isBlank(query.twounit)){
			if(!query.twounit.equals(XmlUtil.admin)){
				 dqs=ReportOrder.find("assignee=? and twoUnit=?", "0",query.twounit).fetch().size();
				 dcl=ReportOrder.find("statusId=? and twoUnit=?", "7",query.twounit).fetch().size();
				 yqsh=ReportOrder.find("statusId=? and twoUnit=?", "9",query.twounit).fetch().size();
				 thsh=ReportOrder.find("statusId=? and twoUnit=?", "8",query.twounit).fetch().size();
				 wcsh=ReportOrder.find("statusId=? and twoUnit=?", "10",query.twounit).fetch().size();
			}
		}
		render(rs,page,query,dqs,dcl,yqsh,thsh,wcsh);
	}
	public static void edit(long id,Integer page,ReportOrderQuery query) {
		ReportOrder report = ReportOrder.findById(id);
		OrderLog log = OrderLog.find("order by id desc").first();
		String loginId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		User user = User.findById(Long.parseLong(loginId));
		render(report,log,user,page,query);
	}
	public static void statistics(String beginAt,String endAt){
		ReportOrder.changeBsColor();
		StringBuffer sb = new StringBuffer();
		sb.append("select t.two_unit twoname,SUM(CASE WHEN  t.bs='red' THEN 1 ELSE 0 END) red,SUM(CASE WHEN  t.bs='yellow' THEN 1 ELSE 0 END) yellow,SUM(CASE WHEN  t.bs='green' THEN 1 ELSE 0 END) green from t_report_order t where t.send_date>? and t.send_date<? group by t.two_unit;");
		Connection conn = DB.getConnection();
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sb.toString());
			stmt.setString(1,beginAt);
			stmt.setString(2,endAt);
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			ExceptionHandler.throwRuntimeException(e,"SQL查询出现异常", logger);
		}
		List<Statistic> result = new ArrayList<Statistic>();
		try {
			while(rs!=null && rs.next() ){
				Statistic obj=Statistic.class.newInstance();
				Field [] fields = Statistic.class.getDeclaredFields();
				for(int i=0;i<fields.length;i++){
					Field field = fields[i];
					field.setAccessible(true);
					try{
						if("String".equals(field.getType().getSimpleName())||"java.lang.String".equals(field.getType().getSimpleName())){
							field.set(obj,rs.getString(field.getName()));
						}else if("Date".equals(field.getType().getSimpleName())||"java.util.Date".equals(field.getType().getSimpleName())){
							field.set(obj,rs.getDate(field.getName()));
						}else if("int".equals(field.getType().getSimpleName())||"Integer".equals(field.getType().getSimpleName())||"java.lang.Integer".equals(field.getType().getSimpleName())){
							field.set(obj,rs.getInt(field.getName()));
						}else if("double".equals(field.getType().getSimpleName())||"Double".equals(field.getType().getSimpleName())||"java.lang.Double".equals(field.getType().getSimpleName())){
							field.set(obj,rs.getDouble(field.getName()));
						}
					}catch(SQLException e){
						if("String".equals(field.getType().getSimpleName())||"java.lang.String".equals(field.getType().getSimpleName())){
							field.set(obj,"");
						}else if("Date".equals(field.getType().getSimpleName())||"java.util.Date".equals(field.getType().getSimpleName())){
							field.set(obj,null);
						}else if("int".equals(field.getType().getSimpleName())||"Integer".equals(field.getType().getSimpleName())||"java.lang.Integer".equals(field.getType().getSimpleName())){
							field.set(obj,0);
						}else if("double".equals(field.getType().getSimpleName())||"Double".equals(field.getType().getSimpleName())||"java.lang.Double".equals(field.getType().getSimpleName())){
							field.set(obj,0.00);
						}    //对未存在的列进行空值处理
					}
					field.setAccessible(false);
				}
				result.add(obj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		render(result,beginAt,endAt);
	}
	public static void update(long id,String [] twounit,String [] bz,Integer page,ReportOrderQuery query) {
		System.gc();
		ReportOrder report = ReportOrder.findById(id);
		String typeid = params.get("typeid");
		System.out.println("typeid:::"+typeid);
		String loginId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		User user = User.findById(Long.parseLong(loginId));
		String two=report.twoUnit;
		report.edit(ParamNode.convert(params.all()), "report");
		validation.valid(report);
		String str=null;
		Date date = new Date();
		if(typeid.equals("dc")){
			export(id);
		}
		if(!validation.hasErrors()){
			if(XmlUtil.admin.equals(user.name)){
				if(typeid.equals("0")){//签收
					String temp =XmlUtil.pis_claim_pdsj(XmlUtil.xmlId, report.formId, report.taskId);
					OrderLog ol=new OrderLog();
						try {
							str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.claimPdsj),XmlUtil.message);
							if(XmlUtil.claimPdsjr.equals(str)){
								report.assignee="1";
								report.statusId="7";
								report.lastupdatetime= DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
								report.save();
								ol.gzmc="签收";
								ol.bldw=user.name;
								ol.bljg="签收成功待处理";
								ol.blqk=user.name+"签收工单";
							}else{
								ol.gzmc="签收";
								ol.bldw=user.name;
								ol.bljg="签收失败";
								ol.blqk=user.name+"签收工单";
							}
							ol.report=report;
							ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
							ol.save();
						} catch (Exception e) {
							
							e.printStackTrace();
						}
//					}
				}else if(typeid.equals("1")){//退回
					String thyj = params.get("thyj");
					if(StringUtil.isBlank(thyj)){
						saveUserLogAndNotice("退回内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======1");
					try {
						OrderLog ol=new OrderLog();
						String temp = XmlUtil.pis_back_pdsj(XmlUtil.xmlId,report.formId,report.taskId,thyj);
						str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.backPdsj),XmlUtil.message);
						System.out.println("str==="+str);
						if(XmlUtil.backPdsjr.equals(str)){
							report.statusId="8";
							report.assignee="1";
							report.save();
							ol.gzmc="退回";
							ol.bldw=user.name;
							ol.bljg="退回成功";
							ol.blqk=thyj;
							ol.thyj=thyj;
						}else{
							report.statusId="20";
							report.save();
							ol.gzmc="退回";
							ol.bldw=user.name;
							ol.bljg="退回失败";
							ol.blqk=thyj;
							ol.thyj=thyj;
						}
						ol.report=report;
						ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						ol.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(typeid.equals("2")){//转办
					if(null!=twounit)
					for(int i=0;i<twounit.length;i++){
					System.out.println("typeid=======2"+bz[i]+"||"+twounit[i]);
					report.twoUnit=twounit[i];
					System.out.println("======="+report.twoUnit);
					report.statusId="11";
					report.assignee="0";
					report.save();
					OrderLog ol=new OrderLog();
					ol.gzmc="转办";
					ol.bldw=user.name;
					ol.bljg="转办至"+twounit[i];
					ol.blqk=bz[i];
					ol.thyj=bz[i];
					ol.report=report;
					ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
					ol.save();
					}
				}else if(typeid.equals("3")){//办理
					String blyj = params.get("blyj");
					if(StringUtil.isBlank(blyj)){
						saveUserLogAndNotice("办结内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======3");
					String temp = XmlUtil.pis_finish_pdsj(XmlUtil.xmlId,report.formId,report.taskId,blyj);
					try {
						str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.finishPdsj),XmlUtil.message);
						OrderLog ol=new OrderLog();
						if(XmlUtil.finishPdsjr.equals(str)){
							report.statusId="10";
							report.save();
							ol.gzmc="填写处理结果";
							ol.bldw=user.name;
							ol.bljg="办结成功";
//							ol.bljg="办结成功待审核";
							ol.blqk=blyj;
						}else{
							ol.gzmc="填写处理结果";
							ol.bldw=user.name;
							ol.bljg="办结失败";
							ol.blqk=blyj;
						}
						ol.report=report;
						ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						ol.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}else if(typeid.equals("4")){//申请延期
					String comment = params.get("sqyq");
					if(StringUtil.isBlank(comment)){
						saveUserLogAndNotice("申请延期内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======4");
					String limitTime =  params.get("limitTime");
					String temp = XmlUtil.pis_req_limitTime(XmlUtil.xmlId,limitTime,report.limitDate,report.delayType);
					try {
						String limitDate = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.getLimitTime),"limitDate");
						temp = XmlUtil.pis_delay_pdsj(XmlUtil.xmlId,report.formId,report.taskId,comment,limitTime,limitDate);
						str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.delayPdsj),XmlUtil.message);
						OrderLog ol=new OrderLog();
						if(XmlUtil.delayPdsjr.equals(str)){
							report.limitDate=limitDate;
							report.statusId="9";
							report.save();
							ol.gzmc="申请延期";
							ol.bldw=user.name;
							ol.bljg="申请延期成功待审核";
							ol.blqk=comment;
						}else{
							ol.gzmc="申请延期";
							ol.bldw=user.name;
							ol.bljg="申请延期失败";
							ol.blqk=comment;
						}
						ol.report=report;
						ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						ol.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(typeid.equals("5")){
					String thyj = params.get("thyj");
					System.out.println("typeid=======5");
					if(StringUtil.isBlank(thyj)){
						saveUserLogAndNotice("退回内容不能为空", false);
						edit(id,null,null);
					}
					try {
						OrderLog ol=new OrderLog();
						ol.gzmc="退回";
						ol.bldw=user.name;
						ol.bljg="二级退回失败";
						ol.blqk=thyj;
						ol.thyj=thyj;
						ol.report=report;
						ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						ol.save();
						report.statusId="14";
						report.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(typeid.equals("6")){
					String blyj = params.get("blyj");
					if(StringUtil.isBlank(blyj)){
						saveUserLogAndNotice("办结内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======6");
					String temp = XmlUtil.pis_finish_pdsj(XmlUtil.xmlId,report.formId,report.taskId,blyj);
					try {
						str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.finishPdsj),XmlUtil.message);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(XmlUtil.finishPdsjr.equals(str)+"-===-==-==-==-=");
//					if(XmlUtil.finishPdsjr.equals(str)){
						report.statusId="10";
						report.save();
//					}
					OrderLog ol=new OrderLog();
					ol.gzmc="处理结果通过";
					ol.bldw=user.name;
					ol.bljg="处理结果通过";
//					ol.bljg="处理结果通过至12328待审核";
					ol.blqk=blyj;
					ol.report=report;
					ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
					ol.save();
				}else if(typeid.equals("7")){
					String blyj = params.get("blyj");
					if(StringUtil.isBlank(blyj)){
						saveUserLogAndNotice("办结内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======7");
					report.statusId="16";
					report.save();
					OrderLog ol=new OrderLog();
					ol.gzmc="处理结果失败";
					ol.bldw=user.name;
					ol.bljg="处理结果失败至二级单位处理";
					ol.blqk=blyj;
					ol.report=report;
					ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
					ol.save();
				}else if(typeid.equals("8")){
					String comment = params.get("sqyq");
					if(StringUtil.isBlank(comment)){
						saveUserLogAndNotice("办结内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======4");
					String limitTime =  params.get("limitTime");
					String temp = null;
					String limitDate =null;
					try {
						if(!limitTime.equals(report.lc)){
							temp = XmlUtil.pis_req_limitTime(XmlUtil.xmlId,limitTime,report.limitDate,report.delayType);
							limitDate = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.getLimitTime),"limitDate");
							temp = XmlUtil.pis_delay_pdsj(XmlUtil.xmlId,report.formId,report.taskId,comment,limitTime,limitDate);
							str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.delayPdsj),XmlUtil.message);	
						}else{
							limitDate=report.bz;
							temp = XmlUtil.pis_delay_pdsj(XmlUtil.xmlId,report.formId,report.taskId,comment,limitTime,limitDate);
							str = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.delayPdsj),XmlUtil.message);	
						}
						
						OrderLog ol=new OrderLog();
						if(XmlUtil.delayPdsjr.equals(str)){
							report.limitDate=limitDate;
							report.statusId="9";
							report.save();
							ol.gzmc="申请延期";
							ol.bldw=user.name;
							ol.bljg="申请延期成功待审核";
							ol.blqk=comment;
						}else{
							ol.gzmc="申请延期";
							ol.bldw=user.name;
							ol.bljg="申请延期失败";
							ol.blqk=comment;
						}
						ol.report=report;
						ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						ol.save();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(typeid.equals("9")){
					String blyj = params.get("blyj");
					if(StringUtil.isBlank(blyj)){
						saveUserLogAndNotice("申请延期内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid=======7");
					report.statusId="18";
					report.save();
					OrderLog ol=new OrderLog();
					ol.gzmc="申请延期失败";
					ol.bldw=user.name;
					ol.bljg="申请延期失败至二级单位处理";
					ol.blqk=blyj;
					ol.report=report;
					ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
					ol.save();
				}
			}else{
				if(typeid.equals("0")){//签收
					OrderLog ol=new OrderLog();
					System.out.println("typeid===2===0");
						report.statusId="12";
						report.assignee="1";
						report.lastupdatetime= DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						report.twoUnitAt= DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						report.save();
						ol.gzmc="二级单位签收";
						ol.bldw=user.name;
						ol.bljg=user.name+"签收成功待处理";
						ol.blqk=user.name+"签收工单";
					ol.report=report;
					ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
					ol.save();
				}else if(typeid.equals("1")){//退回
					String thyj = params.get("thyj");
					if(StringUtil.isBlank(thyj)){
						saveUserLogAndNotice("退回内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid===2===1");
					//if(XmlUtil.parseInt(report.statusId)){
						try {
							OrderLog ol=new OrderLog();
							ol.gzmc="二级退回成功";
							ol.bldw=user.name;
							ol.bljg=user.name+"退回工单表单至"+XmlUtil.admin;
							ol.blqk=thyj;
							ol.report=report;
							ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
							ol.save();
							report.statusId="13";
							report.assignee="0";
							report.save();
						} catch (Exception e) {
							e.printStackTrace();
						}
					//}
				}
				else if(typeid.equals("3")){//办理
					String blyj = params.get("blyj");
					if(StringUtil.isBlank(blyj)){
						saveUserLogAndNotice("办结内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid===2===3");
					//if(XmlUtil.parseInt(report.statusId)){
						OrderLog ol=new OrderLog();
						ol.gzmc="二级填写处理结果";
						ol.bldw=user.name;
						ol.bljg="二级办结成功待审核";
						ol.blqk=blyj;
						ol.report=report;
						ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						ol.save();
						report.statusId="15";
						report.twoUnitHfAt= DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
						report.twoUnitHfnr=blyj;
						report.save();
					//}
				}else if(typeid.equals("4")){//申请延期
					String comment = params.get("sqyq");
					if(StringUtil.isBlank(comment)){
						saveUserLogAndNotice("申请延期内容不能为空", false);
						edit(id,null,null);
					}
					System.out.println("typeid===2===4");
					String limitTime =  params.get("limitTime");
					String temp = XmlUtil.pis_req_limitTime(XmlUtil.xmlId,limitTime,report.limitDate,report.delayType);
					try {
						String limitDate = XmlUtil.getReturn(XmlUtil.send(temp,XmlUtil.getLimitTime),"limitDate");
						report.lc=limitTime;
						report.bz=limitDate;
						report.statusId="17";
						report.assignee="0";
						report.save();
					} catch (Exception e) {
						e.printStackTrace();
					}

					OrderLog ol=new OrderLog();
					ol.gzmc="二级申请延期";
					ol.bldw=user.name;
					ol.bljg="二级申请延期待审核";
					ol.blqk=comment;
					ol.thyj=comment;
					ol.report=report;
					ol.wcsj=DateUtil.dateFormat(date,DateUtil.FORMAT_TYPE_ALL);
					ol.save();
				}
			}
			index(page,query);
		} else {
			render("@edit",report);
		}
	}
	/**
	 * 导出单个登记单
	 */
	public static void export(long id){
		System.out.println("id===="+id);
		ReportOrder.changeBsColor();
		ReportOrder report = ReportOrder.findById(id);
		DateFormat format = new SimpleDateFormat("HHmmss");
		Workbook workbook=null;
		try {
			if(report.sqlb.equals("24")){
				workbook = ExcelUtil.createWorkbook(new FileInputStream(Play.getFile(SecurityConstant.EXPORT_TEMPLATE+"tslb.xlsx")));
			}else if(report.sqlb.equals("25")){
				workbook = ExcelUtil.createWorkbook(new FileInputStream(Play.getFile(SecurityConstant.EXPORT_TEMPLATE+"jblb.xlsx")));
			}else if(report.sqlb.equals("26")){
				workbook = ExcelUtil.createWorkbook(new FileInputStream(Play.getFile(SecurityConstant.EXPORT_TEMPLATE+"jylb.xlsx")));
			}else if(report.sqlb.equals("27")){
				workbook = ExcelUtil.createWorkbook(new FileInputStream(Play.getFile(SecurityConstant.EXPORT_TEMPLATE+"bylb.xlsx")));
			}else if(report.sqlb.equals("28")){
				workbook = ExcelUtil.createWorkbook(new FileInputStream(Play.getFile(SecurityConstant.EXPORT_TEMPLATE+"zxlb.xlsx")));
			}
			//获取工作薄添加下拉列表
			Sheet sheet=workbook.getSheetAt(0);
			sheet=ExportUtil.writeSheet(sheet,report);
			String sqlb = DicUtil.getValueByKey(SecurityConstant.dic_sqlb,report.sqlb);
			//生成excel文件流
			String filePath =  SecurityConstant.FILE_UPLOAD_PATH+"/"+sqlb+format.format(new Date())+".xlsx";
			File file= ExportUtil.createCallOrderExcelFile(workbook,filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			renderBinary(file,file.getName());
			
		} catch (Exception e) {
			System.out.println("导出工单"+report.formId+"失败");
		}
	}
	
	public static void checkAll(List<Long> ids,ReportOrderQuery query){
		ReportOrder.changeBsColor();
		if(null == query){
			query = new ReportOrderQuery();
		}
		if(null==ids||ids.isEmpty()){
			saveUserLogAndNotice("请选择要导出的工单", false);
			index(null,null);
		}
		DateFormat format = new SimpleDateFormat("HHmmss");
		System.out.println(ids.size()+"==="+ids.toString());
		String loginId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		User user = User.findById(Long.parseLong(loginId));
		query.twounit=user.name;
		List<List<String>> dataList = ExportUtil.getExcelDataList(ids);
		String fileName= SecurityConstant.FILE_UPLOAD_PATH+"/"+format.format(new Date())+"工单列表.xlsx";
		File file = ExportUtil.writeDataToFile(ReportOrder.titles, dataList,fileName);
		if (!file.exists()) {
			System.out.println(3);
			try {
				System.out.println(2);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		renderBinary(file,file.getName());;
	}

	public static void statistic(String beginAt,String endAt,String twoname,String bs){
		ReportOrderQuery query = new ReportOrderQuery();
		String loginId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		User user = User.findById(Long.parseLong(loginId));
		if(user.name.equals(XmlUtil.admin) || twoname.equals(user.name)){
			query.beginAt =beginAt;
			query.endAt = endAt;
			query.bs = bs;
			query.twounit = twoname;
			index(null, query);
		}else {
			saveUserLogAndNotice("查看权限不够", false);
			statistics(beginAt,endAt);
		}
	}
	
}

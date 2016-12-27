package models.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.order.OrderLog;
import models.order.ReportOrder;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import play.Play;
import framework.utils.DicUtil;
import framework.utils.ExcelUtil;
import framework.utils.StringUtil;

public class ExportUtil {
	public static List<List<String>>  getExcelDataList(List<Long> ids){
		 List<List<String>> result = new ArrayList<List<String>>();
		 for(int i=0;i<ids.size();i++){
				ReportOrder order=ReportOrder.findById(ids.get(i));
				 List<String> line = new ArrayList<String>();
				line.add(StringUtil.trim(order.createtime));
				line.add(StringUtil.trim(order.formId));
				line.add(StringUtil.trim(order.twoUnitAt));
				line.add(StringUtil.trim(order.twoUnitHfAt));
				line.add(StringUtil.trim(order.twoUnitHfnr));
				line.add(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_orderId,order.statusId)));
				line.add(StringUtil.trim(order.dealMessage));
				line.add(StringUtil.trim(order.twoUnit));
				line.add(StringUtil.trim(order.hfyq));
				line.add(StringUtil.trim(order.username));
				line.add(StringUtil.trim(order.tssx));
				line.add(StringUtil.trim(order.callid));
				line.add(StringUtil.trim(order.linkphone));
				line.add(StringUtil.trim(order.attr1));
				line.add(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_tsbs,order.tsbs)));
				result.add(line);
		 }
		return result;
	}
	
	public static Sheet writeSheet(Sheet sheet,ReportOrder order) {
		if(order.sqlb.equals("24")){//投诉
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_tsbs,order.tsbs),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_sqlb,order.sqlb),""));
			sheet.getRow(8).getCell(8).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_tslbid,order.tslbid),""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_jjcd,order.jjcd),""));
			sheet.getRow(10).getCell(5).setCellValue(StringUtil.trim(order.attr1,""));
			sheet.getRow(10).getCell(8).setCellValue(StringUtil.trim(order.attr2,""));
			sheet.getRow(12).getCell(2).setCellValue(StringUtil.trim(order.attr3+" 至  "+order.toWhere,""));
			sheet.getRow(12).getCell(7).setCellValue(StringUtil.trim(order.attr6,""));
			sheet.getRow(14).getCell(2).setCellValue(StringUtil.trim(order.scz,""));
			sheet.getRow(14).getCell(7).setCellValue(StringUtil.trim(order.xcz,""));
			sheet.getRow(16).getCell(2).setCellValue(StringUtil.trim(order.tssj,""));
			sheet.getRow(16).getCell(5).setCellValue(StringUtil.trim(order.sendDate,""));
			sheet.getRow(16).getCell(8).setCellValue(StringUtil.trim(order.limitDate,""));
			sheet.getRow(18).getCell(2).setCellValue(StringUtil.trim(order.tssx,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(22+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(22+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(22+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(22+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(22+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(22+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("25")){//举报
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_sqlb,order.sqlb),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_tslbid,order.tslbid),""));
			sheet.getRow(8).getCell(8).setCellValue(StringUtil.trim(order.attr3+" 至  "+order.toWhere,""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_jjcd,order.jjcd),""));
			sheet.getRow(10).getCell(5).setCellValue(StringUtil.trim(order.attr1,""));
			sheet.getRow(10).getCell(8).setCellValue(StringUtil.trim(order.attr2,""));
			sheet.getRow(16).getCell(2).setCellValue(StringUtil.trim(order.tssj,""));
			sheet.getRow(16).getCell(5).setCellValue(StringUtil.trim(order.sendDate,""));
			sheet.getRow(16).getCell(8).setCellValue(StringUtil.trim(order.limitDate,""));
			sheet.getRow(18).getCell(2).setCellValue(StringUtil.trim(order.wfqk,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(19+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(19+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(19+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(19+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(19+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(19+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("26")){//建议
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_jylbid,order.jylbid),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(order.ssqy,""));
			sheet.getRow(8).getCell(8).setCellValue(StringUtil.trim(order.sendDate,""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_jjcd,order.jjcd),""));
			sheet.getRow(10).getCell(5).setCellValue(StringUtil.trim(order.attr1,""));
			sheet.getRow(10).getCell(8).setCellValue(StringUtil.trim(order.limitDate,""));
			sheet.getRow(12).getCell(2).setCellValue(StringUtil.trim(order.content,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(16+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(16+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(16+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(16+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(16+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(16+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("27")){//表扬
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_hyid,order.hyid),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_bylbid,order.bylbid),""));
			sheet.getRow(8).getCell(8).setCellValue(StringUtil.trim(order.ygbh,""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(order.attr1,""));
			sheet.getRow(10).getCell(5).setCellValue(StringUtil.trim(order.attr2,""));
			sheet.getRow(10).getCell(8).setCellValue(StringUtil.trim(order.attr3+" 至  "+order.toWhere,""));
			sheet.getRow(12).getCell(2).setCellValue(StringUtil.trim(order.tssj,""));
			sheet.getRow(12).getCell(5).setCellValue(StringUtil.trim(order.fsdd,""));
			sheet.getRow(14).getCell(2).setCellValue(StringUtil.trim(order.content,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(18+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(18+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(18+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(18+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(18+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(18+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("28")){//咨询
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_sqlb,order.sqlb),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_zxlbid,order.zxlbid),""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(order.content,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(14+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(14+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(14+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(14+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(14+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(14+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("29")){//非辖属
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_sqlb,order.sqlb),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_zxlbid,order.zxlbid),""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(order.content,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(14+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(14+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(14+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(14+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(14+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(14+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("30")){//无效
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_sqlb,order.sqlb),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_zxlbid,order.zxlbid),""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(order.content,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(14+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(14+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(14+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(14+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(14+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(14+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		if(order.sqlb.equals("31")){//其他
			sheet.getRow(2).getCell(2).setCellValue(StringUtil.trim(order.callid,""));
			sheet.getRow(2).getCell(5).setCellValue(StringUtil.trim(order.username,""));
			sheet.getRow(2).getCell(8).setCellValue(StringUtil.trim(order.createtime,""));
			sheet.getRow(4).getCell(2).setCellValue(StringUtil.trim(order.linkphone,""));
			sheet.getRow(4).getCell(5).setCellValue(StringUtil.trim(order.linkman,""));
			sheet.getRow(4).getCell(8).setCellValue(StringUtil.trim(order.issecrecy.equals("1")?"保密":"不保密",""));
			//
			sheet.getRow(8).getCell(2).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_sqlb,order.sqlb),""));
			sheet.getRow(8).getCell(5).setCellValue(StringUtil.trim(DicUtil.getValueByKey(SecurityConstant.dic_zxlbid,order.zxlbid),""));
			sheet.getRow(10).getCell(2).setCellValue(StringUtil.trim(order.content,""));
			for(int i=0;i<order.logs.size();i++){
				OrderLog log=order.logs.get(i);
				sheet.getRow(14+i).getCell(0).setCellValue(StringUtil.trim(i+1+"",""));
				sheet.getRow(14+i).getCell(1).setCellValue(StringUtil.trim(log.gzmc,""));
				sheet.getRow(14+i).getCell(2).setCellValue(StringUtil.trim(log.bldw,""));
				sheet.getRow(14+i).getCell(4).setCellValue(StringUtil.trim(log.bljg,""));
				sheet.getRow(14+i).getCell(6).setCellValue(StringUtil.trim(log.blqk,""));
				sheet.getRow(14+i).getCell(9).setCellValue(StringUtil.trim(log.wcsj,""));
			}
		}
		return sheet;
	}
	/**
	 * 工单
	 * @param workbook
	 * @return File
	 */
	public static File createCallOrderExcelFile(Workbook workbook,String filePath){
		File file = Play.getFile(filePath);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			workbook.write(fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public static File writeDataToFile(String[] titless,
			List<List<String>> dataList, String fileName) {
		 String [] title = {"来电时间","原工单编号","二级单位签收时间","二级单位回复时间","二级单位回复内容","办理状态","派单办理意见","责任单位","回复要求","乘客姓名","投诉内容","来电号码","联系方式","线路","投诉标识"};
		
		File dataFile = Play.getFile(fileName);
		if(dataFile.exists()){
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		org.apache.poi.ss.usermodel.Workbook wb=null;
		if(fileName.toLowerCase().endsWith(".xls")){
			wb=ExcelUtil.createExcel(title, dataList);
		}else{
			wb = ExportUtil.createXLSX(title, dataList);
		}
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(dataFile);
			wb.write(fos);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null!=fos){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataFile;
	}
	private static Workbook createXLSX(String[] titles,
			List<List<String>> dataList) {
		// TODO Auto-generated method stub
		Workbook wb = new SXSSFWorkbook(100);
		Sheet sheet = wb.createSheet();
		//设置样式
		sheet.setAutobreaks(true);
		sheet.setColumnWidth(0, 60 * 100);
		sheet.setColumnWidth(1, 60 * 100);
		sheet.setColumnWidth(2, 60 * 100);
		sheet.setColumnWidth(3, 60 * 100);
		sheet.setColumnWidth(4, 60 * 100);
		sheet.setColumnWidth(5, 40 * 100);
		sheet.setColumnWidth(6, 40 * 100);
		sheet.setColumnWidth(7, 35 * 100);
		sheet.setColumnWidth(8, 40 * 100);
		sheet.setColumnWidth(9, 35 * 100);
		sheet.setColumnWidth(10, 160 * 100);
		sheet.setColumnWidth(11, 40 * 100);
		sheet.setColumnWidth(12, 40 * 100);
		sheet.setColumnWidth(13, 40 * 100);
		sheet.setColumnWidth(14, 40 * 100);
		Row title = sheet.createRow(0);
		for(int i=0;i<titles.length;i++){
			title.createCell(i).setCellValue(titles[i]);	
		}
		Row row=null;
		List<String> line=null;
		for(int i=0;i<dataList.size();i++){
			line = dataList.get(i);
			row = sheet.createRow(i+1);
			for(int j=0;j<line.size();j++){
				row.createCell(j).setCellValue(line.get(j));
			}
		}
		return wb;
	}
}

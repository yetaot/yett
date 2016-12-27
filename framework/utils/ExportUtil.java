package framework.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import play.Play;
import play.db.jpa.GenericModel.JPAQuery;
import play.libs.Files;

import framework.logs.LogUtil;

public class ExportUtil {
	
	private final static Logger logger = LogUtil.getInstance(ExportUtil.class);
	/**
	 * 创建XLXS文件
	 * @param titles
	 * @param dataList
	 * @return
	 */
	public static Workbook createXLSX(String[] titles,List<List<String>> dataList){
		Workbook wb = new SXSSFWorkbook(100);
		Sheet sheet = wb.createSheet();
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
	
	
	public static Workbook appendXLSX(Workbook wb,List<List<String>> dataList){
		Sheet sheet  = wb.getSheetAt(0);
		int rowsLen = sheet.getLastRowNum()+1;
		Row row = null;
		for(List<String> line : dataList){
			row = sheet.createRow(rowsLen);
			for(int j=0;j<line.size();j++){
				row.createCell(j).setCellValue(line.get(j));
			}
			rowsLen = rowsLen+1;
		}
		return wb;
	}
	
	
	public static File writeDataToFile(Workbook wb,String fileName){
		String basePath = StringUtil.trim(ContextUtil.getProperty("download.filePath"));
		File dir = new File(basePath);
		if(!dir.exists()){
				dir.mkdirs();
		}
		String sign = DateUtil.dateFormat(new Date(), DateUtil.FULL_PATTERN);
		if(!fileName.toLowerCase().endsWith(".xls")&&!fileName.toLowerCase().endsWith(".xlsx")){
			fileName = fileName+".xls";
		}
		basePath = basePath.endsWith("/")?basePath:basePath+"/";
		try {
			fileName = basePath+sign+"_"+new String(fileName.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		File dataFile = new File(fileName);
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(dataFile);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			logger.info(String.format("文件【%s】不存在或已经被删除", fileName),e);
		} catch (IOException e) {
			logger.info("下载文件写入流关闭时出错", e);
		}finally{
			try {
				if(null!=fos){
					fos.close();
				}
			} catch (IOException e) {
				logger.info("下载文件写入流关闭时出错", e);
			}
		}
		return dataFile;

	}
	
	public static File writeDataToFile(String[] title,List<List<String>> dataList,String fileName){
		String basePath = StringUtil.trim(ContextUtil.getProperty("download.filePath"));
		File dir = new File(basePath);
		if(!dir.exists()){
				dir.mkdirs();
		}
		String sign = DateUtil.dateFormat(new Date(), DateUtil.FULL_PATTERN);
		if(!fileName.toLowerCase().endsWith(".xls")&&!fileName.toLowerCase().endsWith(".xlsx")){
			fileName = fileName+".xls";
		}
		basePath = basePath.endsWith("/")?basePath:basePath+"/";
		fileName = basePath+sign+"_"+fileName;
		File dataFile = new File(fileName);
		Workbook wb=null;
		if(fileName.toLowerCase().endsWith(".xls")){
			wb=ExcelUtil.createExcel(title, dataList);
		}else{
			wb = ExportUtil.createXLSX(title, dataList);
		}
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(dataFile);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			logger.info(String.format("文件【%s】不存在或已经被删除", fileName),e);
		} catch (IOException e) {
			logger.info("下载文件写入流关闭时出错", e);
		}finally{
			try {
				if(null!=fos){
					fos.close();
				}
			} catch (IOException e) {
				logger.info("下载文件写入流关闭时出错", e);
			}
		}
		return dataFile;
	}

	
	public static File writeToZipFile(Workbook wb,String fileName){
		File dataFile=null;
		dataFile = writeDataToFile(wb,fileName);
		String destName = dataFile.getName();
		destName = destName.toLowerCase();
		destName = destName.replace(".xlsx", "");
		destName = destName.replace(".xls","");
		destName = destName+".zip";
		String path = dataFile.getParent();
		File targetFile = new File(path+"/"+destName);
		File dir=new File(Play.tmpDir.getPath()+"/"+System.currentTimeMillis());
		if(!dir.exists()){
			dir.mkdirs();
		}
		String newFileName = dir.getAbsolutePath()+"/"+dataFile.getName();
		File newFile = new File(newFileName);
		Files.copy(dataFile, newFile);
		ExcelUtil.zip(dir, targetFile);
		Files.deleteDirectory(dir);
		Files.delete(dataFile);
		return targetFile;
		
	}
	
	public static File writeToFile(List<String> filePaths, String destPath, String fileName) {
		File destDir = new File(destPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		String sign = DateUtil.dateFormat(new Date(), DateUtil.FULL_PATTERN);
		fileName = sign + "_" + fileName;
		File targetFile = new File(destPath + fileName);
		File dir=new File(Play.tmpDir.getPath()+"/"+System.currentTimeMillis());
		if(!dir.exists()){
			dir.mkdirs();
		}
		for (String path : filePaths) {
			if (!StringUtil.isBlank(path)) {
				File dataFile = new File(path);
				if (dataFile.exists()) {
					String newFileName = dir.getAbsolutePath() + "/" + dataFile.getName();
					File newFile = new File(newFileName);
					Files.copy(dataFile, newFile);
				}
			}
		}
		ExcelUtil.zip(dir, targetFile);
		Files.deleteDirectory(dir);
		return targetFile;
	}
	

}

package framework.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import framework.base.Constant;
import framework.exceptions.ExceptionHandler;
import framework.logs.LogUtil;





import play.Play;
import play.exceptions.UnexpectedException;
import play.libs.Files;
import play.mvc.Http.Response;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
	private final static Logger logger = LogUtil.getInstance(ExcelUtil.class);
	//字符类型
	public final static String CELL_TYPE_STRING="string";
	//整型
	public final static String CELL_TYPE_NUMBERIC="numeric";
	//小数
	public final static String CELL_TYPE_DOUBLE="double";
	//时间格式
	public final static String CELL_TYPE_TIME="time";
	//百分比
	public final static String CELL_TYPE_PERCENT="percent";
	//坐席
	public final static String CELL_TYPE_AGENT="agent";
	
	/**
	 * 
	 * @param exportData 列
	 * @param rowMapper 行
	 * @param encoder 编码格式
	 * @param split 分割符
	 * @return
	 */
	public static InputStream createXLS( File writeFile,List<LinkedHashMap<String, String>> exportData, LinkedHashMap<String,String> rowMapper,String encoder, String split) {
		WritableWorkbook workBook=null;  
		 WritableSheet sheet=null;  
		 InputStream inputStream=null;
		 WorkbookSettings setting=new WorkbookSettings();
		 setting.setEncoding(encoder);
		 try {
			//创建工作薄
			if(writeFile.exists()){
				Workbook upwb=Workbook.getWorkbook(writeFile,setting);
				workBook=Workbook.createWorkbook(writeFile,upwb);
			}else{
				File pFile = writeFile.getParentFile();
				if(!pFile.exists()){
					pFile.mkdirs();
				}
				writeFile.createNewFile();
				workBook=Workbook.createWorkbook(writeFile,setting);
			}
			for(int m=0;m<1;m++){
				//设置默认字体
				WritableFont writeFont = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);  
				WritableCellFormat cellFormat = new WritableCellFormat(writeFont);  
				//如果工作表不存在,获取此sheet
				if(workBook.getNumberOfSheets()>m){
					sheet=workBook.getSheet(m);
				}else{//创建sheet
					sheet=workBook.createSheet("样本模板",m);
					//设置默认列宽
					sheet.getSettings().setDefaultColumnWidth(15); 
					//添加标题列
					int i=0;
					for(Map.Entry<String,String> entry:rowMapper.entrySet()){
						sheet.addCell(new Label(i,0,entry.getValue().trim(),cellFormat));
						i=i+1;
					}
				}
				//从工作表的第一行开始写
				if(null!=exportData&&exportData.size()>0){
					//遍历数据的行
					for(int i=0;i<exportData.size();i++){
						//遍历数据获取某一行的这个对象
						LinkedHashMap<String, String> obj=exportData.get(i);
						if(null!=obj&&obj.size()>0){
							for(int j=0;j<obj.size();j++){
								sheet.addCell(new Label(j,i+1,String.valueOf(obj.values().toArray()[j]).trim(),cellFormat));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionHandler.throwRuntimeException(e,"创建excel文件失败"+e.getMessage());
		} finally{
			try {
				//将数据写到文件中
				workBook.write();
				workBook.close();
				inputStream = new FileInputStream(writeFile);
				writeFile.delete();
			}catch(Exception e) {
				ExceptionHandler.throwRuntimeException(e,"创建excel文件失败"+e.getMessage());
			}
		}
		return inputStream;
	}
	
	/**
	 * 提供excel文件转换为csv文件功能
	 */
	public static File excel2CSV(File tempFile,String split,File newFile,String encoder,String placeHolder){
		return excel2CSV( tempFile,split,newFile,encoder,placeHolder,0);
	}
	/**
	 * 提供excel文件转换为csv文件功能
	 */
	public static File excel2CSV(File tempFile){
	  StringBuffer sb=new StringBuffer();
	  try{
		  WritableWorkbook wb = null;  
		  //构造Workbook（工作薄）对象   
		  Workbook upwb=Workbook.getWorkbook(tempFile);
		  wb=Workbook.createWorkbook(tempFile,upwb);
		  //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了   
		  WritableSheet[] sheets = wb.getSheets();  
		  WritableSheet sheet=sheets[sheets.length-2];
		  int rowCount=sheet.getRows();
		  for(int i=0;i<rowCount;i++){
			  WritableCell cells1 = sheet.getWritableCell(1,1);
			  WritableCell cells2 = sheet.getWritableCell(2,2);
			  String content=StringUtil.trim(cells1.getContents(),"");
			  if(!StringUtil.isBlank(cells2.getContents())){
				  content=content+(content.equals("")?"":";")+cells2.getContents();
			  }
			  jxl.write.Label lbl = new jxl.write.Label(1, 1,content);
			  sheet.addCell(lbl);
		  }
		  wb.write();
		  //最后关闭资源，释放内存   
		  wb.close();  
		//将字符串写入文件
	  }catch(Exception e){
		  ExceptionHandler.throwRuntimeException(e,"创建excel文件失败"+e.getMessage());
	  }
	  return null;
	}
	/**
	 * 提供excel文件转换为csv文件功能
	 */
	public static File excel2CSV(File tempFile,String split,File newFile,String encoder,String placeHolder,int ignoreLineInSheets){
	  StringBuffer sb=new StringBuffer();
	  try{
		  Workbook wb = null;  
		  //构造Workbook（工作薄）对象   
		  wb = Workbook.getWorkbook(tempFile);  
		  //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了   
		  Sheet[] sheets = wb.getSheets();  
		  if((sheets != null) && (sheets.length > 0)) {  
		   //对每个工作表进行循环   
		   for(int i=0; i<sheets.length; i++) {  
		    //得到当前工作表的行数   
		    int rowNum = sheets[i].getRows();  
		    int start=0;
		    if(ignoreLineInSheets<=0){
		    	start=0;
		    }else{
		    	start=ignoreLineInSheets;
		    }
		    for(int j=start; j<rowNum; j++) {  
		     //得到当前行的所有单元格   
		    StringBuffer csvRow=new StringBuffer();
		     Cell[] cells = sheets[i].getRow(j);
		     if((cells != null) && (cells.length > 0)) {  
		    	 //对每个单元格进行循环   
			      for(int k=0; k<cells.length; k++) {  
				       //读取当前单元格的值   
				       String cell = cells[k].getContents().trim();
				       //2013-04-02 当某字段为空字段时以空格占位
				       if(StringUtil.isBlank(cell)){
				    	   cell=placeHolder;
				       }
				       String cellContent="";
				       if(k!=cells.length-1){
				    	   cellContent=cell.trim()+split;
				       }else{
				    	   cellContent=cell.trim();
				       }
				       csvRow.append(cellContent); 
			      }  
		     }  
			 if(csvRow.toString().length()>(cells.length-1)*split.length()){
				//csv文件分行
				 if(j!=rowNum-1){
				    csvRow.append("\n");
				 }
				 sb.append(csvRow.toString());
			 }
		    }  
		   }     
		  }  
		  //最后关闭资源，释放内存   
		  wb.close();  
		//将字符串写入文件
		  OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(newFile,true),encoder);
		  writer.write(sb.toString());
		  writer.flush();
		  writer.close();
	  }catch(Exception e){
		  ExceptionHandler.throwRuntimeException(e,"创建excel文件失败"+e.getMessage());
	  }
	  return newFile;
	}
	/**
	 * 将excel表格转换为List<List<List<String>>>
	 * @param file
	 * @param ignoreLine
	 * @return
	 */
	public static List<List<List<String>>>  readExcelToList(File file,int ignoreLine){
		List<List<List<String>>>  allData=new ArrayList<List<List<String>>>();
		try{
			org.apache.poi.ss.usermodel.Workbook workbook = ExcelUtil.createWorkbook(new FileInputStream(file));  
			  //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
			  int sheetNumber=workbook.getNumberOfSheets();
			  //读取每个工作表
			  if(sheetNumber>0) {  
				   //对每个工作表进行循环，读取每个工作表
				   for(int i=0; i<sheetNumber; i++) {  
					   org.apache.poi.ss.usermodel.Sheet sheet=workbook.getSheetAt(i);
					    if(null==sheet){
					    	   continue;
					    }
					    List<List<String>> sheetList=new ArrayList<List<String>>();
					    //遍历每一行，跳过忽略行数
					    int r=0;
					    Iterator<Row> rows=sheet.iterator();
					    while(rows.hasNext()) {  
					    	r++;
					    	Row row=rows.next();
					    	List<String>rowList=new ArrayList<String>();
					    	if(null==row||r<=ignoreLine){
						    	   continue;
						    }
					        //得到当前行的所有单元格   
					    	if(null!=row){
					    	Iterator<org.apache.poi.ss.usermodel.Cell> cells=row.iterator();
					    	 //对每个单元格进行循环   
						      while(cells.hasNext()) {  
						    	  org.apache.poi.ss.usermodel.Cell cell=cells.next();
							       //读取当前单元格的值   
							       String cellStr ="";
							       if(null==cell){
							    	   continue;
							       }
							       if(cell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
							    	   cellStr=((Double)cell.getNumericCellValue()).toString();
							    	   BigDecimal b = new BigDecimal(cellStr);     
							    	   cellStr=b.toPlainString();  
							       }else  if(cell.getCellType()==XSSFCell.CELL_TYPE_STRING){
							    	   cellStr=cell.getStringCellValue();
							       }
							       rowList.add(cellStr);
						      } 
					    }
					        sheetList.add(rowList);
					    }  
					    allData.add(sheetList);
				   }     
			  }  
		  }catch(Exception e){
			  ExceptionHandler.throwRuntimeException(e,"读取excel文件失败"+e.getMessage());
		  }
		return allData;
	}
	/**
	 * 根据xls和xlsx不同返回不同的workbook
	 * @param inp
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static org.apache.poi.ss.usermodel.Workbook createWorkbook(InputStream inp) throws IOException,InvalidFormatException {
	    if (!inp.markSupported()) {
	        inp = new PushbackInputStream(inp, 8);
	    }
	    if (POIFSFileSystem.hasPOIFSHeader(inp)) {
	        return new HSSFWorkbook(inp);
	    }
	    if (POIXMLDocument.hasOOXMLHeader(inp)) {
	        return new XSSFWorkbook(OPCPackage.open(inp));
	    }
	    throw new IllegalArgumentException("无法解析的excel版本");
	}
	

	
	/**
	 * add by jinchaoyang 2013-06-14
	 * @param titles
	 * @param columns
	 * @param dataList
	 * @return
	 */
	public static HSSFWorkbook createExcel(String[][] titles,String[] columns,List dataList){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		//创建标题
		HSSFRow title = sheet.createRow(0);
		for(int i=0;i<titles.length;i++){
			title.createCell(i).setCellValue(titles[i][0]);
		}
		 //填充表格内容
		HSSFRow content  = null;
		JSONObject obj = null;
		HSSFCell cell=null;
		for(int i=0;i<dataList.size();i++){
			content = sheet.createRow(i+1);
			obj = JSONObject.fromObject(dataList.get(i));
			for(int j=0;j<titles.length;j++){
				cell=content.createCell(j);
				String dataType = titles[j][1].trim();
				String key = columns[j];
				if(CELL_TYPE_NUMBERIC.equals(dataType)){
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(obj.containsKey(key)){
						cell.setCellValue(obj.getInt(columns[j]));
					}else{
						cell.setCellValue(0);
					}
				}else if(CELL_TYPE_DOUBLE.equals(dataType)){
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(obj.containsKey(key)){
						cell.setCellValue(obj.getDouble(columns[j]));
					}else{
						cell.setCellValue(0.0);
					}
				}else if(CELL_TYPE_TIME.equals(dataType)){
					if(obj.containsKey(key)){
						String value = DateUtil.timeFomart(obj.getLong(key));
						cell.setCellValue(value);
					}
				}else if(CELL_TYPE_AGENT.equals(dataType)){
					//获取坐席的信息
					String agent=DicUtil.getValueByKey(Constant.DIC_ALL_AGENT_INFO_NAME, key);//DicUtil.getValueByKey(, key)
					cell.setCellValue(agent);
				}else{
					if(obj.containsKey(key)){
						cell.setCellValue(obj.getString(key));
					}
				}

			}
		}
		return wb;
	}
	/**
	 * 创建EXCEL文件
	 * @param titles  表头
	 * @param dataList 数据
	 * @return
	 */
	public static HSSFWorkbook createExcel(String[] titles,List<List<String>> dataList){
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		//创建标题
		HSSFRow title = sheet.createRow(0);
		for(int i=0;i<titles.length;i++){
			title.createCell(i).setCellValue(titles[i]);
		}
		 //填充表格内容
		HSSFRow content  = null;
		HSSFCell cell=null;
		List<String> element=null;
		for(int i=0;i<dataList.size();i++){
			content = sheet.createRow(i+1);
			  element = dataList.get(i);
			for(int j=0;j<element.size();j++){
				cell = content.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(element.get(j));
			}
		}
		return wb;
	}
	

	
	
	/**
	 * 默认为字符串类型的
	 * @param titles
	 * @param columns
	 * @param dataList
	 * @return
	 */
	public static HSSFWorkbook createExcel(String[] titles,String[] columns,List dataList){
		int len = titles.length;
		String[][] _titles = new String[len][len];
		for(int i=0;i<titles.length;i++){
			_titles[i][0] = titles[i];
			_titles[i][1] = CELL_TYPE_STRING;
		}
		HSSFWorkbook wb = createExcel(_titles, columns, dataList);
	    return wb;
	}
	/**
	 * 文件下载
	 * @param resp
	 * @param workBook
	 * @param fileName
	 */
	public static String writeExcel(HSSFWorkbook workBook,String fileName){
		FileOutputStream fos=null;
		fileName =DateUtil.dateFormat(new Date(), DateUtil.LONG_PATTERN)+"_"+fileName;
		fileName = getDownloadFilePath()+"/"+fileName;
		try {
			fos = new FileOutputStream(fileName);
			workBook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			logger.info("文件不存在", e);
			ExceptionHandler.throwRuntimeException(e, "文件不存在异常", logger);
		} catch (IOException e) {
			logger.info("文件写入过程中IO出错", e);
			ExceptionHandler.throwRuntimeException(e, "文件IO异常", logger);
		}
		return fileName;
	}
	
private static String getDownloadFilePath(){
	String filePath = ContextUtil.getProperty(Constant.DOWNLOAD_FILE_PATH_KEY,Constant.DEFAULT_DOWNLOAD_FILE_PATH);
	File file = Play.getFile(filePath);
	if(!file.exists()){
		file.mkdirs();
	}
	return file.getAbsolutePath();

}

	//xlsx
	public static File xlsx2CSV(File tempFile,String split,File newFile,String encoder,String placeHolder){
		 StringBuffer sb=new StringBuffer();
		
		 try{
			 InputStream fin = new FileInputStream(tempFile);
			  XSSFWorkbook xwb = new XSSFWorkbook(fin);
			  XSSFSheet sheet = xwb.getSheetAt(0);
			  XSSFRow row = null;
			   org.apache.poi.ss.usermodel.Cell cell = null;
			  //遍历每一行,getLastRowNum获得的是最后一行的下标，需+1
			  int rowNum = sheet.getLastRowNum()+1;
			  for(int i=0;i<rowNum;i++){
				  int columns=0;
				  StringBuffer csvRow = new StringBuffer();
				  row = sheet.getRow(i);
				  if(null!=row){
					  Iterator<org.apache.poi.ss.usermodel.Cell> cells=row.cellIterator();
					  int j=0;
					  while(cells.hasNext()){
						  cell = cells.next();
						  String content="";
						  if(cell!=null){
							  columns = columns+1;
							  content = StringUtil.trim(getCellValue(cell),placeHolder);
						  }
						  if(j!=0){
							 content = split+content;
						  }
						  csvRow.append(content);
						  j++;
					  }
				  }
				  //判断是否为空行
				  //if(csvRow.toString().length()>columns*split.length()){
				  if(!StringUtil.isBlank(csvRow.toString())){	
				  	if(i!=rowNum-1){
						  csvRow.append("\n");
					  }
					  sb.append(csvRow.toString());
				  }
				  
			  }
			//将字符串写入文件
			  OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(newFile,true),encoder);
			  writer.write(sb.toString());
			  writer.flush();
			  writer.close();
		  }catch(Exception e){
			  ExceptionHandler.throwRuntimeException(e,"创建excel文件失败"+e.getMessage());
		  }
		  return newFile;

	}
	
	
	public static String getCellValue(org.apache.poi.ss.usermodel.Cell cell){
		String value="";
		if(null!=cell){
			int type = cell.getCellType();
			if(type==cell.CELL_TYPE_BOOLEAN){
				value = String.valueOf(cell.getBooleanCellValue());
			}else if(type==cell.CELL_TYPE_NUMERIC){
				value = String.format("%.0f", cell.getNumericCellValue());
			}else{
				value = cell.getStringCellValue();
			}
		}
		return value;
		
	}
	
	
	public static File writeDataToFile(String[] title,List<List<String>> dataList,String fileName){
		String basePath = StringUtil.trim(ContextUtil.getProperty("download.filePath"));
		File dir = new File(basePath);
		if(!dir.exists()){
				dir.mkdirs();
		}
		if(!dir.canWrite()){
			dir.setWritable(true);
		}
		String sign = DateUtil.dateFormat(new Date(), DateUtil.FULL_PATTERN);
		if(!fileName.toLowerCase().endsWith(".xls")&&!fileName.toLowerCase().endsWith(".xlsx")){
			fileName = fileName+".xls";
		}
		basePath = basePath.endsWith("/")?basePath:basePath+"/";
		fileName = basePath+sign+"_"+fileName;
		File dataFile = new File(fileName);
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

	

	public static File writeToZipFile(String[] title,List<List<String>> dataList,String fileName){
		File dataFile=null;
		dataFile = writeDataToFile(title, dataList,fileName);
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
		zip(dir, targetFile);
		Files.deleteDirectory(dir);
		Files.delete(dataFile);
		return targetFile;
		
	}
	

	
	 public static void zip(File directory, File zipFile) {
	        try {
	            FileOutputStream os = new FileOutputStream(zipFile);
	            ZipOutputStream zos = new ZipOutputStream(os);
	            zos.setEncoding("UTF-8");
	            zipDirectory(directory, directory, zos);
	            zos.close();
	            os.close();
	        } catch (Exception e) {
	            throw new UnexpectedException(e);
	        }
	    }

	    static void zipDirectory(File root, File directory, ZipOutputStream zos) throws Exception {
	        for (File item : directory.listFiles()) {
	            if (item.isDirectory()) {
	                zipDirectory(root, item, zos);
	            } else {
	                byte[] readBuffer = new byte[2156];
	                int bytesIn;
	                FileInputStream fis = new FileInputStream(item);
	                String path = item.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
	                ZipEntry anEntry = new ZipEntry(path);
	                zos.putNextEntry(anEntry);
	                while ((bytesIn = fis.read(readBuffer)) != -1) {
	                    zos.write(readBuffer, 0, bytesIn);
	                }
	                fis.close();
	            }
	        }
	    }

	
}

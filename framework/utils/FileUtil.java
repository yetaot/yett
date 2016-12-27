package framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import play.Logger;

import framework.exceptions.ExceptionHandler;

/**
 * 文件工具类
 * @author 张科伟
 *
 */
public class FileUtil {
	/**
	 * 创建文件或文件夹
	 * @param dir 文件夹
	 * @param fileName 文件名
	 */
	public static File createNewFile(String dir,String fileName){
		File dirFile=new File(dir);
		if(dirFile.isDirectory()&&!dirFile.exists()){
			dirFile.mkdirs();
			dirFile=null;//释放内存，让垃圾回收机制回收内存
		}
		File newFile=new File(dir+"/"+fileName);
		if(!newFile.exists()&&newFile.isFile()){
			try {
				newFile.createNewFile();
			}catch (IOException e) {
				ExceptionHandler.throwRuntimeException(e,"创建文件或文件夹失败:"+e.getMessage());
			}
		}
		if(!newFile.exists()&&newFile.isDirectory()){
			newFile.mkdirs();
		}
		return newFile;
	}
	/**
	 * 获取文件的md5信息
	 * @param f
	 * @return
	 */
	public static String md5sum(File f){
		String result = null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(f);
			byte[] dataBytes = new byte[1024];
			int nread=0;
			while((nread = fis.read(dataBytes))!=-1){
				md.update(dataBytes,0,nread);
			}
			byte[] mdbytes = md.digest();
			StringBuffer sb = new StringBuffer("");
			for(int i=0;i<mdbytes.length;i++){
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			result = sb.toString();
		}catch(Exception e){
			ExceptionHandler.throwRuntimeException(e,"获取文件md5sum信息失败:"+e.getMessage());
		}
		return result;
	}
	
	/**
	 * 创建配置文件
	 * @param accountId
	 * @param content
	 * @param dir
	 * @return
	 */
	public static boolean createConfFile(String accountId, String content, String dir, String prefix) {
		Logger.info(String.format("[%s]: accountId<%s>, content<%s>, dir<%s>",  "createConfFile", accountId, content, dir));
		dir = dir.endsWith("/")?dir:dir+"/";
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String accountDir = dir + prefix + accountId + ".config";
		File accountFile = new File(accountDir);
		boolean flag = false;  
		FileOutputStream out = null;  
		try {  
			out = new FileOutputStream(accountFile);  
			out.write(content.getBytes("UTF-8"));  
			out.close();
		    flag=true;  
		  } catch (Exception e) {  
			  ExceptionHandler.throwRuntimeException(e, "创建配置文件失败: "+e.getMessage());
		  }
		  return flag;
	}
	
	public static String getModifiedTime(File file){
		long time = file.lastModified();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		cal.setTimeInMillis(time);
		return format.format(cal.getTime());
	}
}

package framework.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import framework.exceptions.ExceptionHandler;
/**
 * java生成csv文件工具类
 * @author 张科伟
 *
 */
public class CSVUtils {
	/**
	 * 
	 * @param exportData 列
	 * @param rowMapper 行
	 * @param encoder 编码格式
	 * @param split 分割符
	 * @return
	 */
	public static InputStream createCSV(List exportData,LinkedHashMap rowMapper,String encoder,String split,String lineSplit){
		StringBuffer sb=new StringBuffer();
		ByteArrayInputStream inputStream=null;
		try {
			// 写入文件头部
			for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
				java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
				sb.append(""+ propertyEntry.getValue().toString()+"");
				if (propertyIterator.hasNext()) {
					sb.append(split);
				}
			}
			sb.append(lineSplit);
			// 写入文件内容
			for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
				LinkedHashMap row = (LinkedHashMap) iterator.next();
				for (Iterator propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext();) {
					java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
					sb.append(""+propertyEntry.getValue().toString()+"");
					if (propertyIterator.hasNext()) {
						sb.append(split);
					}
				}
				if (iterator.hasNext()) {
					sb.append(lineSplit);
				}
			}
		} catch (Exception e) {
			ExceptionHandler.throwRuntimeException(e,"创建csv文件失败");
		} finally {
			try {
				inputStream=new ByteArrayInputStream(sb.toString().getBytes(encoder));
			} catch (UnsupportedEncodingException e) {
				ExceptionHandler.throwRuntimeException(e,sb.toString()+"创建csv文件失败:系统不支持”"+encoder+"“字符集.");
			}
		}
		return inputStream;
	}
}
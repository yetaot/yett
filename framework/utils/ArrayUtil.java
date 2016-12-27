package framework.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 数组对象工具类
 * @author jinchaoyang
 *
 */
public class ArrayUtil {
	public static String[] getArray(String[] arr,String[] defaultArr){
		if(null==arr||arr.length==0){
			return defaultArr;
		}else{
			return arr;
		}
	}
	public static String[] getArray(String[] arr){
		if(null==arr||arr.length==0){
			return new String[]{};
		}else{
			return arr;
		}
	}
	public static Long[] getLongArray(Long[] arr){
		if(null==arr||arr.length==0){
			return new Long[]{};
		}else{
			return arr;
		}
	}
	/**
	 * 字符串数组中是否含有某个元素
	 * @param arr
	 * @param str
	 * @return
	 */
	public static boolean contains(String[] arr,String str){
		   boolean status = false;
		   for(String a : arr){
			   if(a.trim().equals(str.trim())){
				   status = true;
				   break;
			   }
		   }
		   return status;
	   }
	/**
	 * 判断数组中是否有重复的值
	 * @param arr
	 * @return
	 */
	public static boolean ifReduplicate(String[] arr){
		  Set results = new HashSet();
		  for(String a :arr){
			results.add(a); 
		  }
		  if(arr.length!=results.size())
			  return true;
		  return false;
	   }
	
	public static String[] delete(String[] array,String ... chars){
		for(int i=0;i<array.length;i++){
			for(String c : chars){
				array[i] = array[i].replace(c,"");
			}
		}
		return array;
	}
	
	public static String toSqlParameters(String[] arr){
		String result ="";
		for(String s : arr){
			if(StringUtil.isBlank(result)){
				result = "'"+s.trim()+"'";
			}else{
				result = result+",'"+s.trim()+"'";
			}
		}
		return result;
	}
	
	public static String toSqlParameters(Object[] arr){
		if(arr==null){
			arr = new Object[0];
		}
		String[] strArray = new String[arr.length];
	    for(int i=0;i<arr.length;i++){
	    	strArray[i] = String.valueOf(arr[i]);
	    }
	    return toSqlParameters(strArray);
	}
	
	/**
	 * 判断数组是否为空数组
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array){
		boolean status = true;
		if(array!=null && array.length>0){
			if(!(array.length==1&&StringUtil.isBlank(String.valueOf(array[0])))){
				status = false;
			}
		}
		return status;
	}
	/**
	 * 获取两个数组的交集
	 * @param arr0
	 * @param arr1
	 * @return
	 */
	public static String[] retainAll(String[] arr0,String[] arr1){
		List<String> result = new ArrayList<String>();
		for(int i=0;i<arr0.length;i++){
			for(int j=0;j<arr1.length;j++){
				if(arr0[i].equals(arr1[j])){
					result.add(arr0[i]);
					break;
				}
			}
		}
		return result.toArray(new String[0]);
	}
	/**
	 * 随机数组
	 * @param objs
	 * @param size
	 * @return
	 */
	public static Object[] rand(Object[] objs,long size){
		List<Object> newList=new ArrayList<Object>();
		if(size>=objs.length){
			return objs;
		}
		Random random=new Random();
		Object index=null;
		while(newList.size()<size){
			if(!newList.contains((index=objs[random.nextInt(objs.length)])))newList.add(index);
		}
		return newList.toArray(new Object[]{});
	}
	
	public static String fill(String string, Integer length) {
		length=null==length||length<1?1:length;
		String[] st=new String[length];
		for(int i=0;i<length;i++){
			st[i]=string;
		}
		return CollectionUtil.join(st,"");
	}
}

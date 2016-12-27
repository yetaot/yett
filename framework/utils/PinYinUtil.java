package framework.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {
	/**
	 * 获取完整的拼音
	 * @param chinese
	 * @return
	 */
	public static String getSpell(String chinese){
		 String pinyinName = "";  
	     String firstChars="";
		 char[] nameChar = chinese.toCharArray();  
	     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
	     defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);  
	     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	     defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	     for (int i = 0; i < nameChar.length; i++) {  
	       if (nameChar[i] > 128) {  
	         try {  
	        	 String[] _array = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
	        	 if(!ArrayUtil.isEmpty(_array)){
	        		 pinyinName += _array[0]; 
	        		 firstChars +=_array[0].charAt(0);
	        	 }
	             } catch (BadHanyuPinyinOutputFormatCombination e) {  
	                e.printStackTrace();  
	            }  
	         }else{  
	             pinyinName += nameChar[i];  
	         }  
	      }  
	     return pinyinName+"_"+firstChars;
	}
	
}

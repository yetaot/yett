package framework.tags;

import framework.base.Constant;


import framework.base.Pager;
import framework.utils.DicUtil;
import framework.utils.StringUtil;
import framework.utils.SysParamUtil;
import groovy.lang.Closure;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;

@FastTags.Namespace("f.tags")
public class TagUtil extends FastTags{
	public static void _dicValue(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine){
		String dicName = (String)args.get("arg");
		String dicKey = (String)args.get("key");
		String emCode = (String)args.get("code");
		String prefix=(String)args.get("prefix");
		dicName = StringUtil.trim(dicName, "");
		dicKey = StringUtil.trim(dicKey,"");
		emCode = StringUtil.trim(emCode);
		if("true".equals(prefix)){
			dicName = emCode+"_"+dicName;
		}
		String value = DicUtil.getValueByKey(dicName,dicKey);
		if(null==value){
			value="";
		}
		out.println(value);
	}
	
	/**
	 * 从字典返回技能组option
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _dics(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine){
		String dicConfig = (String)args.get("dicConfig");
		dicConfig = (String)args.get("dicConfig");
		String dicName = (String)args.get("dicName");
		dicName = StringUtil.trim(dicName,"");
		String blankStr=(String)args.get("showBlank");
		blankStr = StringUtil.trim(blankStr,"");
		boolean showBlank = Boolean.parseBoolean(blankStr);
		Map<String,String> dics = new LinkedHashMap<String,String>();
		if(StringUtil.isBlank(dicConfig)){
			dics=DicUtil.getAllByDicName(dicName);
		}else{
			dics=DicUtil.getAllByDicName(dicConfig, dicName);
		}
		
		StringBuffer sb=new StringBuffer();
		if(showBlank){
			sb.append("<option value=''>全部</option>");
		}
		if(null!=dicName&&!dicName.isEmpty()){
			for(Entry<String,String> entry:dics.entrySet()){
				sb.append("<option value='"+entry.getKey()+"'>"+entry.getValue()+"</option>");
			}
		}
		out.write(sb.toString());
	}
	/**
	 * 列表分页 pager:pager对象 url:路径设置
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 * @throws UnsupportedEncodingException 
	 */
	public static void _pagination(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine) throws UnsupportedEncodingException{
		Pager pager = (Pager) args.get("pager");
		String url = (String) args.get("url");
		String setSize =(String)args.get("setSize");
		pager = pager==null?new Pager():pager;
		setSize =StringUtil.trim(setSize, "true");
		pager.setPath(url);
		Map<String,String> params = pager.allParams();
		Set<String> keys = params.keySet();
		Random random = new Random();
		int sign = random.nextInt(100000000);
		StringBuffer page = new StringBuffer("<form  name=\"_pagerForm\" action='"+url+"' data-override='true'  method=\"GET\" class=\"_pagerForm\" id='form_"+sign+"'>");
		for(String key : keys){
			page.append("<input type='hidden' name='"+key+"' value='"+ java.net.URLDecoder.decode(params.get(key),"utf-8")+"'/>");
		}
		page.append("<input type='hidden' name='pageSize' rel='pageSize' />");
		page.append("<input type='hidden' name='page' value='1' />");
		page.append("</form>");
		page.append("<input type='hidden' name='pageSize' value='"+pager.pageSize+"'/><input type='hidden' name='currentPage' value='"+pager.pageNo+"'/><input type='hidden' name='currentUrl' value='"+pager.getUrl(pager.pageNo)+"'/>");
		page.append("<div class=\"paging_bj clear\"><div class=\"nums_bj\"><p class=\"nums_infor\">当前第<em>");
		int totalPage = pager.totalPage<=0?1:pager.totalPage;
		page.append(pager.pageNo);
		 page.append("</em>页 / 每页");
		 if(setSize.equalsIgnoreCase("true")){
		   page.append("<select class=\"infor_select num_inf\" id='"+sign+"'>");
		   for(int i=10;i<=50;i+=10){
			   page.append("<option "+(i==pager.pageSize?" selected ":"")+" value='"+i+"'>"+i+"</option>");
		   }
		   page.append("<option "+(100==pager.pageSize?" selected ":"")+" value='"+100+"'>"+100+"</option>");
		   page.append("</select>");
		   page.append("<script type='text/javascript'>(function(){$('.num_inf').bind('change',function(){var page = this.value; var _sign = this.id;$('input[rel=\"pageSize\"]').val(page);$('#form_'+_sign).submit();})})();</script>");
		 }else{
		   page.append(Constant.DEFAULT_PAGE_SIZE); 
		 }
		   page.append("条记录");
		   if(totalPage>0){
			   page.append("（共<i>"+pager.totalRow+"</i>条）");
		   }
		   page.append("</p></div><span class=\"paging\">");
		   if(pager.pageNo<=1){
			   page.append("<a class=\"pagAll_l\" alt='首页' title='首页'></a>");
			   page.append("<a class=\"pag_l\" alt='上一页' title='上一页'></a>");
		   }else{
			  page.append("<a class=\"pagAll_l pagAll_lH\" alt='首页' title='首页' href='"+pager.getUrl(pager.firstPage)+"'></a>");
			  page.append("<a class=\"pag_l pag_lH\"  alt='上一页' title='上一页' href='"+pager.getUrl(pager.prePage)+"'></a>");
		   }
		   if(pager.pageNo>=totalPage){
			   page.append("<a class=\"pag_r\" alt='下一页' title='下一页' ></a>");
			   page.append("<a class=\"pagAll_r\" alt='末页' title='末页'></a>");
		   }else{
			   page.append("<a class=\"pag_r pag_rH\" alt='下一页' title='下一页' href='"+pager.getUrl(pager.nextPage)+"' ></a>");
			   page.append("<a class=\"pagAll_r pagAll_rH\" alt='末页' title='末页' href='"+pager.getUrl(pager.lastPage)+"'></a>");
		   }
	   page.append("</span></div>");
	   out.println(page.toString());
	}
	
	public static void _agent(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine){
		String dicKey = (String)args.get("arg");
		dicKey = StringUtil.trim(dicKey, "");
		String dicName =Constant.DIC_ALL_JOBCODE_NAME;
		String value = DicUtil.getValueByKey(dicName,dicKey);
		value = StringUtil.trim(value);
		out.println(value+"["+dicKey+"]");
	}

	public static void _agentName(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine){
		String dicKey = (String)args.get("arg");
		dicKey = StringUtil.trim(dicKey, "");
		String jobCode = DicUtil.getValueByKey(Constant.DIC_ALL_USERID2JOBCODE_NAME, dicKey);
		jobCode = StringUtil.trim(jobCode);
		String agentName = DicUtil.getValueByKey(Constant.DIC_ALL_USER_NAME, dicKey);
		agentName = StringUtil.trim(agentName);		
		out.println(agentName+"["+jobCode+"]");
	}	
	
	public static void _encrypt(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine){

		String phone = (String)args.get("arg");
		phone = StringUtil.trim(phone,"");
		Map<String,String> enctryParams = SysParamUtil.getValuesByKey("systemEnctry");
		String allowEnctry = enctryParams.get("systemEnctry.allowEnctry");
		String enctryLen = enctryParams.get("systemEnctry.enctryLength");
		enctryLen = StringUtil.trim(enctryLen,"0");
		String enctryType = enctryParams.get("systemEnctry.enctryType");
		enctryType = StringUtil.trim(enctryType,"right");
		String enctryChar = enctryParams.get("systemEnctry.enctryChar");
		enctryChar = StringUtil.trim(enctryChar,"*");
		String result = phone;
		if("true".equalsIgnoreCase(allowEnctry)){ //如果允许加密
		int phoneLen = phone.length();
		int len = Integer.parseInt(enctryLen);
		len = (len>=phoneLen)? phoneLen:len;
		String replaceStr = "";
		for(int i=0;i<len;i++){
		replaceStr = replaceStr+enctryChar;
		}
		String subStr="";
		if("left".equalsIgnoreCase(enctryType)){ //左侧加密
		subStr = phone.substring(0, len);
		result = phone.replaceFirst(subStr,replaceStr);
		}else if("middle".equalsIgnoreCase(enctryType)){
		int diff = (phoneLen-len)/2;
		subStr = phone.substring(diff, diff+len);
		result = phone.replaceFirst(subStr,replaceStr);
		}else if("right".equalsIgnoreCase(enctryType)){
		subStr = phone.substring(phoneLen-len, phoneLen);
		result = phone.replaceFirst(subStr, replaceStr);
		}
		}
		out.println(result);

		}
	
	public static void _encryptIdCard(Map<?,?> args,Closure body,PrintWriter out,ExecutableTemplate template,int fromLine){
		String idCard = (String)args.get("arg");
		String code = (String)args.get("code");
		idCard = StringUtil.trim(idCard,"");
		code = StringUtil.trim(code,"");
		String enctryLen = "8";
		String enctryChar = "*";
		String result = idCard;
		if (idCard.length() > 0 && code.length() > 3) {
			int idCardLen = idCard.length();
			int len = Integer.parseInt(enctryLen);
			len = (len>=idCardLen)? idCardLen:len;
			String replaceStr = "";
			for(int i=0;i<len;i++){
				replaceStr = replaceStr+enctryChar;
			}
			String subStr = idCard.substring(6, 14);
			result = idCard.replaceFirst(subStr, replaceStr);
		}
		out.println(result);
	}
}

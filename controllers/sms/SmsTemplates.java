package controllers.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.sms.SmsTemplate;
import models.sms.SmsTemplateConfig;
import models.sms.vo.SmsBatchSendLogQuery;
import models.sms.vo.SmsTemplateQuery;
import models.utils.SecurityConstant;
import play.data.binding.ParamNode;
import play.data.validation.Valid;
import play.mvc.After;
import framework.base.BaseController;
import framework.base.Constant;
import framework.base.PagerRS;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

/**
 * 短信模板
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-1-28 下午11:46:58
 */
public class SmsTemplates extends BaseController {
	 public static void index(Integer page,SmsTemplateQuery query){
		 if( null == query){
				query = new SmsTemplateQuery();
		}
  	    PagerRS<SmsTemplate> rs = (PagerRS<SmsTemplate>) SmsTemplate.findByPager(getQuery(query,SmsTemplateQuery.class),getPageNo(page),getPageSize());
        render(rs);
     }
     public static void add(){
  	   	render();
     }
     
     public static void create(@Valid SmsTemplate template){
  	   if(!validation.hasErrors()){
  		   String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
  		   template.createdAt = new Date();
  		   template.creatorId = Long.valueOf(currUserId);
  		   template.ifUse = 1;
  		   template.operatorId = Long.valueOf(currUserId);
  		   template.updatedAt = new Date();
  		   if(!StringUtil.isBlank(template.templateCode)){
  			 template.templateCode = "*6"+template.templateCode;
  		   }
  		   String ts = params.get("ts");
			if("0".equals(ts)){
				template.submittedAt = new Date();
				template.status = "pending";
				saveUserLogAndNotice("短信模板【"+template.name+"】提交成功", true);
				
			}else{
				template.status = "ready";
				saveUserLogAndNotice("短信模板【"+template.name+"】暂存成功", true);
			}
  		   template.create();
  		 index(null,null);
  	   }else{
  		 render("@add",template); 
  	   }
     }
     
     public static void edit(long id){
  	   SmsTemplate template = SmsTemplate.findById(id);
  	   render(template);
     }
     
     public static void update(long id){
    	 String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
    	 SmsTemplate template = SmsTemplate.findById(id);
    	 template.edit(ParamNode.convert(params.all()), "template");
    	 validation.valid(template);
    	 if(!validation.hasErrors()){
    		 template.updatedAt=new Date();
    		 template.operatorId = Long.valueOf(currUserId);
    		 if(!StringUtil.isBlank(template.templateCode)){
      			 template.templateCode = "*6"+template.templateCode;
      		  }else{
      			template.templateCode = null;
      		  }
    		 template.submittedAt = new Date();
    		 template.status = "pending";
    		 template.auditedAt = null;
    		 template.auditor = 0L;
    		 template.auditReason = null;
    		 template.save();
    		 SmsTemplateConfig.deleteByTemplateId(template.id);
    		 saveUserLogAndNotice("短信模板【"+template.name+"】提交成功", true);
    		 index(null, null);
    	   }else{
    		   render("@edit",template);   
    	   }
     }
     
     public static void show(long id){
    	 SmsTemplate template = SmsTemplate.findById(id);
    	 render(template);
     }
     
     public static void destroy(long id){
    	 SmsTemplate template = SmsTemplate.findById(id);
    	 String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
    	 if(null != template){
    		 template.ifUse = 0;
    		 template.operatorId = Long.valueOf(currUserId);
    		 template.updatedAt = new Date();
    		 template.save();
    	 }
    	 saveUserLogAndNotice("短信模板【"+template.name+"】删除成功", true);
  	    index(null,null);
     }
 	/**
 	 * 审核列表
 	 * @param page
 	 * @param query
 	 */
 	public static void auditQuery(Integer page, SmsTemplateQuery query){
 		if(null == query){
 			query = new SmsTemplateQuery();
 		}
 		query.status = "pending";
 		query.orderBy = "0";
 		PagerRS<SmsTemplate> rs = (PagerRS<SmsTemplate>) SmsTemplate.findByPager(getQuery(query,SmsTemplateQuery.class),getPageNo(page),getPageSize());
 		render(rs);
 	}
 	/**
 	 * 审核页面
 	 * @param id
 	 */
 	public static void audit(long id){
 		 SmsTemplate template = SmsTemplate.findById(id);
    	 render(template);
 	}
 	/**
 	 * 审核操作
 	 * @param id
 	 * @param result
 	 * @param auditReason
 	 */
 	public static void auditAction(long id, String result, String auditReason){
 		SmsTemplate template = SmsTemplate.findById(id);
		 String currUserId = StringUtil.trim(SecurityUtil.getLoginUserId(), "0");
		template.status = result;
		template.auditor = Long.valueOf(currUserId);
		template.auditReason = auditReason;
		template.auditedAt = new Date();
		template.save();
		auditQuery(null,null);
 	}
 	 /**
 	 * 生命周期
 	 */
 	@After(only={"sms.SmsTemplates.create","sms.SmsTemplates.update","sms.SmsTemplates.destroy","sms.SmsTemplates.auditAction"})
 	public static void lifeCycle(){
 		DicUtil.loadByDicName(SecurityConstant.DIC_SMS_TEMPLATE_NAME);
 	}
}

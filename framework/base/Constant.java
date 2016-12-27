package framework.base;

import framework.utils.ContextUtil;

public class Constant {
	
	//数据字典前缀
	public static final  String DIC_PREFIX="DIC.";
	public static final  String DIC_BASE_PREFIX="DIC[._]"; 
	//数据字典存值顺序
	public static final  String DIC_INDEX="DIC_INDEX.";
	//用户组在数据字典中的字典名称
	public static final String DIC_USER_GROUP_NAME="groupList";

	//可用的数据用户数据列表
	public static final String DIC_USER_NAME = "userList";
	public static final String DIC_JOBCODE_NAME = "jobCodeList";
	public static final String  DIC_USERID2JOBCODE_NAME="id2JobCodeList";
	public static final String  DIC_JOBCODE_2_GROUP="allJobCode2Group";
	public static final String DIC_AGENT_INFO_NAME="agentInfo";
	public static final String DIC_GROUP_USERS="groupUser";//用户组和多个用户ID的对应关系

	//所有的用户数据列表
	public static final String DIC_ALL_USER_NAME = "allUser";
	public static final String DIC_ALL_JOBCODE_NAME = "allJobCode";
	public static final String  DIC_ALL_USERID2JOBCODE_NAME="allId2JobCode";
	public static final String DIC_ALL_JOBCODE2USERID = "allJobCode2Id";
	public static final String DIC_ALL_AGENT_INFO_NAME="allAgentInfo";
	public static final String DIC_ALL_LINENUMBER_PHONE = "lineNumber2Phone";
	public static final String DIC_ALL_JOBCODE_LINENUMBER = "jobCode2LineNumber";
	public static final String DIC_ALL_JOBCODE_PHONE ="jobCode2Phone";

	
	public static final String DIC_TASK_QUEUE="TASKS_QUEUE";
	public static final String DIC_DOWNLOAD_QUEUE="DOWNLOAD_QUEUE";
	

	public static final String DIC_ROLE_NAME = "roleList";
	public static final String DIC_PLATFORM_ROLE_NAME="platformRoleList";
	
	
	
	public static final String PAGE_SIZE_PARAM_NAME="pageSize";
	public static final String CURRENT_PAGE_PARAM_NAME="page";
	public static final String DIC_IFUSE_NAME="codeInfo.ifUse";
	public static final String DIC_YES_OR_NO_NAME="codeInfo.yesOrNo";
	
	public static final String DIC_OPERATE_TYPE_NAME="codeInfo.operateType";
	public static final String DIC_TASK_STATUS_NAME="codeInfo.taskStatus";
	
	public static final String PREFIX_PAGE_PARAM_NAME="query.";
	

	public static final String DIC_TYPE_ID_DETAIL_CODE_NAME="IDDetailCodeList";
	
	public static final String DIC_TYPE_ID_DETAIL_CODE_PREFIX="CD_";
	
	public final static String DIC_SERVICE_LIST_NAME="RESOURCE.SERVICE";
	
	public final static int DEFAULT_PAGE_SIZE=10;
	
	//登陆时选择的接听方式
	public static final String DIC_ANSWER_TYPE_NAME="platform.answerType";
	public static final String RESOURCE_TYPE_MENU_NAME="MENU";
	public static final String RESOURCE_TYPE_ACTION_NAME="ACTION";
	//系统部署方式
	public static final String APP_DEPLOY_TYPE=getAppDeployType();
			
	public static String getAppDeployType(){
		return  ContextUtil.getProperty("em.app.deploytype", "remote");
	}
	
	

	
	public final static String DOWNLOAD_FILE_PATH_KEY="download.filePath";
	
	public final static  String DEFAULT_DOWNLOAD_FILE_PATH="/public/download/";
	
	//字典--性别
	public final static String DIC_SEX_TYPE_NAME="codeInfo.sex";
	//系统消息队列
	public final static String DIC_MESSAGE_QUEUE_NAME="SYS_MESSAGE_QUEUE";
	public final static String DIC_USER_MESSAGE_QUE_PREFIX="USER_MESSAGE_QUEUE_";
	
	public final static String CFG_MESSAGE_RECEIVE="message.receive";
	
	
	public final static String MESSAGE_TYPE_FILE_DOWN="fileDownload";//文件下载
	public final static String MESSAGE_TYPE_WORK_ORDER="workOrder";//工单提醒
	public final static String MESSAGE_TYPE_SYSTEM="system";//系统
	public final static String MESSAGE_TYPE_BOOKED="booked";//预约回访
	
	public final static String CFG_AGENT_APPNAME="CFG_APP_NAME";
    	
	
	public static String DIC_FS_LIST_NAME="fsList";
	
	

	//系统参数字典名称
	public static final String SYSTEM_PARAMS_DICNAME="sysParams";
	
	//基于数据库的开放用户配置权限的数据库字典
	public static final String DIC_DB="dbDic";
	public static final String DIC_PUBLIC_DB="dbDic.publicDic";
	//客服自己设置的挂机后的状态
	public final static String DIC_AGENT_CONFIG_STATE_NAME="CFG_AGENT_STATE";
	public final static String AGENTBAR_READY_STATE_KEY="agentSet.lAutoReady";
	public final static String DIC_TEMPLATE_CFG_NAME="CFG_TEMPLATE_NAME";
	
	
	public final static String DIC_SESSION_ID_QUEUE="SESSION_ROUTE";
	
	
	//上传文件存放的路径
	public final static String ATTACHMENT_FILE_PATH=getAttachmentPath();
	
	public static String getAttachmentPath(){
		return ContextUtil.getProperty("fileupload.path");
	}
	//公告类型
	public final static String DIC_ANNOUNCEMENT_TYPE_NAME="codeInfo.announcementType";
	
	/**
	 * 分机配置样例文件信息
	 * @return
	 */
	public static String deviceDefaultConfig(){
		return ContextUtil.getProperty("fs.device.defaultConfig","/conf/freeswitch/default_device.xml");
	}
	/**
	 * freeswitch变量配置文件
	 * @return
	 */
	public static String varsDefaultConfig(){
		return ContextUtil.getProperty("fs.vars.defaultConfig","/conf/freeswitch/vars_custom.xml");
	}
	/**
	 * 分机配置文件路径
	 * @return
	 */
	public static String extConfigPath(){
		return ContextUtil.getProperty("fs.device.confPath","/conf/freeswitch/extensions/");
	}
	
	
	public final static String DIC_LOCKED_ACCOUNTS = "LOCKED_ACCOUNTS_LIST";
	
	
	public final static String DIC_MESSAGE_SEND_QUEUE="SYS_MESSAGE_SEND_QUEUE";
	
	
	public final static String MESSAGE_SMS_TYPE_NAME="sms"; //短信
	public final static String MESSAGE_MESSAGE_TYPE_NAME="message";//消息

}

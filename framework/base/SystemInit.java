package framework.base;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import framework.utils.DicUtil;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;
@OnApplicationStart
public class SystemInit extends Job{
private final static String TEMPLATE_FILE_NAME="conf/template/message.yml";
private final static String TEMPLATE_YAML_FILE_NAME="template/message.yml";
	@Override
	public void doJob() throws Exception {
		//加载字典
		DicUtil.load();
		loadMessageTemplate();
	}
	/**
	 * 加载消息的模板信息
	 */
	public static void loadMessageTemplate(){
		File f = Play.getFile(TEMPLATE_FILE_NAME);
		if(f.exists()){
			List<Map<String,String>> templates = (List<Map<String, String>>) Fixtures.loadYamlAsList(TEMPLATE_YAML_FILE_NAME);
			for(int i=0;i<templates.size();i++){
				Map<String,String> e = templates.get(i);
				String name=e.get("name");
				DicUtil.setValueByKey(Constant.DIC_TEMPLATE_CFG_NAME,name,JSONObject.fromObject(e).toString());
			}
		}
	}
}

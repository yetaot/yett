package models.jobs;

import java.security.MessageDigest;

import javax.xml.namespace.QName;

import models.utils.XmlUtil;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import framework.utils.StringUtil;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
@Every("30min")
@OnApplicationStart
public class GetOrderJob extends Job {

	@Override
	public void doJob() throws Exception {
		doProcess();
	}
	public static void doProcess() throws Exception{
		String temp = null;
		String result=null;
		for(int j=1;j<8;j++){
			temp = XmlUtil.pis_req_pdsj(XmlUtil.xmlId, j+"", "1", "30");
			result = XmlUtil.send(temp,XmlUtil.queryPdsjList);
			XmlUtil.tobean(result,j);
			String stringInt = XmlUtil.getReturn(result,XmlUtil.count);
			int x= 0;
			if(!StringUtil.isBlank(stringInt)){
				x=Integer.parseInt(stringInt);
			}
			for(int i=2;i<x/30+2;i++){
				temp = XmlUtil.pis_req_pdsj(XmlUtil.xmlId, j+"", i+"", "30");
				result = XmlUtil.send(temp,XmlUtil.queryPdsjList);
				XmlUtil.tobean(result,j);
			}
		}
	}
}

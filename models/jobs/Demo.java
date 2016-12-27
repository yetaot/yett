package models.jobs;

import java.security.MessageDigest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;

import framework.utils.DateUtil;
import models.utils.XmlUtil;

public class Demo {
	public static void main(String[] args) throws Exception{
		// 调用方法
		String endpoint = "http://10.212.202.229:8989/bkgd/services/unifiedExternal?wsdl";
		// 直接引用远程的wsdl文件
		//String temp = XmlUtil.pis_req_pdsj("123456", "8", "1", "30","20160721140100530472");
		String temp = XmlUtil.pis_req_pdsj("123456", "1", "1", "30");
		// 以下都是套路
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(endpoint);
		String namespace = "";// 命名空间

		SOAPHeaderElement header = new SOAPHeaderElement(namespace,
				"Authentication");
		header.setPrefix("");// 前缀
		header.addChildElement("authenticationAccount").addTextNode("DT"); // 帐号
		header.addChildElement("authenticationCode").addTextNode(
				MD5("dtunifiedexternal")); // 密码

		call.addHeader(header);
		call.setOperationName(new QName(
				"http://service.webService.becoda.com/", "queryPdsjList"));// WSDL里面描述的接口名称
		call.addParameter("reqXml",
				org.apache.axis.encoding.XMLType.XSD_DATE,
				javax.xml.rpc.ParameterMode.IN);// 接口的参数
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
		String result = "";
		try {
			result = (String) call.invoke(new Object[] { temp });
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 给方法传递参数，并且调用方法
		System.out.println("result is "+result+"!!!");
	}

	private static void demo() {
		String result="";
		try {
			//String temp = XmlUtil.pis_req_limitTime("123456","2","2016-08-10 16:11:35","01");
			//String temp = XmlUtil.pis_delay_pdsj("123456","20160624134800370579","3092897","测试事实上","2","2016-08-12 16:11:35");
			String temp = XmlUtil.pis_req_pdsj("123456", "8", "1", "30","20160722165100533332");
			//String temp = XmlUtil.pis_claim_pdsj("123456","20160706150200370636","3098007");
			result = XmlUtil.send(temp,"queryPdsjList");
			//XmlUtil.tobean(result, 1);
			System.out.println("result="+result+"!!!");
			//System.out.println(XmlUtil.getReturn(result,"message"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	public static long getDaySub(String beginDateStr,String endDateStr) 
	{ 
	long day=0; 
	java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	java.util.Date beginDate; 
	java.util.Date endDate; 
	try 
	{ 
	beginDate = format.parse(beginDateStr); 
	endDate= format.parse(endDateStr); 
	day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
	//System.out.println("相隔的天数="+day); 
	} catch (ParseException e) 
	{ 
	// TODO 自动生成 catch 块 
	e.printStackTrace(); 
	} 
	return day; 
	} 
}

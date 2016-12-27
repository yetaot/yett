package models.em;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import models.utils.SecurityConstant;

import org.apache.log4j.Logger;

import play.data.validation.Match;
import play.data.validation.Required;
import play.data.validation.Unique;
import framework.base.BaseModel;
import framework.logs.LogUtil;
import framework.utils.CollectionUtil;
import framework.utils.DicUtil;
import framework.utils.StringUtil;
@Entity
@Table(name="T_IP_TABLE")
public class IPTable extends BaseModel {
	
	private static final Logger logger = LogUtil.getInstance(IPTable.class);
	
	//服务ip地址
	@Unique(message="IP地址不能重复")
	@Required(message="IP不能为空")
	@Match(value="([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}",message="请输入正确的IP地址")
	public String ip;
	//服务名称
	public String name;
	//创建时间
	public Date createdAt;
	//更新时间
	public Date updatedAt;
	//操作人
	public long operator;
	//是否启用--0:否,1:是
	public int ifUse;
	
	public IPTable(){
		this.createdAt = new Date();
		this.updatedAt = new Date();
		this.ifUse = 1;
	}
	
	/**
	 * 删除
	 */
	public void destory(){
		this.delete();
	}
	
	/**
	 * 获取所有启用的ip
	 * @return
	 */
	public static List<IPTable> findActiveIps(){
		String sql = "FROM IPTable config WHERE config.ifUse = ?";
		return IPTable.find(sql, 1).fetch();
	}
	
	public static String iptables(){
		Map<String, String> ips = null;
		try {
			ips = DicUtil.getAllByDicName(SecurityConstant.DIC_IP_LIST_NAME);
		} catch (Exception e) {
			ips = new LinkedHashMap<String, String>();
			logger.error(String.format("[%s]: errorMessage<%s>", "iptables::GET IPTABLES FAILED", e.getMessage()));
		}
		String results = "127.0.0.1";
		if (!ips.isEmpty()) {
			String iptables = CollectionUtil.join(ips.keySet(), ",");
			if (iptables.indexOf(results) < 0) {
				results = iptables + "," +results;
			}
		}
		logger.info(String.format("[%s]: iptables<%s>", "findIpsToStr", results));
		return results;
	}
	
}

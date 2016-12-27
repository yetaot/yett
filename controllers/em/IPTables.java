package controllers.em;

import java.util.Date;

import models.em.IPTable;
import models.em.vo.IPTableQuery;
import models.utils.SecurityConstant;

import play.data.binding.ParamNode;
import play.data.validation.Valid;
import play.mvc.After;
import framework.base.BaseController;
import framework.base.PagerRS;
import framework.utils.DicUtil;
import framework.utils.SecurityUtil;
import framework.utils.StringUtil;

public class IPTables extends BaseController {
	
	public static void index(Integer page, IPTableQuery query){
		PagerRS<IPTable> rs = (PagerRS<IPTable>) IPTable.findByPager(getQuery(query, IPTableQuery.class), getPageNo(page), getPageSize());
		render(rs);
	}
	
	public static void add(){
		render();
	}
	
	public static void create(@Valid IPTable iptables){
		if (validation.hasErrors()) {
			render("@add", iptables);
		} else {
			iptables.operator = StringUtil.strToLong(SecurityUtil.getLoginUserId());
			iptables.save();
			saveUserLogAndNotice("ip["+iptables.ip+"]添加成功", true);
			index(null, null);
		}
	}
	
	public static void edit(long id){
		IPTable iptables = IPTable.findById(id);
		render(iptables);
	}
	
	public static void update(long id){
		IPTable iptables = IPTable.findById(id);
		iptables.edit(ParamNode.convert(params.all()), "iptables");
		validation.valid(iptables);
		if (validation.hasErrors()) {
			render("@edit", iptables);
		} else {
			iptables.updatedAt = new Date();
			iptables.operator = StringUtil.strToLong(SecurityUtil.getLoginUserId());
			iptables.save();
			saveUserLogAndNotice("ip["+iptables.ip+"]修改成功", true);
			index(null, null);
		}
	}
	
	public static void show(long id){
		IPTable iptables = IPTable.findById(id);
		render(iptables);
	}
	
	public static void destory(long id){
		IPTable iptables = IPTable.findById(id);
		String ip = iptables.ip;
		iptables.destory();
		logger.info(String.format("[%s]: ip<%s>, operator<%s>, operateAt<%s>", "destory", ip, SecurityUtil.getLoginUserId(), new Date()));
		saveUserLogAndNotice("ip["+ip+"]删除成功", true);
		index(null, null);
	}
	
	@After(only={"em.IPTables.create", "em.IPTables.update", "em.IPTables.destory"})
	public static void lifeCycle(){
		DicUtil.loadByDicName(SecurityConstant.DIC_IP_LIST_NAME);
	}

}

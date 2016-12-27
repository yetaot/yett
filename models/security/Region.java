package models.security;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import framework.base.BaseModel;

/**
 * 区域表
 * @author jinchaoyang
 *
 */

@Entity
@Table(name="T_REGION")
public class Region extends BaseModel{
	//区域编号
	public String code;
	//区域名称
	public String name;
	//邮编
	public String zipCode;
	//区域类型 0:省 1:市 2:县
	public int rType;
	//父节点编号
	public String parent;
	//区号
	public String areaCode;

	public Region(){
		
	}
	
	public final static int PROVINCE_TYPE=0;
	public final static int CITY_TYPE=1;
	public final static int COUNTRY_TYPE=2;
	
	public Region(String code ,String name, int rType){
		this.code = code;
		this.name = name;
		this.rType = rType;
	}
	/**
	 * 根据地区类型查询相关地区信息
	 * @param rType
	 * @return
	 */
	public static List<Region> queryByType(int rType){
		String sql="from Region a where a.rType=? order by a.code";
		return Region.find(sql, rType).fetch();
	}
	/**
	 * 根据父级编号获取子级信息
	 * @param parent
	 * @return
	 */
	public static List<Region> queryByParent(String parent){
		String sql="from Region a where a.parent=? order by a.code";
		return Region.find(sql, parent).fetch();
	}
	
	
}

package framework.security.store;

import java.util.List;

import java.util.Map;

import framework.security.models.SecurityMenu;


public interface ISecurityStore {
	public List<String> getAllKeys(String key);
	public Map<String,String> getAllByKeys(String key);
	/**
	 * 保存权限信息
	 * @param sources
	 * @return
	 */
	public void storeResource(Map<SecurityMenu,List<SecurityMenu>> resources);
	/**
	 * 清空资源
	 */
	public void clearResource();
	
}

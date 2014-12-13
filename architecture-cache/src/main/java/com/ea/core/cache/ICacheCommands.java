package com.ea.core.cache;

import java.util.Set;

/**
 * 缓存客户端命令的封装
 * 
 * @author yiyongfei
 *
 */
public interface ICacheCommands {

	public Boolean add(String key, String value, int seconds) throws Exception;
	
	public Boolean set(String key, String value, int seconds) throws Exception;
	
	public Boolean replace(String key, String value, int seconds) throws Exception;
	
	public Boolean expire(String key, int seconds) throws Exception;
	
	public Set<String> keys(final String pattern) throws Exception;
	
	public String get(String key) throws Exception;
	
	public Boolean exists(String key) throws Exception;
	
	public Boolean delete(String key) throws Exception;

	public Boolean append(String key, String value) throws Exception;
    
    public void shutdown() throws Exception;
    
    public Object getCommands();
}

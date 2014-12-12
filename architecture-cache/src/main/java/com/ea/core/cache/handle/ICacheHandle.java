package com.ea.core.cache.handle;

import java.util.Map;
import java.util.Set;

public interface ICacheHandle {
	
	public void set(String cacheLevel, Map<String, String> map, int seconds) throws Exception;
	
	public boolean set(String cacheLevel, String key, String value, int seconds) throws Exception;
	
	public void add(String cacheLevel, Map<String, String> map, int seconds) throws Exception;
	
	public boolean add(String cacheLevel, String key, String value, int seconds) throws Exception;
	
	public void replace(String cacheLevel, Map<String, String> map, int seconds) throws Exception;
	
	public boolean replace(String cacheLevel, String key, String value, int seconds) throws Exception;
	
	public Set<String> keys(String pattern, String regexp) throws Exception;
	
	public Boolean expire(String key, int seconds) throws Exception;
	
	public String get(String key) throws Exception;
	
	public Boolean exists(String key) throws Exception;
	
	public Map<String, String> getByRegexp(String pattern, String regexp) throws Exception;
	
	public String showRedisByRegexp(String pattern, String regexp) throws Exception;
	
	public Boolean delete(String key) throws Exception;
	
	public int deleteByRegexp(String pattern, String regexp) throws Exception;
	
	public boolean isActivate();
}

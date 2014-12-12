package com.ea.core.cache.memcached.config;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.JedisPoolConfig;

import com.ea.core.cache.IPoolConfig;

public class MemcachedPoolConfig implements IPoolConfig {
	private Map<String, Object> config;
	public MemcachedPoolConfig(){
		config = new HashMap<String, Object>();
	}
	
	@Override
	public void setMaxTotal(int maxTotal) {
		// TODO Auto-generated method stub
		config.put("MaxTotal", maxTotal);
	}

	@Override
	public int getMaxTotal() {
		// TODO Auto-generated method stub
		return (Integer) config.get("MaxTotal");
	}

	@Override
	public void setMaxIdle(int maxIdle) {
	}

	@Override
	public int getMaxIdle() {
		return 0;
	}

	@Override
	public void setMaxConnectMillis(long maxConnectMillis) {
		// TODO Auto-generated method stub
		config.put("MaxConnectMillis", maxConnectMillis);
	}

	@Override
	public long getMaxConnectMillis() {
		// TODO Auto-generated method stub
		return (Long) config.get("MaxConnectMillis");
	}

	@Override
	public void setMaxWaitMillis(long maxWaitMillis) {
		// TODO Auto-generated method stub
		config.put("MaxWaitMillis", maxWaitMillis);
	}

	@Override
	public long getMaxWaitMillis() {
		// TODO Auto-generated method stub
		return (Long) config.get("MaxWaitMillis");
	}

	@Override
	public void setTestOnBorrow(boolean testOnBorrow) {
	}

	@Override
	public boolean getTestOnBorrow() {
		return false;
	}

	@Override
	public void setTestOnReturn(boolean testOnReturn) {
	}

	@Override
	public boolean getTestOnReturn() {
		return false;
	}

	@Override
	public void setLifo(boolean lifo) {
	}

	@Override
	public boolean getLifo() {
		return false;
	}

	@Override
	public void setEnableHealSession(boolean enableHealSession) {
		// TODO Auto-generated method stub
		config.put("EnableHealSession", enableHealSession);
	}

	@Override
	public boolean getEnableHealSession() {
		// TODO Auto-generated method stub
		return (Boolean) config.get("EnableHealSession");
	}

	@Override
	public void setHealSessionInterval(long healSessionInterval) {
		// TODO Auto-generated method stub
		config.put("HealSessionInterval", healSessionInterval);
	}

	@Override
	public long getHealSessionInterval() {
		// TODO Auto-generated method stub
		return (Long) config.get("HealSessionInterval");
	}

	@Override
	public void setFailureMode(boolean failureMode) {
		// TODO Auto-generated method stub
		config.put("FailureMode", failureMode);
	}

	@Override
	public boolean getFailureMode() {
		// TODO Auto-generated method stub
		return (Boolean) config.get("FailureMode");
	}

	@Override
	public Object getConfig() {
		// TODO Auto-generated method stub
		return config;
	}

}

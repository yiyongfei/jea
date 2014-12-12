package com.ea.core.cache.redis.config;

import redis.clients.jedis.JedisPoolConfig;

import com.ea.core.cache.IPoolConfig;

public class RedisPoolConfig implements IPoolConfig {
	private JedisPoolConfig config;
	
	public RedisPoolConfig(){
		config = new JedisPoolConfig();
	}
	
	public void setMaxTotal(int maxTotal){
		config.setMaxTotal(maxTotal);
	}
	public int getMaxTotal(){
		return config.getMaxTotal();
	}
	
	public void setMaxIdle(int maxIdle){
		config.setMaxIdle(maxIdle);
	}
	public int getMaxIdle(){
		return config.getMaxIdle();
	}
	
	public void setMaxConnectMillis(long maxConnectMillis){
		
	}
	public long getMaxConnectMillis(){
		return 0L;
	}
	
	public void setMaxWaitMillis(long maxWaitMillis){
		config.setMaxWaitMillis(maxWaitMillis);
	}
	public long getMaxWaitMillis(){
		return config.getMaxWaitMillis();
	}
	
	public void setTestOnBorrow(boolean testOnBorrow){
		config.setTestOnBorrow(testOnBorrow);
	}
	public boolean getTestOnBorrow(){
		return config.getTestOnBorrow();
	}
	
	public void setTestOnReturn(boolean testOnReturn){
		config.setTestOnReturn(testOnReturn);
	}
	public boolean getTestOnReturn(){
		return config.getTestOnReturn();
	}
	
	public void setLifo(boolean lifo) {
		config.setLifo(lifo);
	}
	public boolean getLifo() {
		return config.getLifo();
	}
	
	public Object getConfig(){
		return config;
	}

	public void setEnableHealSession(boolean enableHealSession) {
	}

	public boolean getEnableHealSession() {
		return false;
	}

	public void setHealSessionInterval(long healSessionInterval) {
	}

	public long getHealSessionInterval() {
		return 0;
	}

	public void setFailureMode(boolean failureMode) {
	}

	public boolean getFailureMode() {
		return false;
	}
}

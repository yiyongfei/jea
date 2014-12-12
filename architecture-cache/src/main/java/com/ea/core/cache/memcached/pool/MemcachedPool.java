/**
 * Copyright 2014 伊永飞
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ea.core.cache.memcached.pool;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.ea.core.cache.ICacheCommands;
import com.ea.core.cache.ICachePool;
import com.ea.core.cache.IPoolConfig;
import com.ea.core.cache.memcached.commands.MemcachedCommands;
import com.ea.core.cache.memcached.config.MemcachedPoolConfig;

public class MemcachedPool implements ICachePool {
	private MemcachedClientBuilder builder;
	private IPoolConfig poolConfig;
	private List<String> addresses;
	private List<InetSocketAddress> socketAddress;
	
	public MemcachedPool(){
		addresses = new ArrayList<String>();
		poolConfig = new MemcachedPoolConfig();
		poolConfig.setMaxTotal(50);
		poolConfig.setMaxConnectMillis(5000);
		poolConfig.setMaxWaitMillis(3000);
		poolConfig.setEnableHealSession(true);
		poolConfig.setHealSessionInterval(2000);
		poolConfig.setFailureMode(false);
	}
	
	public void setMaxTotal(int maxTotal){
		poolConfig.setMaxTotal(maxTotal);
	}
	
	public void setMaxIdle(int maxIdle){
	}
	
	public void setMaxWaitMillis(long maxWaitMillis){
		poolConfig.setMaxWaitMillis(maxWaitMillis);
	}
	
	public void setTestOnBorrow(boolean testOnBorrow){
	}
	
	public void setTestOnReturn(boolean testOnReturn){
	}
	
	public void setLifo(boolean lifo) {
	}
	
	public void addRedisServer(String host, int port){
		String address = host + ":" + port;
		if(!addresses.contains(address)){
			addresses.add(address);
		}
	}
	public void addRedisServer(String address){
		if(!addresses.contains(address)){
			addresses.add(address);
		}
	}
	
	public ICacheCommands getResource() throws Exception {
		if(builder == null){
			initPool();
		}
		MemcachedClient client = builder.build();
		return new MemcachedCommands(client, socketAddress);
	}
	
	public void returnResource(ICacheCommands commands) throws Exception {
		if(builder == null){
			throw new RuntimeException("释放资源前请先获取资源");
		}
		commands.shutdown();
	}
	
	private void initPool(){
		StringBuffer memcachedAddr = new StringBuffer();
		for(String addr : addresses){
			memcachedAddr.append(addr).append(" ");
		}
		socketAddress = AddrUtil.getAddresses (memcachedAddr.toString().trim());
		builder = new XMemcachedClientBuilder(socketAddress);
		builder.setConnectionPoolSize(poolConfig.getMaxTotal());
		builder.setConnectTimeout(poolConfig.getMaxConnectMillis());
		builder.setFailureMode(poolConfig.getFailureMode());
		builder.setOpTimeout(poolConfig.getMaxWaitMillis());
		builder.setEnableHealSession(poolConfig.getEnableHealSession());
		builder.setHealSessionInterval(poolConfig.getHealSessionInterval());
		/**
		 * 分布策略
		 * 默认分布的策略是按照key的哈希值模以连接数得到的余数
		 * KetamaMemcachedSessionLocator：一致性哈希（consistent hash)
		 * ElectionMemcachedSessionLocator：选举散列哈希算法
		 */
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
	}

	@Override
	public void setMaxConnectMillis(long maxConnectMillis) {
		// TODO Auto-generated method stub
		poolConfig.setMaxConnectMillis(maxConnectMillis);
	}

	@Override
	public void setEnableHealSession(boolean enableHealSession) {
		// TODO Auto-generated method stub
		poolConfig.setEnableHealSession(enableHealSession);
	}

	@Override
	public void setHealSessionInterval(long healSessionInterval) {
		// TODO Auto-generated method stub
		poolConfig.setHealSessionInterval(healSessionInterval);
	}

	@Override
	public void setFailureMode(boolean failureMode) {
		// TODO Auto-generated method stub
		poolConfig.setFailureMode(failureMode);
	}
}

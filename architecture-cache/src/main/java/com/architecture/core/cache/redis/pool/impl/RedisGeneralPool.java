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
package com.architecture.core.cache.redis.pool.impl;

import com.architecture.core.cache.redis.pool.RedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisGeneralPool implements RedisPool {
	private JedisPool jedisPool;
	private JedisPoolConfig poolConfig;
	private String host;
	private int port;
	
	public RedisGeneralPool(){
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(50);
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxWaitMillis(500);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
	}
	
	public void setMaxTotal(int maxTotal){
		poolConfig.setMaxTotal(maxTotal);
	}
	
	public void setMaxIdle(int maxIdle){
		poolConfig.setMaxIdle(maxIdle);
	}
	
	public void setMaxWaitMillis(long maxWaitMillis){
		poolConfig.setMaxWaitMillis(maxWaitMillis);
	}
	
	public void setTestOnBorrow(boolean testOnBorrow){
		poolConfig.setTestOnBorrow(testOnBorrow);
	}
	
	public void setTestOnReturn(boolean testOnReturn){
		poolConfig.setTestOnReturn(testOnReturn);
	}
	
	public void addRedisServer(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public JedisCommands getResource(){
		if(jedisPool == null){
			initPool();
		}
		return jedisPool.getResource();
	}
	
	public void returnResource(JedisCommands jedis){
		if(jedisPool == null){
			throw new RuntimeException("释放资源前请先获取资源");
		}
		jedisPool.returnResource((Jedis)jedis);
	}
	
	private void initPool(){
		jedisPool = new JedisPool(poolConfig, host, port);
	}
}

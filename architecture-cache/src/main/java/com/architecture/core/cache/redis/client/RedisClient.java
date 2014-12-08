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
package com.architecture.core.cache.redis.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.architecture.core.cache.redis.pool.RedisPool;
import com.architecture.core.cache.redis.pool.factory.RedisPoolFactory;
import com.ea.core.base.utils.RegexUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

public class RedisClient {
	private RedisPool pool;
	
	public RedisClient(String host, int port){
		pool=RedisPoolFactory.getInstance(RedisPoolFactory.GENERAL);
		pool.addRedisServer(host, port);
	}
	
	public RedisClient(String host, int port, String factoryType){
		pool = RedisPoolFactory.getInstance(factoryType);
		pool.addRedisServer(host, port);
	}
	
	public void set(Map<String, String> map){
		JedisCommands jedisCommands = pool.getResource();
		Iterator<String> it = map.keySet().iterator();
		String key;
		try{
			while(it.hasNext()){
				key = it.next();
				jedisCommands.set(key, map.get(key));
			}
		} finally {
			pool.returnResource(jedisCommands);
		}

	}
	
	public boolean set(String key, String value){
		JedisCommands jedisCommands = pool.getResource();
		try{
			jedisCommands.set(key, value);
		} finally {
			pool.returnResource(jedisCommands);
		}
		return true;
	}
	
	public Set<String> keys(){
		return keys("*", null);
	}
	
	public Set<String> keys(String pattern){
		return keys(pattern, null);
	}
	
	public Set<String> keys(String pattern, String regexp){
		JedisCommands jedisCommands = pool.getResource();
		Set<String> set = new HashSet<String>();
		try{
			if(jedisCommands instanceof Jedis){
				Jedis jedis = (Jedis)jedisCommands;
				set.addAll(RegexUtil.matcher(regexp, jedis.keys(pattern)));
			} else {
				ShardedJedis jedis = (ShardedJedis)jedisCommands;
				
			}
			return set;
		} finally {
			pool.returnResource(jedisCommands);
		}
	}
	
	public String get(String key){
		JedisCommands jedisCommands = pool.getResource();
		try{
			return jedisCommands.get(key);
		} finally {
			pool.returnResource(jedisCommands);
		}
	}
	
	public Map<String, String> getByPattern(String pattern){
		return getByRegexp(pattern, null);
	}
	
	public Map<String, String> getByRegexp(String pattern, String regexp){
		Set<String> keySet = keys(pattern, regexp);
		Map<String, String> mapVal = new HashMap<String, String>();
		if(keySet != null && !keySet.isEmpty()){
			JedisCommands jedisCommands = pool.getResource();
			Iterator<String> it = keySet.iterator();
			try{
				String key = null;
				while(it.hasNext()){
					key = it.next();
					mapVal.put(key, jedisCommands.get(key));
				}
			} finally {
				pool.returnResource(jedisCommands);
			}
		}
		return mapVal;
	}
	
	public String showRedisByPattern(String pattern){
		return showRedisByRegexp(pattern, null);
	}
	
	public String showRedisByRegexp(String pattern, String regexp){
		Map<String, String> mapVal = getByRegexp(pattern, regexp);
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = mapVal.keySet().iterator();
		String key = null;
		while(it.hasNext()){
			key = it.next();
			sb.append(key + "======" + mapVal.get(key) + "\n");
		}
		return sb.toString();
	}
	
	public void clean(String key){
		JedisCommands jedisCommands = pool.getResource();
		try{
			jedisCommands.del(key);
		} finally {
			pool.returnResource(jedisCommands);
		}
	}
	
	public int cleanByPattern(String pattern){
		return cleanByRegexp(pattern, null);
	}
	
	public int cleanByRegexp(String pattern, String regexp){
		Set<String> keySet = keys(pattern, regexp);
		if(keySet != null && !keySet.isEmpty()){
			JedisCommands jedisCommands = pool.getResource();
			Iterator<String> it = keySet.iterator();
			try{
				while(it.hasNext()){
					jedisCommands.del(it.next());
				}
			} finally {
				pool.returnResource(jedisCommands);
			}
		}
		return keySet.size();
	}
}

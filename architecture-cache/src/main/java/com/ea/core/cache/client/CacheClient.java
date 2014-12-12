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
package com.ea.core.cache.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ea.core.base.utils.RegexUtil;
import com.ea.core.cache.ICacheCommands;
import com.ea.core.cache.ICachePool;

public class CacheClient {
	private ICachePool pool;
	
	public CacheClient(ICachePool cachePool) {
		this.pool = cachePool;
	}

	public void set(Map<String, String> map, int seconds) throws Exception{
		ICacheCommands commands = pool.getResource();
		Iterator<String> it = map.keySet().iterator();
		String key;
		try{
			while(it.hasNext()){
				key = it.next();
				commands.set(key, map.get(key), seconds);
			}
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public boolean set(String key, String value, int seconds) throws Exception{
		ICacheCommands commands = pool.getResource();
		try{
			return commands.set(key, value, seconds);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public void add(Map<String, String> map, int seconds) throws Exception{
		ICacheCommands commands = pool.getResource();
		Iterator<String> it = map.keySet().iterator();
		String key;
		try{
			while(it.hasNext()){
				key = it.next();
				commands.add(key, map.get(key), seconds);
			}
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public boolean add(String key, String value, int seconds) throws Exception{
		ICacheCommands commands = pool.getResource();
		try{
			return commands.add(key, value, seconds);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public void replace(Map<String, String> map, int seconds) throws Exception{
		ICacheCommands commands = pool.getResource();
		Iterator<String> it = map.keySet().iterator();
		String key;
		try{
			while(it.hasNext()){
				key = it.next();
				commands.replace(key, map.get(key), seconds);
			}
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public boolean replace(String key, String value, int seconds) throws Exception{
		ICacheCommands commands = pool.getResource();
		try{
			return commands.replace(key, value, seconds);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public Set<String> keys(String pattern, String regexp) throws Exception{
		ICacheCommands commands = pool.getResource();
		try{
			return (RegexUtil.matcher(regexp, commands.keys(pattern)));
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public Boolean expire(String key, int seconds) throws Exception {
		ICacheCommands commands = pool.getResource();
		try{
			return commands.expire(key, seconds);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public String get(String key) throws Exception {
		ICacheCommands commands = pool.getResource();
		try{
			return commands.get(key);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public Boolean exists(String key) throws Exception {
		ICacheCommands commands = pool.getResource();
		try{
			return commands.exists(key);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public Map<String, String> getByRegexp(String pattern, String regexp) throws Exception{
		Set<String> keySet = keys(pattern, regexp);
		Map<String, String> mapVal = new HashMap<String, String>();
		if(keySet != null && !keySet.isEmpty()){
			ICacheCommands commands = pool.getResource();
			Iterator<String> it = keySet.iterator();
			try{
				String key = null;
				while(it.hasNext()){
					key = it.next();
					mapVal.put(key, commands.get(key));
				}
			} finally {
				pool.returnResource(commands);
			}
		}
		return mapVal;
	}
	
	public String showRedisByRegexp(String pattern, String regexp) throws Exception{
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
	
	public Boolean delete(String key) throws Exception{
		ICacheCommands commands = pool.getResource();
		try{
			return commands.delete(key);
		} finally {
			pool.returnResource(commands);
		}
	}
	
	public int deleteByRegexp(String pattern, String regexp) throws Exception{
		int count = 0;
		Set<String> keySet = keys(pattern, regexp);
		if(keySet != null && !keySet.isEmpty()){
			ICacheCommands commands = pool.getResource();
			Iterator<String> it = keySet.iterator();
			try{
				while(it.hasNext()){
					Boolean result = commands.delete(it.next());
					if(result){
						count++;
					}
				}
			} finally {
				pool.returnResource(commands);
			}
		}
		return count;
	}
}

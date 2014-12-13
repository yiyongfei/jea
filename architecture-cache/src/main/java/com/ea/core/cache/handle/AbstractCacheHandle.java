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
package com.ea.core.cache.handle;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ea.core.cache.CacheConstants;
import com.ea.core.cache.CacheDefinition;
import com.ea.core.cache.ICachePool;
import com.ea.core.cache.client.CacheClient;
import com.ea.core.cache.memcached.pool.MemcachedPool;
import com.ea.core.cache.redis.pool.impl.RedisGeneralPool;
import com.ea.core.cache.redis.pool.impl.RedisShardedPool;

public abstract class AbstractCacheHandle implements ICacheHandle, ApplicationContextAware {
	protected ApplicationContext context = null;
	
	private CacheClient client;
	private ICacheHandle nextHandle;
	private String level;
	private boolean activate;
	
	public AbstractCacheHandle(String cacheLevel) {
		this.level = cacheLevel;
		this.activate = Boolean.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "activate"));
		if (activate) {
			ICachePool cachePool = null;
			String cacheType = CacheDefinition.getPropertyValue(cacheLevel + "type");
			if(CacheConstants.CACHE_TYPE.MEMCACHED.getCode().equals(cacheType)){
				cachePool = new MemcachedPool();
			} else if (CacheConstants.CACHE_TYPE.REDIS.getCode().equals(cacheType)){
				String servers = CacheDefinition.getPropertyValue(cacheLevel + "servers");
				if(servers.split(" ").length > 1){
					cachePool = new RedisShardedPool();
				} else {
					cachePool = new RedisGeneralPool();
				}
			} else {
				throw new RuntimeException("配置的缓存类型不正确，请配置为MEMCACHED或REDIS");
			}
			cachePool.addRedisServer(CacheDefinition.getPropertyValue(cacheLevel + "servers"));
			cachePool.setMaxTotal(Integer.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "maxTotal")));
			cachePool.setMaxIdle(Integer.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "maxIdle")));
			cachePool.setMaxConnectMillis(Integer.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "maxConnectMillis")));
			cachePool.setMaxWaitMillis(Integer.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "maxWaitMillis")));
			cachePool.setEnableHealSession(Boolean.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "enableHealSession")));
			cachePool.setHealSessionInterval(Integer.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "healSessionInterval")));
			cachePool.setFailureMode(Boolean.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "failureMode")));
			cachePool.setTestOnBorrow(Boolean.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "testOnBorrow")));
			cachePool.setTestOnReturn(Boolean.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "testOnReturn")));
			cachePool.setLifo(Boolean.valueOf(CacheDefinition.getPropertyValue(cacheLevel + "lifo")));
			
			this.client = new CacheClient(cachePool);
		}		
	}
	
	/**
	 * 当Key存在时，会替换，当Key不存在时，会新增
	 * 当指定级别缓存器和当前缓存器级别一致时，当前缓存器设置缓存数据
	 * 当前缓存器设置完后，如果当前缓存器下还存在下个级别的缓存器，将数据交由下个级别的缓存器缓存
	 */
	public void set(String cacheLevel, Map<String, String> map, int seconds) throws Exception{
		if(cacheLevel == null || this.level.equals(cacheLevel)){
			//set缓存时，如果明确了数据欲缓存的级别，那只有该级别和该级别下的缓存服务器才会缓存
			//例：数据要缓存在L2上，此时L1将不缓存，而L2和后续级别将缓存该数据
			if(this.isActivate()){
				client.set(map, seconds);
			}
			cacheLevel = null;
		}
		this.setNextHandle();
		if(this.nextHandle != null){
			this.nextHandle.set(cacheLevel, map, seconds);
		}
	}
	
	public boolean set(String cacheLevel, String key, String value, int seconds) throws Exception{
		boolean result = false;
		if(cacheLevel == null || this.level.equals(cacheLevel)){
			//set缓存时，如果明确了数据欲缓存的级别，那只有该级别和该级别下的缓存服务器才会缓存
			//例：数据要缓存在L2上，此时L1将不缓存，而L2和后续级别将缓存该数据
			if(this.isActivate()){
				result = client.set(key, value, seconds);
			}
			cacheLevel = null;
		}
		
		this.setNextHandle();
		if(this.nextHandle != null){
			if(this.isActivate()){
				this.nextHandle.set(cacheLevel, key, value, seconds);
			} else {
				result = this.nextHandle.set(cacheLevel, key, value, seconds);
			}
		}
		return result;
	}
	
	/**
	 * 当Key存在时，失败，当Key不存在时，会新增
	 * 当指定级别缓存器和当前缓存器级别一致时，当前缓存器设置缓存数据
	 * 当前缓存器设置完后，如果当前缓存器下还存在下个级别的缓存器，将数据交由下个级别的缓存器缓存
	 */
	public void add(String cacheLevel, Map<String, String> map, int seconds) throws Exception{
		if(cacheLevel == null || this.level.equals(cacheLevel)){
			if(this.isActivate()){
				client.add(map, seconds);
			}
			cacheLevel = null;
		}
		this.setNextHandle();
		if(this.nextHandle != null){
			this.nextHandle.add(cacheLevel, map, seconds);
		}
	}
	
	public boolean add(String cacheLevel, String key, String value, int seconds) throws Exception{
		boolean result = false;
		if(cacheLevel == null || this.level.equals(cacheLevel)){
			if(this.isActivate()){
				result = client.add(key, value, seconds);
			}
			cacheLevel = null;
		}
		
		this.setNextHandle();
		if(this.nextHandle != null){
			if(this.isActivate()){
				this.nextHandle.add(cacheLevel, key, value, seconds);
			} else {
				result = this.nextHandle.add(cacheLevel, key, value, seconds);
			}
		}
		return result;
	}
	
	/**
	 * 当Key存在时，会替换，当Key不存在时，会失败
	 * 当指定级别缓存器和当前缓存器级别一致时，当前缓存器设置缓存数据
	 * 当前缓存器设置完后，如果当前缓存器下还存在下个级别的缓存器，将数据交由下个级别的缓存器缓存
	 */
	public void replace(String cacheLevel, Map<String, String> map, int seconds) throws Exception{
		if(cacheLevel == null || this.level.equals(cacheLevel)){
			if(this.isActivate()){
				client.replace(map, seconds);
			}
			cacheLevel = null;
		}
		this.setNextHandle();
		if(this.nextHandle != null){
			this.nextHandle.replace(cacheLevel, map, seconds);
		}
	}
	
	public boolean replace(String cacheLevel, String key, String value, int seconds) throws Exception{
		boolean result = false;
		if(cacheLevel == null || this.level.equals(cacheLevel)){
			if(this.isActivate()){
				result = client.replace(key, value, seconds);
			}
			cacheLevel = null;
		}
		
		this.setNextHandle();
		if(this.nextHandle != null){
			if(this.isActivate()){
				this.nextHandle.replace(cacheLevel, key, value, seconds);
			} else {
				result = this.nextHandle.replace(cacheLevel, key, value, seconds);
			}
		}
		return result;
	}
	
	/**
	 * 当前缓存器设置了Key，返回当前缓存器的Key内容，如果当前缓存器未设置Key，交给下一缓存器去获取
	 */
	public Set<String> keys(String pattern, String regexp) throws Exception{
		if(this.isActivate()){
			Set<String> set = client.keys(pattern, regexp);
			if(set != null) {
				return set;
			}
		}
		
		this.setNextHandle();
		if (this.nextHandle != null) {
			return this.nextHandle.keys(pattern, regexp);
		} else {
			return null;
		}
	}
	
	/**
	 * 设置失效时间
	 * 所有缓存级别只要存在该Key，都会设置失败时间
	 * 考虑到Key的分布可能不会在所有缓存器里都存在，所以只要有一个缓存器设置成功，则返回成功
	 * 
	 */
	public Boolean expire(String key, int seconds) throws Exception {
		boolean result = false;
		if(this.isActivate()){
			result = client.expire(key, seconds);
		}
		this.setNextHandle();
		if(this.nextHandle != null){
			if(this.isActivate() && result){
				this.nextHandle.expire(key, seconds);
			} else {
				result = this.nextHandle.expire(key, seconds);
			}
		}
		return result;
	}
	
	/**
	 * 当前缓存器如果激活，则根据Key获取Value，获到后直接返回，如果获不到，交由下一缓存器获取
	 */
	public String get(String key) throws Exception{
		if(this.isActivate()){
			String value = client.get(key);
			if(value != null) {
				return value;
			}
		}
		this.setNextHandle();
		if (this.nextHandle != null) {
			return this.nextHandle.get(key);
		} else {
			return null;
		}
	}
	
	/**
	 * 当前缓存器如果存在Key，返回True，否则交给下一缓存器判断
	 */
	public Boolean exists(String key) throws Exception {
		if(this.isActivate()){
			Boolean value = client.exists(key);
			if(value != null && value) {
				return value;
			}
		}
		this.setNextHandle();
		if (this.nextHandle != null) {
			return this.nextHandle.exists(key);
		} else {
			return new Boolean(false);
		}
	}
	
	public Map<String, String> getByRegexp(String pattern, String regexp) throws Exception{
		if(this.isActivate()){
			Map<String, String> map = client.getByRegexp(pattern, regexp);
			if(map != null && !map.isEmpty()) {
				return map;
			} 
		}
		this.setNextHandle();
		if (this.nextHandle != null) {
			return this.nextHandle.getByRegexp(pattern, regexp);
		} else {
			return null;
		}
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
		boolean result = false;
		if(this.isActivate()){
			result = client.delete(key);
		}
		this.setNextHandle();
		if(this.nextHandle != null){
			if(this.isActivate() && result){
				this.nextHandle.delete(key);
			} else {
				result = this.nextHandle.delete(key);
			}
		}
		return result;
	}
	
	public int deleteByRegexp(String pattern, String regexp) throws Exception{
		int count = 0;
		if(this.isActivate()){
			count = client.deleteByRegexp(pattern, regexp);
		}
		this.setNextHandle();
		if(this.nextHandle != null){
			if(this.isActivate() && count > 0){
				this.nextHandle.deleteByRegexp(pattern, regexp);
			} else {
				count = this.nextHandle.deleteByRegexp(pattern, regexp);
			}
		}
		return count;
	}
	
	@Override
	public boolean isActivate() {
		// TODO Auto-generated method stub
		return activate;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		// TODO Auto-generated method stub
		context = applicationContext;
	}
	
	protected abstract void setNextHandle();
	
	protected void setNextHandle(ICacheHandle nextHandle){
		this.nextHandle = nextHandle;
	}
}

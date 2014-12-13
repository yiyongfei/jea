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
package com.ea.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ea.core.cache.handle.ICacheHandle;
import com.ea.core.cache.request.Request;
import com.ea.core.kryo.ISerializer;
import com.ea.core.kryo.impl.DefaultSerializer;
import com.ea.core.kryo.impl.JavaSerializer;
import com.ea.core.kryo.impl.ObjectSerializer;

/**
 * 缓存上下文
 * 
 * @author yiyongfei
 *
 */
@Component
public class CacheContext {
	@Autowired
	private ICacheHandle l1CacheHandle;
	
	public CacheContext(){
		
	}

	/**
	 * 设置缓存数据，如果Key存在，则会覆盖Value
	 * 所有Level的缓存器都会缓存数据
	 * 
	 * @param map
	 * @param seconds 失效时间，秒
	 * @throws Exception
	 */
	public void set(Map<String, Object> map, int seconds) throws Exception{
		set(null, map, seconds);
	}
	/**
	 * 设置缓存数据，如果Key存在，则会覆盖Value
	 * 指定Level及后续的缓存器会缓存数据
	 * 
	 * @param cacheLevel 缓存级别，如果设置成L2，表明L2及后续的缓存器将缓存数据，但L1不缓存
	 * @param map
	 * @param seconds 失效时间，秒
	 * @throws Exception
	 */
	public void set(String cacheLevel, Map<String, Object> map, int seconds) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		Set<String> keys = map.keySet();
		for(String key : keys){
			result.put(key, this.serialize(map.get(key)));
		}
		l1CacheHandle.set(cacheLevel, result, seconds);
	}
	
	public boolean set(String key, Object value, int seconds) throws Exception{
		return set(null, key, value, seconds);
	}
	public boolean set(String cacheLevel, String key, Object value, int seconds) throws Exception{
		return l1CacheHandle.set(cacheLevel, key, this.serialize(value), seconds);
	}
	
	/**
	 * 设置缓存数据，如果Key存在，则缓存不成功
	 * 
	 * @param map
	 * @param seconds 失效时间，秒
	 * @throws Exception
	 */
	public void add(Map<String, Object> map, int seconds) throws Exception{
		add(null, map, seconds);
	}
	public void add(String cacheLevel, Map<String, Object> map, int seconds) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		Set<String> keys = map.keySet();
		for(String key : keys){
			result.put(key, this.serialize(map.get(key)));
		}
		l1CacheHandle.add(cacheLevel, result, seconds);
	}
	
	public boolean add(String key, Object value, int seconds) throws Exception{
		return add(null, key, value, seconds);
	}
	public boolean add(String cacheLevel, String key, Object value, int seconds) throws Exception{
		return l1CacheHandle.add(cacheLevel, key, this.serialize(value), seconds);
	}
	
	/**
	 * 设置缓存数据，如果Key不存在，则缓存不成功
	 * 
	 * @param map
	 * @param seconds 失效时间，秒
	 * @throws Exception
	 */
	public void replace(Map<String, Object> map, int seconds) throws Exception{
		replace(null, map, seconds);
	}
	public void replace(String cacheLevel, Map<String, Object> map, int seconds) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		Set<String> keys = map.keySet();
		for(String key : keys){
			result.put(key, this.serialize(map.get(key)));
		}
		l1CacheHandle.replace(cacheLevel, result, seconds);
	}
	
	public boolean replace(String key, Object value, int seconds) throws Exception{
		return replace(null, key, value, seconds);
	}
	public boolean replace(String cacheLevel, String key, Object value, int seconds) throws Exception{
		return l1CacheHandle.replace(cacheLevel, key, this.serialize(value), seconds);
	}
	
	/**
	 * 获得缓存服务器所有指定表达式的Key
	 * 
	 * @param pattern
	 * @param regexp
	 * @return
	 * @throws Exception
	 */
	public Set<String> keys(String pattern, String regexp) throws Exception{
		return l1CacheHandle.keys(pattern, regexp);
	}
	
	/**
	 * 设置失效时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	public Boolean expire(String key, int seconds) throws Exception {
		return l1CacheHandle.expire(key, seconds);
	}
	
	/**
	 * 根据Key获得缓存数据
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object get(String key) throws Exception{
		return this.deserialize(l1CacheHandle.get(key));
	}
	
	/**
	 * 判断Key是否存在
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Boolean exists(String key) throws Exception  {
		return l1CacheHandle.exists(key);
	}
	
	/**
	 * 根据正则表达式获得符合条件的Key和缓存数据
	 * 
	 * @param pattern
	 * @param regexp
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getByRegexp(String pattern, String regexp) throws Exception{
		Map<String, String> map = l1CacheHandle.getByRegexp(pattern, regexp);
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> keys = map.keySet();
		for(String key : keys){
			result.put(key, this.deserialize(map.get(key)));
		}
		return result;
	}
	
	/**
	 * 没有实际意义
	 * 
	 * @param pattern
	 * @param regexp
	 * @return
	 * @throws Exception
	 */
	public String showRedisByRegexp(String pattern, String regexp) throws Exception{
		return l1CacheHandle.showRedisByRegexp(pattern, regexp);
	}
	
	/**
	 * 根据Key删除缓存数据
	 * 
	 * @param key
	 * @throws Exception
	 */
	public void delete(String key) throws Exception{
		l1CacheHandle.delete(key);
	}
	
	/**
	 * 根据正则表达式删除符合条件的缓存数据
	 * 
	 * @param pattern
	 * @param regexp
	 * @return
	 * @throws Exception
	 */
	public int deleteByRegexp(String pattern, String regexp) throws Exception{
		return l1CacheHandle.deleteByRegexp(pattern, regexp);
	}
	
	protected String serialize(Object model) throws Exception{
		Request request = new Request();
		
		ISerializer contentSerializer = new ObjectSerializer();
		request.setContent(contentSerializer.serialize(model));
		request.setSerializer(new JavaSerializer().serialize(contentSerializer));

		ISerializer requestSerializer = new DefaultSerializer();
		requestSerializer.register(request.getClass());
		return requestSerializer.serialize(request);
	}
	
	protected Object deserialize(String value) throws Exception{
		if(value == null || value.trim().length() == 0){
			return null;
		}
		
		ISerializer requestSerializer = new DefaultSerializer();
		requestSerializer.register(Request.class);
		Request request = (Request) requestSerializer.deserialize(value);
		
		ISerializer serializer = (ISerializer) new JavaSerializer(ObjectSerializer.class).deserialize(request.getSerializer());
		return serializer.deserialize(request.getContent());
	}
	
}

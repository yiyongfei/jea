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

@Component
public class CacheContext {
	@Autowired
	private ICacheHandle l1CacheHandle;
	
	public CacheContext(){
		
	}

	public void set(Map<String, Object> map, int seconds) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		Set<String> keys = map.keySet();
		for(String key : keys){
			result.put(key, this.serialize(map.get(key)));
		}
		l1CacheHandle.set(result, seconds);
	}
	
	public boolean set(String key, Object value, int seconds) throws Exception{
		return l1CacheHandle.set(key, this.serialize(value), seconds);
	}
	
	public Set<String> keys(String pattern, String regexp) throws Exception{
		return l1CacheHandle.keys(pattern, regexp);
	}
	
	public Boolean expire(String key, int seconds) throws Exception {
		return l1CacheHandle.expire(key, seconds);
	}
	
	public Object get(String key) throws Exception{
		return this.deserialize(l1CacheHandle.get(key));
	}
	
	public Boolean exists(String key) throws Exception  {
		return l1CacheHandle.exists(key);
	}
	
	public Map<String, Object> getByRegexp(String pattern, String regexp) throws Exception{
		Map<String, String> map = l1CacheHandle.getByRegexp(pattern, regexp);
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> keys = map.keySet();
		for(String key : keys){
			result.put(key, this.deserialize(map.get(key)));
		}
		return result;
	}
	
	public String showRedisByRegexp(String pattern, String regexp) throws Exception{
		return l1CacheHandle.showRedisByRegexp(pattern, regexp);
	}
	
	public void clean(String key) throws Exception{
		l1CacheHandle.clean(key);
	}
	
	public int cleanByRegexp(String pattern, String regexp) throws Exception{
		return l1CacheHandle.cleanByRegexp(pattern, regexp);
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
		ISerializer requestSerializer = new DefaultSerializer();
		requestSerializer.register(Request.class);
		Request request = (Request) requestSerializer.deserialize(value);
		
		ISerializer serializer = (ISerializer) new JavaSerializer(ObjectSerializer.class).deserialize(request.getSerializer());
		return serializer.deserialize(request.getContent());
	}
	
}

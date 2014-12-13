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
package com.ea.core.kryo.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.ea.core.base.dto.DynamicDTO;
import com.ea.core.base.model.BaseModel;
import com.ea.core.kryo.AbstractSerializer;

/**
 * 序列化Object对象
 * PS：目前在序列化HashMap和ArrayList有些问题，相应的可以替换成TreeMap和LinkedList
 * 
 * @author yiyongfei
 *
 */
public class ObjectSerializer extends AbstractSerializer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7256431025561662836L;

	public ObjectSerializer(){
		super();
	}
	
	@Override
	public String serialize(Object obj) throws Exception{
		return super.serialize(obj);
	}
	
	@SuppressWarnings("unchecked")
	protected void register(Object obj) {
		if(obj == null){
			return;
		} else {
			if (obj instanceof DynamicDTO){
				throw new RuntimeException("不能序列化DynamicDTO类型的对象，请调用getMap方法获得Map对象");
			} else {
				this.register(obj.getClass());
				if(obj instanceof Map){
					//Map的处理
					Map<String, Object> map = (Map<String, Object>)obj;
					Set<String> keys = map.keySet();
					for(String key : keys){
						register(map.get(key));
					}
				} else if (obj instanceof BaseModel){
					DynamicDTO dto = new DynamicDTO((BaseModel)obj);
					Set<String> keys = dto.properties();
					for(String key : keys){
						register(dto.getValue(key));
					}
				} else if (obj instanceof Collection){
					//对象集合的处理
					Collection<Object> c = (Collection<Object>) obj;
					for(Object tmp : c){
						register(tmp);
					}
				} else if (obj instanceof Object[]){
					//对象数组的处理
					Object[] ary = (Object[]) obj;
					for(Object tmp : ary){
						register(tmp);
					}
				} else {
					Class<?> className = obj.getClass();
					if(className.getName().indexOf("BeanGeneratorByCGLIB") > 0) {
						//根据DynamicDTO获得的Bean对象的处理
						DynamicDTO dto = new DynamicDTO(obj);
						Set<String> keys = dto.properties();
						for(String key : keys){
							register(dto.getValue(key));
						}
					}
				}
			}
		}
	}
}

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
package com.ea.core.base.dto;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jodd.typeconverter.TypeConverterManager;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import com.ea.core.base.model.BaseModel;
import com.ea.core.typeconverter.impl.StringConverter;


public class DynamicDTO extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -599263703870892867L;
	
	private BeanGenerator generator;
	private BeanMap beanMap;
	private Map<String, Object> valueMap;
	private Object beanObject;
	
	public DynamicDTO(){
		generator = new BeanGenerator();
		valueMap = new TreeMap<String, Object>();
	}
	
	public DynamicDTO(Map<String, Object> map){
		generator = new BeanGenerator();
		/*使用Kryo序列化Map时，发现HashMap的序列化会报NullPointException，用TreeMap就没问题*/
		/*考虑到可能会使用DynamicDTO来获得Map对象，实例化时使用TreeMap类型*/
		valueMap = new TreeMap<String, Object>();
		Iterator<String> keys = map.keySet().iterator();
		String key = null;
		while(keys.hasNext()){
			key = keys.next();
			this.setValue(key, map.get(key));
		}
		try {
			generateBean();
		} catch (Exception e) {
			throw new RuntimeException("生成Bean对象时出现异常！");
		}
	}
	
	public DynamicDTO(BaseModel model){
		generator = new BeanGenerator();
		valueMap = new TreeMap<String, Object>();
		beanObject = model;
		beanMap = BeanMap.create(model);
		valueMap.putAll(beanMap);
	}
	
	/**
	 * 建议用于SOAP对象的转换，一般的JavaBean请继承BaseDTO或BasePO
	 * @param obj
	 */
	public DynamicDTO(Object obj){
		generator = new BeanGenerator();
		valueMap = new TreeMap<String, Object>();
		beanObject = obj;
		beanMap = BeanMap.create(obj);
		valueMap.putAll(beanMap);
	}
	
	/**
	 * 若不通过Map或Model生成DynamicDTO，可通过setValue设置新生成DTO的属性和该属性的值
	 * 
	 * @param propName
	 * @param value
	 */
	public void setValue(String propName, Object value){
		setValue(propName, null, value);
	}
	public void setValue(String propName, Class<? extends Object> className, Object value){
		if(beanMap == null){
			if(className == null){
				if(value == null){
					return;
				} else {
					generator.addProperty(propName, value.getClass());
				}
			} else {
				generator.addProperty(propName, className);
			}
			
			this.valueMap.put(propName, value);
		} else {
			beanMap.put(propName, value);
		}
	}
	
	public Object getValue(String property) {
		if(beanMap == null){
			return this.valueMap.get(property);
		} else {
			return this.beanMap.get(property);
		}
	}
	
	/**
	 * 获得DynamicDTO所有属性
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<String> properties(){
		if(beanMap == null){
			return this.valueMap.keySet();
		} else {
			return this.beanMap.keySet();
		} 
	}
	
	/**
	 * 获得DynamicDTO指定属性的Class类型
	 * 
	 * @param property
	 * @return
	 */
	public Class getPropertyType(String property){
		if(beanMap == null){
			return this.valueMap.get(property).getClass();
		} else {
			return this.beanMap.getPropertyType(property);
		}
	}
	
	public Object getBean() {
		if(beanObject == null) {
			try {
				generateBean();
			} catch (Exception e) {
				return null;
			}
		}
		return beanObject;
	}
	
	public Object getCloneBean() {
		try {
			Object tmpObject = generator.create();
			BeanMap tmpMap = BeanMap.create(tmpObject);
			tmpMap.putAll(valueMap);
			return tmpObject;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Map<String, Object> getMap(){
		return valueMap;
	}
	
	/**
	 * 转换器，设置DynamicDTO的属性值到对应的Model对象
	 * 
	 * @param target
	 */
	@SuppressWarnings("unchecked")
	public void convert(BaseModel target) {
		BeanMap tmp = BeanMap.create(target);
		Iterator<String> keys = tmp.keySet().iterator();
		String key = null;
		while(keys.hasNext()){
			key = keys.next();
			if(beanMap.containsKey(key)){
				StringConverter converter = new StringConverter();
				TypeConverterManager.register(String.class, converter);
				tmp.put(key, TypeConverterManager.convertType(beanMap.get(key), tmp.getPropertyType(key)));
			}
		}
	}
	
	public void reset(){
		valueMap.clear();
		beanMap.clear();
		beanMap = null;
		beanObject = null;
	}
	
	public void destroy(){
		beanMap.clear();
		valueMap.clear();
		valueMap = null;
		beanMap = null;
		generator = null;
		beanObject = null;
	}

	private void generateBean() throws Exception {
		beanObject = generator.create();
		beanMap = BeanMap.create(beanObject);
		beanMap.putAll(valueMap);
	}

}

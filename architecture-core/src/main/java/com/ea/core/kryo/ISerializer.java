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
package com.ea.core.kryo;

import java.io.Serializable;

import com.esotericsoftware.kryo.Serializer;

public interface ISerializer extends Serializable{
	
	/**
	 * 序列化成字符串，字符串以ISO-8859-1编码
	 * 目前框架里所有序列化的内容都以字符串方式存在
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public String serialize(Object obj) throws Exception;
	
	/**
	 * 序列化成byte数组
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public byte[] Serialize(Object obj) throws Exception;
	
	/**
	 * 根据字符串内容反序列化成对象
	 * 
	 * @param serialString
	 * @return
	 * @throws Exception
	 */
	public Object deserialize(String serialString) throws Exception;
	
	public Object Deserialize(byte[] aryByte) throws Exception;
	
	/**
	 * 注册Class
	 * 
	 * @param type
	 */
	public void register(Class<?> type);
	
	public void register(Class<?> type, Serializer<?> serializer);
}

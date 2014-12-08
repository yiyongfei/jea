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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class AbstractSerializer implements ISerializer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Class<?>, Serializer<?>> propertyMap;
	
	public AbstractSerializer(){
		propertyMap = new LinkedHashMap<Class<?>, Serializer<?>>();
	}
	
	@Override
	public byte[] Serialize(Object obj) throws Exception {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outStream = _Serialize(obj);
		try{
			return outStream.toByteArray();
		} finally {
			outStream.close();
		}
	}
	@Override
	public String serialize(Object obj) throws Exception {
		ByteArrayOutputStream outStream = _Serialize(obj);
		try{
			return outStream.toString("ISO-8859-1");
		} finally {
			outStream.close();
		}
	}
	
	private ByteArrayOutputStream _Serialize(Object obj) throws Exception{
		initBySerialize(obj);
		Kryo kryo = initKryo();
		Output output = null; 
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try{
			output = new Output(outStream);
			kryo.writeClassAndObject(output, obj); 
			output.flush();
			return outStream;
		} finally {
			output.close();
			kryo = null;
		}
	}
	
	public Object Deserialize(byte[] aryByte) throws Exception{
		Kryo kryo = initKryo();
		Input input = null;
        try {
            input = new Input(new ByteArrayInputStream(aryByte));
            Object obj = kryo.readClassAndObject(input);
            return obj;
        } finally {
        	if(input != null){
        		input.close(); 
        	}
        	kryo = null;
        }
	}
	public Object deserialize(String serialString) throws Exception{
		return Deserialize(serialString.getBytes("ISO-8859-1"));
	}
	
	public void register(Class<?> type){
		propertyMap.put(type, null);
	}
	
	public void register(Class<?> type, Serializer<?> serializer){
		propertyMap.put(type, serializer);
	}
	
	protected void initBySerialize(Object obj) {
		register(obj);
	}
	
	protected Kryo initKryo() {
		Kryo kryo = new Kryo();
		kryo.setReferences(true); 
		kryo.setRegistrationRequired(true); 
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		Set<Class<?>> keys = propertyMap.keySet();
		for(Class<?> key : keys){
			if(propertyMap.get(key) != null){
				kryo.register(key, propertyMap.get(key));
			} else {
				kryo.register(key);
			}
		}
		
		return kryo;
	}

	protected abstract void register(Object obj);
}

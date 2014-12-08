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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.ea.core.kryo.ISerializer;
import com.ea.core.kryo.JavaSerializerWrapper;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class JavaSerializer implements ISerializer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4662496051401930682L;
	private Class<?> className;
	private Serializer<?> serializer;

	public JavaSerializer(){
		this.serializer = new JavaSerializerWrapper();
	}
	public JavaSerializer(Class<?> type){
		this.className = type;
		this.serializer = new JavaSerializerWrapper();
	}
	public JavaSerializer(Class<?> type, Serializer<?> serializer){
		this.className = type;
		this.serializer = serializer;
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
	
	private ByteArrayOutputStream _Serialize(Object obj) throws Exception {
		// TODO Auto-generated method stub
		if(obj instanceof Serializable){
			this.className = obj.getClass();
			Kryo kryo = new Kryo();
			kryo.setReferences(true); 
			kryo.setRegistrationRequired(true); 
			kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
			kryo.register(obj.getClass(), this.serializer);
			kryo.register(this.serializer.getClass());
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
		} else {
			throw new Exception("欲序列化的对象请实现Serializable接口");
		}
		
	}

	@Override
	public Object Deserialize(byte[] aryByte) throws Exception {
		Kryo kryo = new Kryo();
		kryo.setReferences(true); 
		kryo.setRegistrationRequired(true); 
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		kryo.register(this.className, this.serializer);
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
	@Override
	public Object deserialize(String serialString) throws Exception {
		return Deserialize(serialString.getBytes("ISO-8859-1"));
	}

	@Override
	public void register(Class<?> type) {
		// TODO Auto-generated method stub
		this.className = type;
	}

	@Override
	public void register(Class<?> type, Serializer<?> serializer) {
		// TODO Auto-generated method stub
		this.className = type;
		this.serializer = serializer;
	}
}

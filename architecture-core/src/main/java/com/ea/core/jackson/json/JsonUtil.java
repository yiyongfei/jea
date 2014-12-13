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
package com.ea.core.jackson.json;

import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static JsonFactory factory = new ObjectMapper().getFactory();
	
	/**
	 * 将对象转换成Json串
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static <T> String generatorJson(T object) throws Exception{
		StringWriter writer = new StringWriter();
		JsonGenerator generator = factory.createGenerator(writer);
		try{
			generator.writeObject(object);
			return writer.toString();
		} finally {
			generator.close();
			writer.close();
		}
	}
	
	/**
	 * 将Json串转换成对象
	 * 
	 * @param jsonString
	 * @param objClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T parserJson(String jsonString, Class<T> objClass) throws Exception{
		JsonParser parser = factory.createParser(jsonString);
		try{
			return parser.readValueAs(objClass);
		} finally {
			parser.close();
		}
	}
	
}

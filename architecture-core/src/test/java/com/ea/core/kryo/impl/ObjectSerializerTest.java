package com.ea.core.kryo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.ea.core.base.dto.DynamicDTO;

public class ObjectSerializerTest {

	@Test
	public void testSerialize(){
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("key1", "value1");
		tmp.put("key2", 1);
		tmp.put("key3", new Date());
		tmp.put("key4", true);
		tmp.put("key5", 0.1);
		
		ObjectSerializer s = new ObjectSerializer();
		s.register(HashMap.class);
		try {
			String str = s.serialize(tmp);
			Object obj = s.deserialize(str);
			System.out.println(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

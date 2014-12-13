package com.ea.core.kryo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.ea.core.base.dto.DynamicDTO;
import com.ea.core.jackson.json.JsonUtil;

public class ObjectSerializerTest {

	@Test
	public void testSerialize(){
		Map<String, Object> tmp = new TreeMap<String, Object>();
		tmp.put("key1", "value1");
		tmp.put("key2", 1);
		tmp.put("key3", new Date());
		tmp.put("key4", true);
		tmp.put("key5", 0.1);
		
		DynamicDTO dto = new DynamicDTO();
		dto.setValue("ijfdifjeir", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("uyr78ruerer", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("y5tergdgdfg", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("rdfdfeerefd", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("hhfghfgggdf", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("jiwejfsdjfjkdn", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("sjdfksdmcxkd", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("jiwjfsfkdsss", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("skjfkmcsjfsf", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("jifjsdkmxkms", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("jklsmfkdmcxc", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("dfjoijwfklsf", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("jsdmcsjfksfm", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("jsifjksdmcxkl", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir1", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir2", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir3", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir4", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir5", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir6", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir7", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir8", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeir9", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirq", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirw", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeire", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirr", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirt", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeiry", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeiru", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeiri", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeiro", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirp", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeira", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirs", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeird", "jojierkefjoijifdkfjoidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijfdifjeirf", "jojierkefjoijifdkfj国在伙杰脑海里枷你数学粉碎噢对做数学数据源oidjfiowejfkfjoij8u34irje路边人");
		dto.setValue("ijirjsdfjis1", new Date());
		dto.setValue("ijirjsdfjis2", new Boolean(true));
		dto.setValue("ijirjsdfjis3", 7583458934889895L);
		dto.setValue("ijirjsdfjis4", new BigDecimal(754545.656));
		dto.setValue("ijirjsdfjis5", 785546);
		dto.setValue("ijirjsdfjis6", 7583458934889895.76);
		dto.setValue("ijirjsdfjis7", new Float(5485.656));
		dto.setValue("ijirjsdfjis8", tmp);
		dto.setValue("ijirjsdfjis9", new int[]{67,34,54});
		
		List list = new LinkedList();
		for(int i = 0; i < 50; i++){
			list.add(dto.getCloneBean());
		}
		
		System.out.println(list);
		ObjectSerializer s = new ObjectSerializer();
		s.register(list);
		
		try {
			long start = new Date().getTime();
			String str = s.serialize(list);
			Object obj = s.deserialize(str);
			long end = new Date().getTime();
			System.out.println("time" + (end - start));
//			DynamicDTO dto2 = new DynamicDTO(obj);
//			System.out.println(obj);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

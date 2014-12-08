package com.ea.core.jackson;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ea.core.jackson.xml.XmlUtil;

public class XmlTest {
	@Test
	public void testToXml(){
		DTO dto = new DTO();
		dto.setId("001");
		dto.setName("张三");
		dto.setAge(25);
		dto.setAges(26);
		dto.setBirth(new Date());
		dto.setSale(3000);
		dto.setSales(4000);
		dto.setType('a');
		dto.setWork(true);
		dto.setBignum(new BigDecimal(456.675).setScale(3, BigDecimal.ROUND_HALF_DOWN));
		char[] ary = new char[3];
		ary[0] = '1';
		ary[1] = 'a';
		ary[2] = '&';
		dto.setAryChar(ary);
		
		Map map = new HashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		dto.setMap(map);
		
//		ChildDTO child = new ChildDTO();
//		child.setChildId("childId");
//		child.setChildName("childName");
//		dto.setChild(child);
		String json = null;
		try {
			json = XmlUtil.generatorXml(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("xml\n" + json);
	}
	
	@Test
	public void testToObject(){
		DTO dto = new DTO();
		dto.setId("001");
		dto.setName("张三");
		dto.setAge(25);
		dto.setAges(26);
		dto.setBirth(new Date());
		dto.setSale(3000);
		dto.setSales(4000);
		dto.setType('a');
		dto.setWork(true);
		dto.setBignum(new BigDecimal(456.675).setScale(3, BigDecimal.ROUND_HALF_DOWN));
		char[] ary = new char[3];
		ary[0] = '1';
		ary[1] = 'a';
		ary[2] = '&';
//		dto.setAryChar(ary);
		
		Map map = new HashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		dto.setMap(map);
		
//		ChildDTO child = new ChildDTO();
//		child.setChildId("childId");
//		child.setChildName("childName");
//		dto.setChild(child);
		String json = "";
		try {
			json = XmlUtil.generatorXml(dto);
			DTO dto2 = XmlUtil.parserXml(json, DTO.class);
			System.out.println("dto2\n" + dto2.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}


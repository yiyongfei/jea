package com.ea.core.jackson;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ea.core.jackson.json.JsonUtil;

public class JsonTest {

	@Test
	public void testToJson(){
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
		

		ChildDTO child = new ChildDTO();
		child.setChildId("childId");
		child.setChildName("childName");
		dto.setChild(child);
		
		String json = null;
		try {
			json = JsonUtil.generatorJson(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("json\n" + json);
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
		dto.setAryChar(ary);
		
		Map map = new HashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		dto.setMap(map);
		
		ChildDTO child = new ChildDTO();
		child.setChildId("childId");
		child.setChildName("childName");
		dto.setChild(child);
		
		String json = "";
		try {
			json = JsonUtil.generatorJson(dto);
			DTO dto2 = JsonUtil.parserJson(json, DTO.class);
			System.out.println("dto2\n" + dto2.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class DTO {
	private String id;
	private String name;
	private boolean isWork;
	private int age;
	private float sale;
	private Date birth;
	private char type;
	private double sales;
	private long ages;
	private BigDecimal bignum;
	private char[] aryChar;
	private Map<String, Object> map;
	private ChildDTO child;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isWork() {
		return isWork;
	}
	public void setWork(boolean isWork) {
		this.isWork = isWork;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public float getSale() {
		return sale;
	}
	public void setSale(float sale) {
		this.sale = sale;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public double getSales() {
		return sales;
	}
	public void setSales(double sales) {
		this.sales = sales;
	}
	public long getAges() {
		return ages;
	}
	public void setAges(long ages) {
		this.ages = ages;
	}
	public BigDecimal getBignum() {
		return bignum;
	}
	public void setBignum(BigDecimal bignum) {
		this.bignum = bignum;
	}
	
	public char[] getAryChar() {
		return aryChar;
	}
	public void setAryChar(char[] aryChar) {
		this.aryChar = aryChar;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
	public ChildDTO getChild() {
		return child;
	}
	public void setChild(ChildDTO child) {
		this.child = child;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("id"+"=").append(id).append("\n");
		sb.append("name"+"=").append(name).append("\n");
		sb.append("age"+"=").append(age).append("\n");
		sb.append("ages"+"=").append(ages).append("\n");
		sb.append("sale"+"=").append(sale).append("\n");
		sb.append("sales"+"=").append(sales).append("\n");
		sb.append("birth"+"=").append(birth).append("\n");
		sb.append("bignum"+"=").append(bignum).append("\n");
		sb.append("iswork"+"=").append(isWork).append("\n");
		sb.append("type"+"=").append(type).append("\n");
		sb.append("map"+"=").append(map).append("\n");
//		sb.append("aryChar"+"=").append(aryChar).append("\n");
		sb.append("child"+"=").append(child).append("\n");
		return sb.toString();
	}
}

class ChildDTO{
	private String childId;
	private String childName;
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("childId"+"=").append(childId).append("\n");
		sb.append("childName"+"=").append(childName).append("\n");
		return sb.toString();
	}
	
}
package com.ea.core.kryo.impl;


import org.junit.Test;

import com.ea.core.kryo.ISerializer;

public class JavaSerializerTest {
	@Test
	public void testException(){
		Exception ex = new ClassNotFoundException("haha");
		
		ISerializer s = new JavaSerializer();
		try {
			String str = s.serialize(ex);
			
			ISerializer s2 = new JavaSerializer();
			String dstr = s2.serialize(s);
			
			ISerializer s3 = (ISerializer) s2.deserialize(dstr);
			Object obj = s3.deserialize(str);
			System.out.println("wowo"+obj);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

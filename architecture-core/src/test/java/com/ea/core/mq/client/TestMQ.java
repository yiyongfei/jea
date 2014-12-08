package com.ea.core.mq.client;

import org.junit.Test;

public class TestMQ {

	@Test
	public void send() throws Exception{
		byte[] ary = "ijsdfoidsf98u4ijfi中央".getBytes();
		MQClient client = new MQClient("tcp://192.168.222.134:61616", "admin", "admin");
		client.sendMessage("exampleQueue", ary);
	}
	
	@Test
	public void receive() throws Exception{
		MQClient client = new MQClient("tcp://192.168.222.134:61616", "admin", "admin");
		byte[] ary = client.receiveMessage("exampleQueue");
		System.out.println("receive:" + new String(ary));
	}
}

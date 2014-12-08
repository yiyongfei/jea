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
package com.ea.core.mq.client;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.ea.core.kryo.impl.ObjectSerializer;
import com.ea.core.mq.pool.MQPool;
import com.ea.core.mq.pool.factory.MQPoolFactory;

public class MQClient {
	private MQPool mqPool;
	
	public MQClient(String host, String username, String password) {
		mqPool = MQPoolFactory.getInstance(MQPoolFactory.ACTIVE_MQ);
		mqPool.addMQPool(host, username, password);
	}
	
	public void sendMessage(String queue, byte[] message) throws Exception {
		//Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		//Session： 一个发送或接收消息的线程
		Session session = null;
		//Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		//MessageProducer：消息发送者
		MessageProducer producer;
		//TextMessage mqMessage;
		BytesMessage mqMessage;
		try {
			// 构造从工厂得到连接对象
			connection = mqPool.createConnection();
			// 获取操作连接
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queue);
			// 创建消息生产者
            producer = session.createProducer(destination);
            // 设置持久化，DeliveryMode.PERSISTENT和DeliveryMode.NON_PERSISTENT
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 创建消息
            mqMessage = session.createBytesMessage();
            mqMessage.writeBytes(message);
            // 发送消息到ActiveMQ
            producer.send(mqMessage);
		} finally {
			try {
				mqPool.close(connection);
			} catch (Throwable ignore) {
				ignore.printStackTrace();
			}
		}
	}
	
	public byte[] receiveMessage(String queue) throws Exception {
		//Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		//Session： 一个发送或接收消息的线程
		Session session = null;
		//Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		//MessageProducer：消息消费者
		MessageConsumer consumer;
		//TextMessage mqMessage;
		Message mqMessage = null;
		try {
			// 构造从工厂得到连接对象
			connection = mqPool.createConnection();
			// 获取操作连接
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queue);
			// 创建消息消费者
			consumer = session.createConsumer(destination);
            // 接收消息
            mqMessage = consumer.receiveNoWait();
            if(mqMessage != null){
            	if(mqMessage instanceof BytesMessage){
                	byte[] result = new byte[(int)((BytesMessage)mqMessage).getBodyLength()];
                	((BytesMessage)mqMessage).readBytes(result);
                	return result;
                } else {
                	return new ObjectSerializer().Serialize(mqMessage);
                }
            } else {
            	return null;
            }
		} finally {
			try {
				mqPool.close(connection);
			} catch (Throwable ignore) {
				ignore.printStackTrace();
			}
		}
	}
	
	public void stopPool() throws Exception {
		mqPool.stop();
	}
}
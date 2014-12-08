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
package com.ea.core.mq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ea.core.mq.MQDefinition;
import com.ea.core.mq.client.MQClient;

public abstract class AbstractConsumer implements IConsumer {
	private Logger logger = LoggerFactory.getLogger(AbstractConsumer.class);
	private MQClient client = null;
	private String queueName;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractConsumer(){
		client = new MQClient(MQDefinition.getPropertyValue("mq.server.host"), MQDefinition.getPropertyValue("mq.server.username"), MQDefinition.getPropertyValue("mq.server.password"));
	}
	@Override
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			execute();
		} catch (Exception e) {
			//异常处理（策略是记录原始取到的队列信息和异常信息，并写到队列里，队列名为errorQueue）
			//
		}
	}
	
	protected void execute() throws Exception {
		byte[] queueContent = client.receiveMessage(queueName);
		if(queueContent == null){
			return;
		}
		ReceiveDTO request = deserialize(queueContent);
		logger.info("从队列" + queueName + "收到消息，该消息将交由" + request.getRequestId() + "处理！");
		perform(request.getRequestId(), request.getParams());
	}
	
	/**
	 * 反序列化，默认反序列化方式在DefaultReceive实现
	 * @param aryByte
	 * @return
	 * @throws Exception
	 */
	protected abstract ReceiveDTO deserialize(byte[] aryByte) throws Exception;
	
	protected abstract Object perform(String facadeName, Object... models) throws Exception;
	
}

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
package com.ea.core.bridge.async.mq.client;

import com.ea.core.bridge.IClient;
import com.ea.core.mq.client.MQClient;

public class MqSendClient implements IClient {
	MQClient client = null;
	public MqSendClient(String host, String username, String password) {
		client = new MQClient(host, username, password);
	}
	
	@Override
	public String execute(String queueName, String serialString) throws Exception {
		client.sendMessage(queueName, serialString.getBytes("ISO-8859-1"));
		return null;
	}
	
	public boolean stop() {
		try {
			client.stopPool();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
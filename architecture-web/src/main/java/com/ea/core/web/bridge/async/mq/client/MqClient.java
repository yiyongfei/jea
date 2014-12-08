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
package com.ea.core.web.bridge.async.mq.client;

import org.springframework.stereotype.Component;

import com.ea.core.bridge.async.mq.client.MqSendClient;
import com.ea.core.mq.MQDefinition;

@Component
public class MqClient extends MqSendClient {
	public MqClient() {
		super(MQDefinition.getPropertyValue("mq.server.host"), MQDefinition.getPropertyValue("mq.server.username"), MQDefinition.getPropertyValue("mq.server.password"));
	}
	
}
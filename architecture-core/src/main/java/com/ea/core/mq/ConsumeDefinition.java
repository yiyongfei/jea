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
package com.ea.core.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ea.core.mq.consumer.IConsumer;

public class ConsumeDefinition {
	private static String section_consume_queue = "consume.queue.mapping";
	
	private static Map<String, IConsumer> consumerMapping = new HashMap<String, IConsumer>();
	
	static{
		Set<String> topolotyNames = MQDefinition.mapProperty.keySet();
		for(String tmp : topolotyNames){
			if(tmp.startsWith(section_consume_queue)){
				try {
					IConsumer consumer = (IConsumer) Class.forName(MQDefinition.mapProperty.get(tmp)).newInstance();
					String queueName = tmp.substring(section_consume_queue.length() + 1);
					consumer.setQueueName(queueName);
					consumerMapping.put(queueName, consumer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static IConsumer findByQueue(String queueName){
		return consumerMapping.get(queueName);
	}
	
	public static Set<String> queues(){
		return consumerMapping.keySet();
	}
}

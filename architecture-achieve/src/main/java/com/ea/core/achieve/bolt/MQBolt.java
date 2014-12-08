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
package com.ea.core.achieve.bolt;

import java.util.Set;

import com.ea.core.mq.ConsumeDefinition;
import com.ea.core.storm.bolt.AbstractRichBolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;

public class MQBolt extends AbstractRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 612150437493967382L;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Tuple input, OutputCollector collector) {
		// TODO Auto-generated method stub
		Set<String> queues = (Set<String>) input.getValueByField("queue");
		for(String queue : queues){
			new Thread(ConsumeDefinition.findByQueue(queue)).start();
		}
		collector.ack(input);
	}
}

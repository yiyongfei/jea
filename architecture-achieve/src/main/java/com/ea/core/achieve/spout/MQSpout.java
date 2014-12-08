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
package com.ea.core.achieve.spout;

import com.ea.core.mq.ConsumeDefinition;
import com.ea.core.storm.spout.AbstractRichSpout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class MQSpout extends AbstractRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4599561493534298748L;
	    
    @Override
	public void perform(SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(99);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		collector.emit(new Values(ConsumeDefinition.queues()));
	}
    
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("queue"));
	}

}

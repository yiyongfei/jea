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
package com.ea.core.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public abstract class AbstractRichBolt extends BaseRichBolt {
	private String boltName;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map conf;
	private TopologyContext context;
	private OutputCollector collector;

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		execute(input, this.collector);
	}
	
	@Override
	public void prepare(Map conf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.conf = conf;
		this.context = context;
		this.collector = collector;
	}
	protected Map getConf(){
		return this.conf;
	}
	protected TopologyContext getContext(){
		return this.context;
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
	
	protected abstract void execute(Tuple input, OutputCollector collector);
	
	public String getBoltName() {
		return boltName;
	}
	public void setBoltName(String boltName){
		this.boltName = boltName;
	}
}

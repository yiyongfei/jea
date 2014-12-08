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
package com.ea.core.storm.spout;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichSpout;

public abstract class AbstractRichSpout extends BaseRichSpout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map conf;
	private TopologyContext context;
	private SpoutOutputCollector collector;
	
	@Override
	public void nextTuple() {
		perform(collector);
	}
	
	protected abstract void perform(SpoutOutputCollector collector);
	
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
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
	
}

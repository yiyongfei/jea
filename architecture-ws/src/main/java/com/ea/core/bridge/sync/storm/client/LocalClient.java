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
package com.ea.core.bridge.sync.storm.client;

import backtype.storm.LocalDRPC;

import com.ea.core.bridge.IClient;
import com.ea.core.storm.StormConstant;
import com.ea.core.storm.TopologyDefinition;
import com.ea.core.storm.main.AbstractLocalSubmitTopology;

public class LocalClient implements IClient {
	private LocalDRPC client = null;
	
	@Override
	public String execute(String topolopyName, String serialString) throws Exception {
		return client.execute(topolopyName, serialString);
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		this.client.shutdown();
		return true;
	}
	
	protected void init() throws Exception{
		Class<?> className = TopologyDefinition.findSubmitMode(StormConstant.SUBMIT_MODE.LOCAL.getCode());
		AbstractLocalSubmitTopology submit = (AbstractLocalSubmitTopology) className.newInstance();
		submit.submitTopology();
		client = submit.getClient();
	}
}

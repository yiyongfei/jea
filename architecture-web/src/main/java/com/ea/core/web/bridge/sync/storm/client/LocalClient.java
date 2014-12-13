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
package com.ea.core.web.bridge.sync.storm.client;

import org.springframework.stereotype.Component;

import com.ea.core.base.CoreDefinition;
import com.ea.core.web.bridge.BridgeConstant;

@Component
public class LocalClient extends com.ea.core.bridge.sync.storm.client.LocalClient {
	public LocalClient() throws Exception{
		super();
		if(BridgeConstant.CONNECTOR_MODE.STORM_LOCAL.getCode().equals(CoreDefinition.getPropertyValue("sync.mode"))){
			super.init();
		}
	}
	
}

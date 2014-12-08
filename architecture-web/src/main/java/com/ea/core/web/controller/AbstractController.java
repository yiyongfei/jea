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
package com.ea.core.web.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.ea.core.web.bridge.BridgeConstant;
import com.ea.core.web.bridge.BridgeContext;
import com.ea.core.base.CoreDefinition;

public abstract class AbstractController {
	@Autowired
	BridgeContext abuttingContext;
	
	public Object dispatch(String callback, String facadeId, Object... models) throws Exception{
		String connectorMode = null;
		if(WebConstant.CALL_BACK.ASYNC.getCode().equals(callback)){
			connectorMode = BridgeConstant.CONNECTOR_MODE.MQ.getCode();
		} else {
			connectorMode = CoreDefinition.getPropertyValue("sync.mode");
		}
		
		return abuttingContext.connect(connectorMode, facadeId, models);
	}
}

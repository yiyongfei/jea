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
package com.ea.core.web.bridge.async.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ea.core.bridge.AbstractConnector;
import com.ea.core.bridge.IClient;
import com.ea.core.mq.MQDefinition;

@Component
public class MqConnector extends AbstractConnector {
	@Autowired
	private IClient mqClient;

	@Override
	protected IClient client() {
		// TODO Auto-generated method stub
		return mqClient;
	}

	@Override
	public String findByFacade(String facadeId) {
		// TODO Auto-generated method stub
		return MQDefinition.findByFacade(facadeId);
	}

	@Override
	public void setClient(IClient client) {
		// TODO Auto-generated method stub
	}
}

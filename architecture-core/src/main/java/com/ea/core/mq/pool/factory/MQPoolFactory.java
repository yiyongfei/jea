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
package com.ea.core.mq.pool.factory;

import com.ea.core.mq.pool.MQPool;
import com.ea.core.mq.pool.impl.ActiveMQPool;


public class MQPoolFactory {
	public final static String ACTIVE_MQ = "ACTIVE_MQ";
	
	public static MQPool getInstance(String factoryTag){
		if(ACTIVE_MQ.equals(factoryTag)){
			return new ActiveMQPool();
		}
		
		return null;
	}
}

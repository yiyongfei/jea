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
package com.ea.core.bridge.ws;

import java.net.URL;

import com.ea.core.base.dto.DynamicDTO;
import com.ea.core.bridge.IWSClient;

public abstract class AbstractClient implements IWSClient {
	protected URL httpUrl;
	
	public AbstractClient(URL httpUrl) {
		this.httpUrl = httpUrl;
	}
	
	public Object execute(String methodName, Object... params) throws Exception{
		if(!checkParamType(params)){
			throw new Exception("调用REST客户端时参数必须是String类型或者DynamicDTO类型！");
		}
		return transferWS(methodName, params);
	}
	
	protected abstract Object transferWS(String methodName, Object... params) throws Exception;
	
	private boolean checkParamType(Object... params){
		if(params != null){
			for(Object obj : params){
				if(obj instanceof DynamicDTO || obj instanceof String){
					continue;
				} else {
					return false;
				}
			}
		}
		
		return true;
	}
}

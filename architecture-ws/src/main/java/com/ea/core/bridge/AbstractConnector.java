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
package com.ea.core.bridge;

import com.ea.core.base.request.Request;
import com.ea.core.base.response.Response;
import com.ea.core.base.response.ResponseConstants;
import com.ea.core.bridge.IClient;
import com.ea.core.bridge.IConnector;
import com.ea.core.kryo.ISerializer;
import com.ea.core.kryo.impl.DefaultSerializer;
import com.ea.core.kryo.impl.JavaSerializer;
import com.ea.core.kryo.impl.ObjectSerializer;

public abstract class AbstractConnector implements IConnector {

	public Object connect(String connectorId, String facadeId, Object... models) throws Exception{
		Request request = new Request();
		request.setRequestId(facadeId);
		
		ISerializer contentSerializer = new ObjectSerializer();
		request.setContent(contentSerializer.serialize(models));
		request.setSerializer(new JavaSerializer().serialize(contentSerializer));

		ISerializer requestSerializer = new DefaultSerializer();
		requestSerializer.register(request.getClass());
		String result = client().execute(connectorId, requestSerializer.serialize(request));
		if(result == null){
			return result;
		}
		ISerializer responseSerializer = new DefaultSerializer();
		responseSerializer.register(Response.class);
        Response response = (Response) responseSerializer.deserialize(result);
        ISerializer serializer = null;
        if(response.getResponseId().equals(ResponseConstants.RESPONSE_RESULT.FAILURE.getCode())){
        	serializer = (ISerializer) new JavaSerializer(JavaSerializer.class).deserialize(response.getSerializer());
        	throw (Exception)serializer.deserialize(response.getContent());
        }
        if(response.getResponseId().equals(ResponseConstants.RESPONSE_RESULT.SUCCESS.getCode())){
        	if(response.getSerializer() == null){
        		/*在返回中，没有提供序列化方式，原因是在执行业务操作时，未返回结果或返回结果为null，此时直接返回为null*/
        		return null;
        	}
        	serializer = (ISerializer) new JavaSerializer(ObjectSerializer.class).deserialize(response.getSerializer());
        }
        Object returnObject = serializer.deserialize(response.getContent());
        return returnObject;
    }
	
	protected abstract IClient client();
	public abstract void setClient(IClient client);
	
}

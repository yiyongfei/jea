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
package com.ea.core.achieve.mq.consumer;

import com.ea.core.base.context.AppServerBeanFactory;
import com.ea.core.base.request.Request;
import com.ea.core.facade.IFacade;
import com.ea.core.kryo.ISerializer;
import com.ea.core.kryo.impl.DefaultSerializer;
import com.ea.core.kryo.impl.JavaSerializer;
import com.ea.core.kryo.impl.ObjectSerializer;
import com.ea.core.mq.consumer.AbstractConsumer;
import com.ea.core.mq.consumer.ReceiveDTO;

public class DefaultConsumer extends AbstractConsumer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2267877435951577268L;
	
	@Override
	protected Object perform(String facadeName, Object... models) throws Exception {
		IFacade facade = (IFacade) AppServerBeanFactory.getBeanFactory().getBean(facadeName);
		return facade.facade(models);
	}

	@Override
	protected ReceiveDTO deserialize(byte[] aryByte) throws Exception {
		// TODO Auto-generated method stub
		ReceiveDTO dto = new ReceiveDTO();
		Request request = deserializeRequest(aryByte);
		dto.setRequestId(request.getRequestId());
		if(request.getContent() != null){
			dto.setParams((Object[])deserializeContent(request));
		}
		return dto;
	}
	
	protected Request deserializeRequest(byte[] aryByte) throws Exception{
		ISerializer serializer = new DefaultSerializer();
		serializer.register(Request.class);
		return (Request)serializer.Deserialize(aryByte);
	}
	
	protected Object deserializeContent(Request request) throws Exception{
		ISerializer serializer = (ISerializer) new JavaSerializer(ObjectSerializer.class).deserialize(request.getSerializer());
		return serializer.deserialize(request.getContent());
	}
}

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

import com.ea.core.base.request.Request;
import com.ea.core.base.response.Response;
import com.ea.core.base.response.ResponseConstants;
import com.ea.core.kryo.ISerializer;
import com.ea.core.kryo.impl.DefaultSerializer;
import com.ea.core.kryo.impl.JavaSerializer;
import com.ea.core.kryo.impl.ObjectSerializer;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * DRPC封装Bolt类
 * 1、反序列化远程提交的请求数据
 * 2、调用指定的Facade
 * 3、如果执行成功，将执行结果序列化，并返回
 * 4、如果执行失败，将异常序列化，并返回
 * 
 * @author yiyongfei
 *
 */
public abstract class AbstractDrpcBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String boltName;
	private Map conf;
	private TopologyContext context;
	@Override
	public void execute(Tuple tuple, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		Response response = null;
		try {
			Request request = deserializeRequest(tuple.getString(1));
			Object obj = null;
			if(request.getContent() != null){
				obj = deserializeContent(request);
			}
			Object model = perform(request.getRequestId(), (Object[])obj);
			response = serializeContent(model);
		} catch (Exception e) {
			response = serializeException(e);
		}
		
		try {
			collector.emit(new Values(tuple.getValue(0), serializeResponse(response)));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("id", "result"));
	}
	@Override
    public void prepare(Map conf, TopologyContext context) {
		this.conf = conf;
		this.context = context;
    }
	protected Map getConf(){
		return this.conf;
	}
	protected TopologyContext getContext(){
		return this.context;
	}
	
	protected abstract Object perform(String facadeName, Object... models) throws Exception;
	
	protected Request deserializeRequest(String serialString) throws Exception{
		ISerializer serializer = new DefaultSerializer();
		serializer.register(Request.class);
		return (Request)serializer.deserialize(serialString);
	}
	
	protected Object deserializeContent(Request request) throws Exception{
		ISerializer serializer = (ISerializer) new JavaSerializer(ObjectSerializer.class).deserialize(request.getSerializer());
		return serializer.deserialize(request.getContent());
	}
	
	protected String serializeResponse(Response result) throws Exception{
		ISerializer serializer = new DefaultSerializer();
		serializer.register(Response.class);
		return serializer.serialize(result);
	}
	
	private Response serializeContent(Object model) throws Exception{
		Response result = new Response();
		result.setResponseId(ResponseConstants.RESPONSE_RESULT.SUCCESS.getCode());
		if(model != null){
			ISerializer serializer = new ObjectSerializer();
			result.setContent(serializer.serialize(model));
			result.setSerializer(new JavaSerializer().serialize(serializer));
		}
		return result;
	}
	
	private Response serializeException(Throwable ex) {
		Response result = new Response();
		result.setResponseId(ResponseConstants.RESPONSE_RESULT.FAILURE.getCode());
		ISerializer serializer = new JavaSerializer();
		try {
			result.setContent(serializer.serialize(ex));
			result.setSerializer(new JavaSerializer().serialize(serializer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getBoltName() {
		return boltName;
	}
	public void setBoltName(String boltName){
		this.boltName = boltName;
	}
	
}

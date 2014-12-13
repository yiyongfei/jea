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
package com.ea.core.storm.topology;

import java.util.List;
import java.util.Map;

import com.ea.core.storm.bolt.AbstractDrpcBolt;

import backtype.storm.ILocalDRPC;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.JoinResult;
import backtype.storm.drpc.PrepareRequest;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.generated.StreamInfo;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsGetter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * DRPC Topology的定义
 * 
 * @author yiyongfei
 *
 */
public abstract class AbstractDRPCTopology extends AbstractTopology {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ILocalDRPC localDRPC;
	private String prepareId;
	private String joinId;
	private String returnId;
	
	public void setLocalDRPC(ILocalDRPC localDRPC) {
		this.localDRPC = localDRPC;
	}
	
	@Override
	public void setTopologyName(String topolotyName) {
		super.setTopologyName(topolotyName);
		this.prepareId = topolotyName + "_prepare_id";
		this.joinId = topolotyName + "_join_id";
		this.returnId = topolotyName + "_return_id";
	}
	@Override
	public TopologyBuilder createBuilder(){
		TopologyBuilder builder = super.initBuilder();
		//设置预处理步骤
		builder.setBolt(this.prepareId, new PrepareRequest()).noneGrouping(spoutId);
		AbstractDrpcBolt lastBolt = setBolt(builder, this.prepareId);
		
		OutputFieldsGetter getter = new OutputFieldsGetter();
        lastBolt.declareOutputFields(getter);
        Map<String, StreamInfo> streams = getter.getFieldsDeclaration();
        if(streams.size()!=1) {
            throw new RuntimeException("Must declare exactly one stream from last bolt in LinearDRPCTopology");
        }
        String outputStream = streams.keySet().iterator().next();
        List<String> fields = streams.get(outputStream).get_output_fields();
        if(fields.size()!=2) {
            throw new RuntimeException("Output stream of last component in LinearDRPCTopology must contain exactly two fields. The first should be the request id, and the second should be the result.");
        }
		
        //合并步骤
		builder.setBolt(this.joinId, new JoinResult(this.prepareId))
        .fieldsGrouping(lastBolt.getBoltName(), outputStream, new Fields(fields.get(0)))
        .fieldsGrouping(this.prepareId, PrepareRequest.RETURN_STREAM, new Fields("request"));
		//return步骤
		builder.setBolt(returnId, new ReturnResults()).noneGrouping(this.joinId);
		return builder;
	}
	@Override
	protected IRichSpout initSpout() {
		// TODO Auto-generated method stub
		if(this.localDRPC != null) {
			//本地部署
			return new DRPCSpout(this.getTopologyName(), this.localDRPC);
		} else {
			//远程部署
			return new DRPCSpout(this.getTopologyName());
		}
	}

	@Override
	protected abstract AbstractDrpcBolt setBolt(TopologyBuilder builder, String upStreamId);

}

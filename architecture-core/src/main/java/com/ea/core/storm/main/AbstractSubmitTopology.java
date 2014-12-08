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
package com.ea.core.storm.main;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;

import com.ea.core.storm.cluster.StormCluster;
import com.ea.core.storm.topology.ITopology;

public abstract class AbstractSubmitTopology implements ISubmitTopology {
	private Logger logger = LoggerFactory.getLogger(AbstractSubmitTopology.class);
	private Config conf;
	private StormCluster cluster;
	
	public AbstractSubmitTopology(){
	}
	
	public void submitTopology() throws Exception{
		Collection<ITopology> topologys =  findTopologys();
		if(topologys != null){
			for(ITopology topology : topologys){
				logger.info("将发布Topology:" + topology.getTopologyName());
				TopologyBuilder builder = topology.createBuilder();
				try {
					cluster.submitTopology(
							topology.getTopologyName(),  
						    conf,  
						    builder.createTopology()
						);
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}
	
	protected void init(StormCluster cluster, Config conf){
		this.cluster = cluster;
		this.conf = conf;
	}
	
	protected abstract Collection<ITopology> findTopologys();
	
}

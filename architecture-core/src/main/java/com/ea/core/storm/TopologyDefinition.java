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
package com.ea.core.storm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ea.core.storm.topology.ITopology;

public class TopologyDefinition {

	static String section_topology_definition = "topology.definition";
	private static String section_facade_topology = "facade.topology.mapping";
	private static String section_submit_topology = "submit.definition";
	private static Map<String, ITopology> topolotyMapping = new HashMap<String, ITopology>();
	private static Map<String, Class<?>> submitMapping = new HashMap<String, Class<?>>();
	
	static{
		Set<String> topolotyNames = StormDefinition.mapProperty.keySet();
		for(String tmp : topolotyNames){
			if(tmp.startsWith(section_topology_definition)){
				try {
					ITopology topoloty = (ITopology) Class.forName(StormDefinition.mapProperty.get(tmp)).newInstance();
					String topolotyName = tmp.substring(section_topology_definition.length() + 1);
					topoloty.setTopologyName(topolotyName);
					topolotyMapping.put(topolotyName, topoloty);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(tmp.startsWith(section_submit_topology)){
				try {
					String type = tmp.substring(section_submit_topology.length() + 1);
					submitMapping.put(type, Class.forName(StormDefinition.mapProperty.get(tmp)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static ITopology findByFacade(String facadeId){
		String tmpFacadeId = section_facade_topology + "." + facadeId;
		String topolotyName = null;
		if(StormDefinition.mapProperty.containsKey(tmpFacadeId)){
			topolotyName = (StormDefinition.mapProperty.get(tmpFacadeId));
		} else {
			tmpFacadeId = section_facade_topology + ".default";
			topolotyName = (StormDefinition.mapProperty.get(tmpFacadeId));
		}
		return topolotyMapping.get(topolotyName);
	}
	
	public static Class<?> findSubmitMode(String submitMode){
		return submitMapping.get(submitMode);
	}
	
	public static Collection<ITopology> findAll(){
		return topolotyMapping.values();
	}
	
	public static Collection<ITopology> findFacadeTopology(){
		Set<String> facadeNames = StormDefinition.mapProperty.keySet();
		Collection<ITopology> list = new ArrayList<ITopology>();
		for(String tmp : facadeNames){
			if(tmp.startsWith(section_facade_topology)){
				try {
					list.add(topolotyMapping.get(StormDefinition.mapProperty.get(tmp)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}

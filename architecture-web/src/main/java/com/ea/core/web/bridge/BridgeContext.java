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
package com.ea.core.web.bridge;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ea.core.base.CoreDefinition;
import com.ea.core.bridge.IConnector;
import com.ea.core.storm.TopologyDefinition;
import com.ea.core.storm.topology.ITopology;

@Component
public class BridgeContext implements ApplicationContextAware{
	private Logger logger = LoggerFactory.getLogger(BridgeContext.class);
	private ApplicationContext context;
	private Map<String, IConnector> connectorMapping;
	
	public BridgeContext(){
		connectorMapping = new HashMap<String, IConnector>();
	}

	public Object connect(String connectMode, String facadeId, Object... models) throws Exception {
		// TODO Auto-generated method stub
		long startTime = Calendar.getInstance().getTimeInMillis();
		IConnector connector = (IConnector) connectorMapping.get(connectMode);
		Object result = connectorMapping.get(connectMode).connect(connector.findByFacade(facadeId), facadeId, models);
		long endTime = Calendar.getInstance().getTimeInMillis();
		logger.info("本次执行共耗时" + (endTime - startTime) + "毫秒，连接方式：" + connectMode + "，调用Facade：" + facadeId); 
		return result;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		context = applicationContext;
		connectorMapping.put(BridgeConstant.CONNECTOR_MODE.MQ.getCode(), (IConnector) context.getBean("mqConnector"));
		connectorMapping.put(BridgeConstant.CONNECTOR_MODE.SPRING.getCode(), (IConnector) context.getBean("springConnector"));
		connectorMapping.put(BridgeConstant.CONNECTOR_MODE.STORM_LOCAL.getCode(), (IConnector) context.getBean("localConnector"));
		connectorMapping.put(BridgeConstant.CONNECTOR_MODE.STORM_REMOTE.getCode(), (IConnector) context.getBean("remoteConnector"));
		initConnection();
	}
	
	/**
	 * 启动WEB服务器后，将先往每一个Topology发送一个请求，以预先初始化storm各工作线程Hibernate，以加快后续加载问题
	 * @throws BeansException
	 */
	private void initConnection() {
		if(BridgeConstant.CONNECTOR_MODE.STORM_LOCAL.getCode().equals(CoreDefinition.getPropertyValue("sync.mode"))){
			ActivateStorm astorm = new ActivateStorm(connectorMapping.get(BridgeConstant.CONNECTOR_MODE.STORM_LOCAL.getCode()));
			new Thread(astorm).start();
		}
		if(BridgeConstant.CONNECTOR_MODE.STORM_REMOTE.getCode().equals(CoreDefinition.getPropertyValue("sync.mode"))){
			ActivateStorm astorm = new ActivateStorm(connectorMapping.get(BridgeConstant.CONNECTOR_MODE.STORM_REMOTE.getCode()));
			new Thread(astorm).start();
		}
	}
	
}


/**
 * 首次连接Storm时，需要对Hibernate做些初始化工作，需时大约2-3秒。
 * 增加一个线程，定时触发Storm，以保证Storm的处理时效。
 * @author yiyongfei
 *
 */
class ActivateStorm implements Runnable {
	private Logger logger = LoggerFactory.getLogger(ActivateStorm.class);
	private IConnector connector;
	private long sleeptime = 10*60*1000;

	public ActivateStorm(IConnector connector){
		this.connector = connector;
	}
	
	@Override
	public void run() {
		boolean isAtonce = true;
		while(true){
			try {
				if(isAtonce){
					Thread.sleep(10*1000);
				} else {
					Thread.sleep(sleeptime);
				}
				Collection<ITopology> topologys = TopologyDefinition.findFacadeTopology();
				for(ITopology topology : topologys){
					logger.info("发送到：" + topology.getTopologyName());
					connector.connect(topology.getTopologyName(), "f247969860897966875L", new Object[]{});
				}
				isAtonce = false;
			} catch (Exception e) {
				//异常处理（策略是记录原始取到的队列信息和异常信息，并写到队列里，队列名为errorQueue
				logger.error("异常：" , e);
				isAtonce = true;
			}
		}
	}
}
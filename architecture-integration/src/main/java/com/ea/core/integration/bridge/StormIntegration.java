package com.ea.core.integration.bridge;

import java.util.HashMap;
import java.util.Map;

import com.ea.core.integration.IntegrationConstant;
import com.ea.core.integration.IntegrationContext;

public class StormIntegration {

	public void connect(String host, String port, String timeout, String topologyName, String facadeId, Object... obj) throws Exception{
		IntegrationContext context = new IntegrationContext();
		Map<String, String> conf = new HashMap<String, String>();
		conf.put(IntegrationConstant.CONF.HOST.getCode(), host);
		conf.put(IntegrationConstant.CONF.PORT.getCode(), port);
		conf.put(IntegrationConstant.CONF.TIMEOUT.getCode(), timeout);
		context.connect(IntegrationConstant.CONNECTOR_MODE.STORM.getCode(), conf, topologyName, facadeId, obj);
	}
}

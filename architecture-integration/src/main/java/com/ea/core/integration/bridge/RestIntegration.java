package com.ea.core.integration.bridge;

import java.util.HashMap;
import java.util.Map;

import com.ea.core.integration.IntegrationConstant;
import com.ea.core.integration.IntegrationContext;

public class RestIntegration {

	public void connect(String host, String httpMethod, String method, Object... obj) throws Exception{
		IntegrationContext context = new IntegrationContext();
		Map<String, String> conf = new HashMap<String, String>();
		conf.put(IntegrationConstant.CONF.HOST.getCode(), host);
		conf.put(IntegrationConstant.CONF.HTTP_METHOD.getCode(), httpMethod);
		context.connect(IntegrationConstant.CONNECTOR_MODE.REST.getCode(), conf, method, null, obj);
	}
}

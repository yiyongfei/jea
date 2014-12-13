package com.ea.core.integration.bridge;

import java.util.HashMap;
import java.util.Map;

import com.ea.core.integration.IntegrationConstant;
import com.ea.core.integration.IntegrationContext;

public class SoapIntegration {

	public void connect(String host, String method, Object... obj) throws Exception{
		IntegrationContext context = new IntegrationContext();
		Map<String, String> conf = new HashMap<String, String>();
		conf.put(IntegrationConstant.CONF.HOST.getCode(), host);
		context.connect(IntegrationConstant.CONNECTOR_MODE.SOAP.getCode(), conf, method, null, obj);
	}
}

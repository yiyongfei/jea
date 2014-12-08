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
package com.ea.core.bridge.ws.soap.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.springframework.cglib.beans.BeanMap;

import com.ea.core.base.dto.DynamicDTO;
import com.ea.core.bridge.ws.AbstractClient;

public class SoapClient extends AbstractClient {
	private Client client;
	
	public SoapClient(URL wsdlUrl) {
		super(wsdlUrl);
		
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        client = dcf.createClient(this.httpUrl);
	}
	
	protected Object[] transferWS(String methodName, Object... params) throws Exception{
		Endpoint endpoint = client.getEndpoint();
        QName opName = getQName(endpoint, methodName);
        
        Object[] soapObject = convert(endpoint, opName, params);

        return client.invoke(opName, soapObject);
    }
	
	private Object[] convert(Endpoint endpoint, QName opName, Object... params) throws Exception {
		List<Object> listSoapObject = new ArrayList<Object>();
		BindingOperationInfo boi = endpoint.getEndpointInfo().getBinding().getOperation(opName); // Operation name is processOrder  
        BindingMessageInfo inputMessageInfo = null;
        if (!boi.isUnwrapped()) {
            inputMessageInfo = boi.getWrappedOperation().getInput();
        } else {
            inputMessageInfo = boi.getUnwrappedOperation().getInput();
        }
        List<MessagePartInfo> parts = inputMessageInfo.getMessageParts();
        
        int index = 0;
        for(Object obj : params){
        	MessagePartInfo partInfo = parts.get(index++);
        	Class<?> soapClass = partInfo.getTypeClass();
            Object soapObject = copytoSoapObject(obj, soapClass);
            listSoapObject.add(soapObject);
        }
        
        return listSoapObject.toArray();
	}
	
	private Object copytoSoapObject(Object param, Class<?> soapClass)
			throws Exception {
		Object soapObject = soapClass.newInstance();
		if (soapObject instanceof java.lang.String) {
			soapObject = param.toString();
		} else {
			BeanMap beanMap = BeanMap.create(soapObject);
			DynamicDTO dto = (DynamicDTO) param;
			for (String key : dto.properties()) {
				beanMap.put(key, dto.getValue(key));
			}
		}
		return soapObject;
	}
	
	private QName getQName(Endpoint endpoint, String methodName){
        BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
        QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), methodName);
        
        if (bindingInfo.getOperation(opName) == null) {
            for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                if (methodName.equals(operationInfo.getName().getLocalPart())) {
                    opName = operationInfo.getName();
                    break;
                }
            }
        }
        return opName;
	}
}

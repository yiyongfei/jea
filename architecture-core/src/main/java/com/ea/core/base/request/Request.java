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
package com.ea.core.base.request;

import com.ea.core.base.model.BaseModel;

public class Request extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719792649972660402L;
	/*放置FacadeId（BeanId）*/
	private String requestId;
	/*序列化器被序列化后的内容*/
	private String serializer;
	/*对象被序列化后的内容*/
	private String content;

	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getSerializer() {
		return serializer;
	}
	public void setSerializer(String serializer) {
		this.serializer = serializer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}

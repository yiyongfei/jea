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
package com.ea.core.base.response;

import com.ea.core.base.model.BaseModel;

public class Response extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7756010759471037195L;
	/* 结果：如果调用业务逻辑过程中出现异常，结果为失败，否则成功
	 * ResponseConstants.RESPONSE_RESULT
	 * */
	private String responseId;
	private String serializer;
	private String content;

	public String getResponseId() {
		return responseId;
	}
	public void setResponseId(String responseId) {
		this.responseId = responseId;
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

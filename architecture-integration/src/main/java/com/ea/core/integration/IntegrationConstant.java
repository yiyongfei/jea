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
package com.ea.core.integration;

public class IntegrationConstant {
	public enum CONNECTOR_MODE {
		MQ("MQ"),
		SOAP("SOAP"),
		REST("REST"),
		STORM("STORM");
		
		private String code;

		private CONNECTOR_MODE(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}
	
	public enum CONF {
		HOST("HOST"),
		PORT("PORT"),
		TIMEOUT("TIMEOUT"),
		HTTP_METHOD("HTTP_METHOD"),
		USERNAME("USERNAME"),
		PASSWORD("PASSWORD");
		
		private String code;

		private CONF(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}
	
}

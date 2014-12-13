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
package com.ea.core.cache;

public class CacheConstants {

	public enum CACHE_TYPE {
		MEMCACHED("MEMCACHED", "MEMCACHED"),
		REDIS("REDIS", "REDIS");
		
		private String code;
		private String desc;

		private CACHE_TYPE(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}

	};
	
	public enum CACHE_LEVEL {
		L1("l1.cache.", "1级缓存"),
		L2("l2.cache.", "2级缓存"),
		L3("l3.cache.", "3级缓存");
		
		private String code;
		private String desc;

		private CACHE_LEVEL(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}

	};
	
	public enum REDIS_RESULT {
		SET_RESULT("OK", "set成功的返回结果");
		
		private String code;
		private String desc;

		private REDIS_RESULT(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}
		public String getDesc() {
			return desc;
		}

	};
}

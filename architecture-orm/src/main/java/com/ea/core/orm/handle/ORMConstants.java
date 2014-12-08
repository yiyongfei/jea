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
package com.ea.core.orm.handle;

public class ORMConstants {

	public enum ORM_LEVEL {
		SAVE("SAVE", "保存"), UPDATE("UPDATE", "更新"), DELETE("DELETE", "删除"), LOAD("LOAD", "根据PK查看"), 
		QUERY("QUERY", "查找"), QUERYLIST("QUERYLIST", "查找一批数据"), TRIGGER("TRIGGER", "ֻ触发DB操作"), 
		H_SQL("H_SQL", "使用Hibernate进行SQL操作"), M_SQL("M_SQL", "使用Mybitis进行SQL操作");
		
		private String code;
		private String desc;

		private ORM_LEVEL(String code, String desc) {
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

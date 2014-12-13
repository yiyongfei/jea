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
package com.ea.core.typeconverter.impl;

import java.util.Date;

import jodd.datetime.JDateTime;

/**
 * 日期格式
 * 
 * @author yiyongfei
 *
 */
public class StringConverter extends jodd.typeconverter.impl.StringConverter {
	private String dateFormat = "YYYY-MM-DD hh:mm:ss";

	@Override
	public String convert(Object value) {
		if(value instanceof Date){
			JDateTime jDateTime = new JDateTime(((Date)value));
			return jDateTime.toString(dateFormat);
		}
		return super.convert(value);
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}

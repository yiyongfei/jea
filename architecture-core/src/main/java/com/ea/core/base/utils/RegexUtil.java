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
package com.ea.core.base.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配工具
 * 
 * @author yiyongfei
 *
 */
public class RegexUtil {
	
	public static Set<String> matcher(String regexp, Set<String> keys){
		if(regexp == null || regexp.trim().length() == 0){
			return keys;
		}
		Set<String> set = new HashSet<String>();
		Pattern p = Pattern.compile(regexp);
		Matcher m = null;
		for(String key : keys){
			m = p.matcher(key);
			if(m.matches()){
				set.add(key);
			}
		}
		return set;
	}
	
	public static boolean matcher(String regexp, String key){
		if(regexp == null || regexp.trim().length() == 0){
			return true;
		}
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(key);
		return m.matches();
	}
}

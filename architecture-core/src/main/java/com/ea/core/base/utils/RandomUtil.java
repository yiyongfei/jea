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

import java.util.Random;

/**
 * 随机字符串生成工具
 * 
 * @author yiyongfei
 *
 */
public class RandomUtil {
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789".toUpperCase();
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		System.out.println(RandomUtil.getRandomString(6));
	}
}

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
package com.ea.core.base.po;

import com.ea.core.base.pk.BasePK;
import com.ea.core.base.model.BaseModel;

/**
 * 数据对象类必须继承该类
 * 
 * @author yiyongfei
 *
 */
public abstract class BasePO extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public abstract BasePK getPk();
}

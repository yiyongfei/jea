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
package com.ea.core.base.pk;

import javax.persistence.Embeddable;

import com.ea.core.base.pk.generator.PKGenerator;
import com.ea.core.base.model.BaseModel;

@Embeddable
public abstract class BasePK extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PKGenerator pkGenerator;
	
	public BasePK(PKGenerator pkGenerator){
		this.pkGenerator = pkGenerator;
	}

	public boolean equals(Object obj){
		return super.equals(obj);
	}
	public int hashCode(){
		return super.hashCode();
	}
	public PKGenerator getPkGenerator() {
		return pkGenerator;
	}
	public void setPkGenerator(PKGenerator pkGenerator) {
		this.pkGenerator = pkGenerator;
	}
	
	/**
	 * 在Save时用于生成主键
	 */
	public abstract void generatorPK();
	
	/**
	 * 用于生成应用缓存的Key
	 */
	public abstract void generatorCacheKey();
	
}

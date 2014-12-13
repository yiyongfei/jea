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
package com.ea.core.orm.handle.impl;

import org.springframework.stereotype.Component;

import com.ea.core.orm.handle.AbstractORMHandle;
import com.ea.core.orm.handle.ORMConstants;
import com.ea.core.orm.handle.ORMHandle;
import com.ea.core.orm.handle.dto.ORMParamsDTO;

/**
 * 查询一批符合条件的数据操作，由Mybatis完成
 * 
 * @author yiyongfei
 *
 */
@Component
public class QueryListORMHandle extends AbstractORMHandle {
    
	public QueryListORMHandle() {
		super(ORMConstants.ORM_LEVEL.QUERYLIST.getCode());
	}

	@Override
	protected Object execute(ORMParamsDTO dto) {
		// TODO Auto-generated method stub
		if(dto.getParam() == null){
			return this.getMybatisSessionTemplate().selectList(dto.getSqlid());
		}
		return this.getMybatisSessionTemplate().selectList(dto.getSqlid(), dto.getParam());
	}

	@Override
	public void setNextHandle() {
		ORMHandle nextHandle = (HibernateSqlORMHandle)this.context.getBean("hibernateSqlORMHandle");
		this.setNextHandle(nextHandle);
	}
}

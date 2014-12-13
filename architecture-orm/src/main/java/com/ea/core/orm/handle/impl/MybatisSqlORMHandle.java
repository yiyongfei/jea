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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.stereotype.Component;

import com.ea.core.orm.handle.AbstractORMHandle;
import com.ea.core.orm.handle.ORMConstants;
import com.ea.core.orm.handle.dto.ORMParamsDTO;

/**
 * SQL执行，通过Mybatis完成，用于数据查询（目前不放开使用）
 * @author yiyongfei
 *
 */
@Component
public class MybatisSqlORMHandle extends AbstractORMHandle {
    
	public MybatisSqlORMHandle() {
		super(ORMConstants.ORM_LEVEL.M_SQL.getCode());
	}

	@Override
	protected Object execute(ORMParamsDTO dto) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = this.getMybatisSessionTemplate().getConnection();
		final ORMParamsDTO tmp = dto;
		PreparedStatement ps = connection.prepareStatement(tmp.getSqlid());
		if(tmp.getParam() != null){
			Object data = tmp.getParam();
			if(data instanceof Object[]){
				Object[] array = (Object[])data;
				int index = 1;
				for(Object obj : array){
					setParam(ps, index++, obj);
				}
			} else if (data instanceof Collection) {
				for(Object array : (Collection)data){
					if(array instanceof Object[]){
						int index = 1;
						for(Object obj : (Object[])array){
							setParam(ps, index++, obj);
						}
						ps.addBatch();
					} else {
						throw new SQLException("通过SQL查询DB时，查询参数请以Object[]的方式提供!");
					}
					
				}
			} else {
				throw new SQLException("ͨ通过SQL查询DB时，查询参数请以Object[]或Collection的方式提供!");
			}
		}
		return ps.executeQuery();
	}

	@Override
	public void setNextHandle() {

	}
	
	private void setParam(PreparedStatement ps, int index, Object param) throws SQLException{
		ps.setObject(index, param);
	}
}

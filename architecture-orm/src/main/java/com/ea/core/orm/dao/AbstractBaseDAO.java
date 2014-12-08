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
package com.ea.core.orm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ea.core.base.pk.BasePK;
import com.ea.core.base.po.BasePO;
import com.ea.core.orm.handle.ORMConstants;
import com.ea.core.orm.handle.ORMHandle;
import com.ea.core.orm.handle.dto.ORMParamsDTO;

public abstract class AbstractBaseDAO implements BaseDAO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private ORMHandle triggerORMHandle;
	
	public final BasePK save(BasePO po) throws Exception{
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(po);
		return (BasePK) triggerORMHandle.handle(ORMConstants.ORM_LEVEL.SAVE.getCode(), dto);
	}
	
	public final void update(BasePO po) throws Exception{
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(po);
		triggerORMHandle.handle(ORMConstants.ORM_LEVEL.UPDATE.getCode(), dto);
	}
	
	public final void delete(BasePO po) throws Exception{
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(po);
		triggerORMHandle.handle(ORMConstants.ORM_LEVEL.DELETE.getCode(), dto);
	}
	
	public final BasePO load(BasePO po) throws Exception{
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(po);
		return (BasePO)triggerORMHandle.handle(ORMConstants.ORM_LEVEL.LOAD.getCode(), dto);
	}
	
	public void checkConnection() throws Exception {
		String sql = "select 1 from dual";
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(null);
		dto.setSqlid(sql);
		triggerORMHandle.handle(ORMConstants.ORM_LEVEL.H_SQL.getCode(), dto);
		triggerORMHandle.handle(ORMConstants.ORM_LEVEL.M_SQL.getCode(), dto);
	}
	
	public final Object queryOne(String sqlId) throws Exception{
		return queryOne(sqlId, null);
	}
	public final Object queryOne(String sqlId, Object params) throws Exception{
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(params);
		dto.setSqlid(sqlId);
		return triggerORMHandle.handle(ORMConstants.ORM_LEVEL.QUERY.getCode(), dto);
	}
	
	public final List<Object> queryMany(String sqlId) throws Exception{
		return queryMany(sqlId, null);
	}
	@SuppressWarnings("unchecked")
	public final List<Object> queryMany(String sqlId, Object params) throws Exception{
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(params);
		dto.setSqlid(sqlId);
		return (List<Object>)triggerORMHandle.handle(ORMConstants.ORM_LEVEL.QUERYLIST.getCode(), dto);
	}
	
	public final void executeSQL(String sql, Object[] params) throws Exception{
		if(sql.trim().toLowerCase().startsWith("select")){
			throw new Exception("对于查询操作，请调用queryOne或者queryMany来完成！");
		}
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(params);
		dto.setSqlid(sql);
		triggerORMHandle.handle(ORMConstants.ORM_LEVEL.H_SQL.getCode(), dto);
	}
	
	public final void batchExecuteSQL(String sql, List<Object[]> params) throws Exception{
		if(sql.trim().toLowerCase().startsWith("select")){
			throw new Exception("对于查询操作，请调用queryOne或者queryMany来完成！");
		}
		ORMParamsDTO dto = new ORMParamsDTO();
		dto.setParam(params);
		dto.setSqlid(sql);
		triggerORMHandle.handle(ORMConstants.ORM_LEVEL.H_SQL.getCode(), dto);
	}
}

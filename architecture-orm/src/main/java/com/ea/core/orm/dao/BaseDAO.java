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

import java.io.Serializable;

import com.ea.core.base.pk.BasePK;
import com.ea.core.base.po.BasePO;

/**
 * DAO层操作，默认单表的新增、修改、删除、打开操作
 * 
 * @author yiyongfei
 *
 */
public interface BaseDAO extends Serializable {
	public BasePK save(BasePO po) throws Exception;
	
	public void update(BasePO po) throws Exception;
	
	public void delete(BasePO po) throws Exception;
	
	public BasePO load(BasePO po) throws Exception;
	
	public void checkConnection() throws Exception;
}

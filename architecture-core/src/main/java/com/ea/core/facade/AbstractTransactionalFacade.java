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
package com.ea.core.facade;

import org.springframework.transaction.annotation.Transactional;

/**
 * Facade类，事务控制
 * 在Facade层，子类只要继承该类，该子类的事务就交由Spring Hibernate管理
 * 
 * @author yiyongfei
 *
 */
public abstract class AbstractTransactionalFacade extends AbstractFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transactional(readOnly=false, rollbackFor={Exception.class}) 
	public Object facade(Object... models) throws Exception {
		return super.facade(models);
	}
	
}

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
package com.ea.core.achieve.activate.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ea.core.achieve.activate.service.ActivateService;
import com.ea.core.facade.AbstractFacade;

@Component("f247969860897966875L")
public class ActivateFacadeImpl extends AbstractFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 247969860897966875L;
	@Autowired
	private ActivateService activateService;
	
	@Override
	protected Object perform(Object... obj) throws Exception {
		activateService.activateConnection();
		return null;
	}

}

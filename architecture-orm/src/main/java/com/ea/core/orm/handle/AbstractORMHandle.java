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
package com.ea.core.orm.handle;

import org.mybatis.spring.SqlSessionTemplate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ea.core.orm.handle.dto.ORMParamsDTO;

public abstract class AbstractORMHandle implements ORMHandle, ApplicationContextAware {
	protected ApplicationContext context = null;
	
    @Autowired
    private SqlSessionTemplate mybatisSessionTemplate;
    @Autowired
    private SessionFactory hibernateSessionFactory;
    
	private String level;
	private ORMHandle nextHandle;
	
	public AbstractORMHandle(String level){
		this.level = level;
	}
	
	/**
	 * 符合当前Level，则执行DB操作，否则交由下个Handle去执行
	 */
	public final Object handle(String level, ORMParamsDTO dto) throws Exception {
		if(this.level.equals(level)){
			return this.execute(dto);
		} else {
			this.setNextHandle();
			if(this.nextHandle != null){
				return this.nextHandle.handle(level, dto);
			} else {
				throw new Exception("必须设置ORMHandle处理当前请求");
			}
		}
	}
	
	/**
	 * 执行DB操作
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	protected abstract Object execute(ORMParamsDTO dto) throws Exception;
	
	/**
	 * 设置下个Handle，子类设置，如果是最后一个Handle，不设置
	 */
	public abstract void setNextHandle();

	protected SqlSessionTemplate getMybatisSessionTemplate() {
		return mybatisSessionTemplate;
	}

	protected SessionFactory getHibernateSessionFactory() {
		return hibernateSessionFactory;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		// TODO Auto-generated method stub
		context = applicationContext;
	}
	
	protected void setNextHandle(ORMHandle nextHandle){
		this.nextHandle = nextHandle;
	}

}

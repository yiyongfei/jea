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

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Component;

import com.ea.core.orm.handle.AbstractORMHandle;
import com.ea.core.orm.handle.ORMConstants;
import com.ea.core.orm.handle.ORMHandle;
import com.ea.core.orm.handle.dto.ORMParamsDTO;

/**
 * SQL执行，通过Hibernate完成，用于数据的新增或更新
 * @author yiyongfei
 *
 */
@Component
public class HibernateSqlORMHandle extends AbstractORMHandle {
    
	public HibernateSqlORMHandle() {
		super(ORMConstants.ORM_LEVEL.H_SQL.getCode());
	}

	@Override
	protected Object execute(ORMParamsDTO dto) throws Exception {
		// TODO Auto-generated method stub
		Session session = this.getHibernateSessionFactory().getCurrentSession();
		final ORMParamsDTO tmp = dto;
		session.doWork(new Work() {
			@SuppressWarnings("rawtypes")
			public void execute(Connection connection) throws SQLException {
				// �����Ѿ��õ�connection�ˣ����Լ������JDBC���롣
				// ע�ⲻҪclose�����connection��
				System.out.println("sql:"+tmp.getSqlid());
				PreparedStatement ps = connection.prepareStatement(tmp.getSqlid());
				if(tmp.getParam() != null){
					Object data = tmp.getParam();
					if(data instanceof Object[]){
						Object[] array = (Object[])data;
						int index = 1;
						for(Object obj : array){
							setParam(ps, index++, obj);
						}
						ps.execute();
					} else if (data instanceof Collection) {
						for(Object array : (Collection)data){
							if(array instanceof Object[]){
								int index = 1;
								for(Object obj : (Object[])array){
									setParam(ps, index++, obj);
								}
								ps.addBatch();
							} else {
								throw new SQLException("执行SQL时，参数请以Object[]的方式提供!");
							}
							
						}
						ps.executeBatch();
					} else {
						throw new SQLException("ͨ执行SQL时，如果是单条记录操作，参数请以Object[]的方式提供，如果多条记录操作，请提供Collection实例，实例里存放Object[]!");
					}
				}
				
			}
		});
        return null;
	}

	@Override
	public void setNextHandle() {
		ORMHandle nextHandle = (MybatisSqlORMHandle)this.context.getBean("mybatisSqlORMHandle");
		this.setNextHandle(nextHandle);
	}
	
	private void setParam(PreparedStatement ps, int index, Object param) throws SQLException{
		ps.setObject(index, param);
	}
}

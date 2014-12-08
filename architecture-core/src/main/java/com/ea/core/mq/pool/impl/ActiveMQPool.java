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
package com.ea.core.mq.pool.impl;

import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.activemq.pool.PooledConnectionFactory;

import com.ea.core.mq.pool.MQPool;

public class ActiveMQPool implements MQPool {
	private PooledConnectionFactory connectionPool;
	private String username;
	private String password;
	
	public ActiveMQPool(){
		maxTotal = 10;
		maxIdle = 5;
		maxWaitMillis = 500;
	}
	
	public void addMQPool(String url, String username, String password) {
		connectionPool = new PooledConnectionFactory(url);
		connectionPool.setMaxConnections(maxTotal);
		connectionPool.setMaximumActiveSessionPerConnection(maxIdle);
		connectionPool.setIdleTimeout(maxWaitMillis);
		this.username = username;
		this.password = password;
	}
	
	public Connection createConnection() throws JMSException {
		Connection connection = connectionPool.createConnection(username, password);
		connection.start();
		return connection;
	}
	
	public void close(Connection conn) throws JMSException {
		// TODO Auto-generated method stub
		if(conn != null){
			conn.close();
		}
	}
	
	public void stop() throws JMSException {
		connectionPool.stop();
	}
	
	public void setMaxTotal(int maxTotal) {
		// TODO Auto-generated method stub
		this.maxTotal = maxTotal;
	}

	public void setMaxIdle(int maxIdle) {
		// TODO Auto-generated method stub
		this.maxIdle = maxIdle;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		// TODO Auto-generated method stub
		this.maxWaitMillis = maxWaitMillis;
	}

	private int maxTotal;
	private int maxIdle;
	private int maxWaitMillis;
}

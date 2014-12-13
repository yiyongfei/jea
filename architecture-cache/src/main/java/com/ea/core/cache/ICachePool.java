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
package com.ea.core.cache;

/**
 * 缓存池，具体池参数参看IPoolConfig的说明
 * 
 * @author yiyongfei
 *
 */
public interface ICachePool {
	
	public void setMaxTotal(int maxTotal);
	
	public void setMaxIdle(int maxIdle);
	
	public void setMaxWaitMillis(long maxWaitMillis);
	
	public void setTestOnBorrow(boolean testOnBorrow);
	
	public void setTestOnReturn(boolean testOnReturn);
	
	public void setLifo(boolean lifo);
		
	public void setMaxConnectMillis(long maxConnectMillis);

	public void setEnableHealSession(boolean enableHealSession);

	public void setHealSessionInterval(long healSessionInterval);

	public void setFailureMode(boolean failureMode);
	
	public void addRedisServer(String address);
	
	public ICacheCommands getResource() throws Exception;
	
	public void returnResource(ICacheCommands jedis) throws Exception;

}

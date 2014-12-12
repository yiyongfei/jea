package com.ea.core.cache.redis.client;

import org.junit.Test;

import com.ea.core.cache.ICachePool;
import com.ea.core.cache.client.CacheClient;
import com.ea.core.cache.memcached.pool.MemcachedPool;
import com.ea.core.cache.redis.pool.impl.RedisGeneralPool;

public class Client {

	@Test
	public void test(){
		ICachePool cachePool = null;
		cachePool = new MemcachedPool();
		cachePool.addRedisServer("192.168.222.135:10240");
		CacheClient client = new CacheClient(cachePool);
		
		try {
			client.set("key", "value4345", 10);
			
			System.out.println(client.get("key"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

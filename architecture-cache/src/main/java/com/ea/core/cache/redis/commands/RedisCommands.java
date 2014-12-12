package com.ea.core.cache.redis.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ShardedJedis;

import com.ea.core.cache.CacheConstants;
import com.ea.core.cache.ICacheCommands;

public class RedisCommands implements ICacheCommands {

	private JedisCommands commands;
	public RedisCommands(JedisCommands commands){
		this.commands = commands;
	}
	@Override
	public Boolean set(String key, String value, int seconds) {
		// TODO Auto-generated method stub
		String result = null;
		if(seconds > 0){
			result = commands.setex(key, seconds, value);
		} else {
			result = commands.set(key, value);
		}
		return CacheConstants.REDIS_RESULT.SET_RESULT.getCode().equals(result) ? true : false;
	}
	
	@Override
	public Boolean add(String key, String value, int seconds) throws Exception {
		String result = null;
		if(seconds > 0){
			result = commands.set(key, value, "NX", "EX", seconds);
		} else {
			result = commands.set(key, value, "NX", "EX", -1);
		}
		return CacheConstants.REDIS_RESULT.SET_RESULT.getCode().equals(result) ? true : false;
	}

	@Override
	public Boolean replace(String key, String value, int seconds) throws Exception {
		String result = null;
		if(seconds > 0){
			result = commands.set(key, value, "XX", "EX", seconds);
		} else {
			result = commands.set(key, value, "XX", "EX", -1);
		}
		return CacheConstants.REDIS_RESULT.SET_RESULT.getCode().equals(result) ? true : false;
	}
	
	@Override
	public Boolean expire(String key, int seconds) {
		// TODO Auto-generated method stub
		Long num = commands.expire(key, seconds);
		return num == 1 ? true : false;
	}

	@Override
	public Set<String> keys(String pattern) {
		// TODO Auto-generated method stub
		if (commands instanceof Jedis) {
			return ((Jedis)commands).keys(pattern);
		} else if (commands instanceof ShardedJedis) {
			Set<String> set = new HashSet<String>();
			ShardedJedis shardedJedis = (ShardedJedis)commands;
			Collection<Jedis> jediss = shardedJedis.getAllShards();
			for(Jedis jedis : jediss){
				set.addAll(jedis.keys(pattern));
			}
			return set;
		} else {
			return null;
		}
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		return commands.get(key);
	}

	@Override
	public Boolean exists(String key) {
		// TODO Auto-generated method stub
		return commands.exists(key);
	}

	@Override
	public Boolean delete(String key) {
		// TODO Auto-generated method stub
		Long num = commands.del(key);
		return num > 0 ? true : false;
	}

	@Override
	public Boolean append(String key, String value) {
		// TODO Auto-generated method stub
		Long num = commands.append(key, value);
		return num > 0 ? true : false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getCommands() {
		// TODO Auto-generated method stub
		return this.commands;
	}

}

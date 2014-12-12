package com.ea.core.cache.memcached.commands;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;

import com.ea.core.cache.ICacheCommands;

public class MemcachedCommands implements ICacheCommands {
	private MemcachedClient commands;
	private List<InetSocketAddress> socketAddress;
	
	public MemcachedCommands(MemcachedClient commands, List<InetSocketAddress> socketAddress){
		this.commands = commands;
		this.socketAddress = socketAddress;
	}
	@Override
	public Boolean set(String key, String value, int seconds) throws Exception {
		// TODO Auto-generated method stub
		return commands.set(key, seconds, value);
	}

	@Override
	public Boolean add(String key, String value, int seconds) throws Exception {
		return commands.add(key, seconds, value);
	}
	
	@Override
	public Boolean replace(String key, String value, int seconds) throws Exception {
		return commands.replace(key, seconds, value);
	}
	
	@Override
	public Boolean expire(String key, int seconds) throws Exception {
		// TODO Auto-generated method stub
		return commands.touch(key, seconds);
	}

	@Override
	public Set<String> keys(String pattern) throws Exception {
		// TODO Auto-generated method stub
		Set<String> set = new HashSet<String>();
		for(InetSocketAddress address : socketAddress) {
			KeyIterator iterator = commands.getKeyIterator(address);
			while(iterator.hasNext()){
				set.add(iterator.next());
			}
		}
		return set;
	}

	@Override
	public String get(String key) throws Exception {
		// TODO Auto-generated method stub
		return commands.get(key);
	}

	@Override
	public Boolean exists(String key) throws Exception {
		// TODO Auto-generated method stub
		String str = commands.get(key);
		return str != null ? true : false;
	}

	@Override
	public Boolean delete(String key) throws Exception {
		// TODO Auto-generated method stub
		return commands.delete(key);
	}

	@Override
	public Boolean append(String key, String value) throws Exception {
		// TODO Auto-generated method stub
		return commands.append(key, value);
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		commands.shutdown();
	}
	@Override
	public Object getCommands() {
		// TODO Auto-generated method stub
		return this.commands;
	}

}

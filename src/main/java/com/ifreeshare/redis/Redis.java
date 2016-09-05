package com.ifreeshare.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Redis {
	
	private static JedisPool jedisPool = null;
	
	static{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(20);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}
	
	
	
	public boolean hPut(String key, String field, String value){
		boolean flag = false;
		Jedis jedis = jedisPool.getResource();
		if(jedis != null){
			long id = jedis.hset(key, field, value);
		}
		return false;
	}
	
	
	
	public boolean hput(String key,String field){
		Jedis jedis = jedisPool.getResource();
		return  jedis.hexists(key, field);
	}

}

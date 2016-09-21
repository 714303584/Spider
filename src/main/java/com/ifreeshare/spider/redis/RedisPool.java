package com.ifreeshare.spider.redis;

import io.vertx.core.json.JsonObject;

import java.net.MalformedURLException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ifreeshare.spider.http.HttpUtil;

public class RedisPool {
	
	
private static  JedisPool jedisPool = null;
	
	
	static{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(10);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}


	public static boolean fieldExist(String key,String field) {
		boolean flag = false;
		Jedis jedis = jedisPool.getResource();
		flag = jedis.hexists(key, field);
		if(jedis != null){
			jedis.close();
		}
		return flag;
	}

	public static long delfield(String keys,String... field) {
		long flag = 0L;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			flag = jedis.hdel(keys, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return flag;
	}
	
	
	public static long delKey(String key) {
		Long flag = 0L;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			flag = jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return flag;
	}

	public static boolean addfield(String key, String field,String value) {
		boolean flag = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return flag;
	}

	public static String  getFieldValue(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return null;
	}
	

}

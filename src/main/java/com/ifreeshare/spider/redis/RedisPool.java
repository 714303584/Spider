package com.ifreeshare.spider.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis connection pool 
 * @author zhuss
 * @date 2016-10-21-3:55:15
 * @description Redis connection pool 
 */
public class RedisPool {
	
	/**
	 * Jedis connection pool
	 */
	private static  JedisPool jedisPool = null;
	
	
	static{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(10);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}

	/**
	 * Exist Key 
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean fieldExist(String key,String field) {
		boolean flag = false;
		Jedis jedis = null;
		try {
			jedis  = jedisPool.getResource();
			flag = jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				jedis.close();
			}
		}
		return flag;
	}
	
	/**
	 * Delete A Data Of Redis
	 * @param keys 
	 * @param field
	 * @return
	 */
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
	
	/**
	 * Delete A Key Of Redis
	 * @param key  To Remove The Key
	 * @return How Many Deleted, 1 success.
	 */
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

	
	/**
	 * Add a data to HSET Redis
	 * @param key The Key Of Redis
	 * @param field The Key Of Data in HSET.
	 * @param value The Value Of Data;
	 * @return  Success is True , Failed is False
	 */
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

	/**
	 * To Obtain A data By HSET
	 * @param key
	 * @param field
	 * @return
	 */
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

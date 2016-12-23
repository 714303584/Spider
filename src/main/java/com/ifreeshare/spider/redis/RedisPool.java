package com.ifreeshare.spider.redis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

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
	
	private static JedisCluster jedisCluster = null;
	
	static{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(10);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
		Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
		hostAndPorts.add(new HostAndPort("127.0.0.1", 6379));
		jedisCluster = new JedisCluster(hostAndPorts);
	}

	/**
	 * Exist Key 
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean hExist(String key,String field) {
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
	public static boolean hSet(String key, String field,String value) {
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
	public static String  hGet(String key, String field) {
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
	
	
	
	/**
	 * Get The Size of Len
	 * @param key
	 */
	public static long hLen(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return 0;
	}
	
	
	public static ScanResult<Map.Entry<String, String>> hScan(String key, String cursor,int count){
		
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			 ScanParams params = new ScanParams();
			 params.count(count);
			ScanResult<Map.Entry<String, String>> sr = 
				jedis.hscan(key, cursor,params);
			Iterator<Map.Entry<String, String>> it = sr.getResult().iterator();
			System.out.println("sr.getStringCursor():"+sr.getStringCursor());
//			while (it.hasNext()) {
//				Map.Entry<String, String> entity = it.next();
//				System.out.println("entity.getKey():"+entity.getKey());
//				System.out.println("entity.getValue():"+entity.getValue());
//			}
//			
			return sr;
//			return jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return null;
	}
	
	
//	public static String getKey(String key){
//		
//	}

	/**
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<String> pageList(String key, int start, int end){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(jedis != null){
				jedis.close();
			}
		}
		return null;
	}
	
	
	
//	public static List<String> addList(String key, int start, int end){
//		Jedis jedis = null;
//		try {
//			jedis = jedisPool.getResource();
//			return jedis.l(key, start, end);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			if(jedis != null){
//				jedis.close();
//			}
//		}
//		return null;
//	}
	
	
	
}

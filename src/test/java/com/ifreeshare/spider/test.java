package com.ifreeshare.spider;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import io.vertx.core.json.JsonObject;

import com.ifreeshare.spider.verticle.validate.MainVerticleValidate;
import com.ifreeshare.spider.verticle.validate.RedisValidate;


public class test {
	
	MainVerticleValidate validate = new RedisValidate();
	
	public static void main(String[] args) {
		  JedisPool jedisPool = null;
		
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(10);
			config.setMaxIdle(5);
			config.setMaxWaitMillis(1000l);
			config.setTestOnBorrow(false);
			jedisPool = new JedisPool(config, "127.0.0.1", 6379);

		
		test t = new test();
		
		for (int i = 0; i < 100; i++) {
			t.validate.addOrUpdateUrl("http://wenku.baidu.com/link?url=fy"+Thread.currentThread().getName()+i, new JsonObject().put("inter", 1));
			
			
//			Jedis jedis = jedisPool.getResource();
//			jedis.hset("zhushunshan", "key_"+i, "value_"+i);
//			jedis.close();
			try {
				
				System.out.println(i++);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		for (int i = 0; i < 20; i++) {
//			
//			
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					int count = 0;
//					while (true) {
//						t.validate.addOrUpdateUrl("http://wenku.baidu.com/link?url=fy"+Thread.currentThread().getName()+count, new JsonObject().put("inter", 1));
//						try {
//							System.out.println(count++);
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}).start();
//			
//			
//			
//		}
	}

}

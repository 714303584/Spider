package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.logging.log4j.Logger;


import redis.clients.jedis.ScanResult;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.Channels.OverflowPolicy;


import com.ifreeshare.spider.Runner;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;
import com.ifreeshare.spider.verticle.msg.MessageType;
import com.ifreeshare.spider.verticle.validate.MainVerticleValidate;
import com.ifreeshare.spider.verticle.validate.RedisValidate;

public class SpiderMainVerticle extends AbstractVerticle  {

	private static final Logger logger  = Log.register(SpiderMainVerticle.class.getName());
	
	public static final String MAIN_ADDRESS = "com.ifreeshare.spider.verticle.SpiderMainVerticle";
//	private static Set<String> urls = new HashSet<String>();
	
	MainVerticleValidate validate = new RedisValidate();
	
	private int grabFrequency = 3;
	
	private long grabTime = 60000;
	
	
	private Map<String, JsonObject> works = new HashMap<String, JsonObject>();
	
//	private Map<String, JsonObject> waitQueue = new HashMap<String,JsonObject>();
	
	Channel<String> urlChannel = Channels.newChannel(100000,OverflowPolicy.BLOCK);
	
	public SpiderMainVerticle(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
		
	}
	
	@Override
	public void start() throws Exception {
		
		vertx.eventBus().consumer(MAIN_ADDRESS,msg -> {
			JsonObject mbody = (JsonObject) msg.body();
			
			int type = mbody.getInteger(MessageType.MESSAGE_TYPE);
			JsonObject body = mbody.getJsonObject(MessageType.MESSAGE_BODY);
			switch (type) {
			case MessageType.URL_DISTR:
				urlCheck(mbody);
				break;
			case MessageType.SUCC_URL:
				succUrl(body);
				break;
			case MessageType.Fail_URL:
				failUrl(body);
				break;
			default:
				break;
			}
			
		});
		
		
		System.out.println(this.context.get(Runner.BASE_ARRAY));
		
		JsonArray baseUrls = this.context.get(Runner.BASE_ARRAY);
		
		for (int i = 0; i < baseUrls.size(); i++) {
			String baseUrl = baseUrls.getString(i);
			JsonObject message = createMessage(MessageType.URL_DISTR, new JsonObject().put("url", baseUrl));
//			waitQueue.put(baseUrl, message);
			putCache(baseUrl, message);
			urlChannel.send(baseUrl);
			
			
//			vertx.eventBus().send(SpiderHeaderVerticle.WORKER_ADDRESS, message);
		}
		
		
		Fiber urlDistributeFiber = new Fiber(() -> {
			int fullTimes = 0;
			String url = null;
			Map<String, JsonObject> cache = new HashMap<String,JsonObject>();
			while ((url = urlChannel.receive()) != null) {
				try {
					if(fullTimes >= 10){
						works.clear();
						fullTimes = 0;
					}
					if(works.size() >= grabFrequency){
						Fiber.sleep(grabTime);
						fullTimes++;
						urlChannel.send(url);
						continue;
					}
//					JsonObject info =  waitQueue.get(url);
					
					JsonObject info = cache.remove(url);
					if(info == null){
						info =  getCache(url);
					}
					works.put(url, info);
					delCache(url);
//					waitQueue.remove(url);
					
					vertx.eventBus().send(SpiderHeaderVerticle.WORKER_ADDRESS, info);
					Log.log(logger, Level.DEBUG, "url Distribute Fiber ----------------------------- url:%s;   body:%s", url,info);
					Log.log(logger, Level.DEBUG, "url Distribute Fiber -----------------------------cache ---->size:%d", cache.size());
					
					if(cache.size() == 0){
						ScanResult<Map.Entry<String, String>> sr = RedisPool.hScan(CoreBase.FIND_NEW_URL_BUT_NO_GRAB_AND_CACHE_IFREESHARE_COM, "0", 1000);
						List<Map.Entry<String, String>> entrys = sr.getResult();
						Iterator<Map.Entry<String, String>> it = entrys.iterator();
						while (it.hasNext()) {
							Map.Entry<String, String> entry = it.next();
							String key = entry.getKey();
							String value = entry.getValue();
							urlChannel.send(key);
							cache.put(key, new JsonObject(value));
//							cache.get(new JsonObject(value));
							Log.log(logger, Level.DEBUG, "put in cache ----------------------------- url:%s;   body:%s", key,value);
						}
					}
//					if(RedisPool.h)
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		urlDistributeFiber.start();
		
	}





	private void failUrl(JsonObject body) {
		
	}

	private void succUrl(JsonObject body) {
		String url = body.getString(HttpUtil.URL);
		validate.addOrUpdateUrl(url, body);
		works.remove(url);
		Log.log(logger, Level.DEBUG, "success ----------------------------- url:%s;   body:%s", url,body);
	}

	private void succUrl(String string) {
		
	}




	@Override
	public void init(Vertx vertx, Context context) {
		
	}
	
	public JsonObject createMessage(int type ,JsonObject body){
		JsonObject message = new JsonObject();
		message.put(MessageType.MESSAGE_TYPE, type);
		message.put(MessageType.MESSAGE_BODY, body);
		return message;
	}
	
	
	
	
	/**
	 * Verify whether URL has been crawled 
	 * @param message Received URL information 
	 */
	public void urlCheck(JsonObject message){
		JsonObject body =  message.getJsonObject(MessageType.MESSAGE_BODY);
		String url = body.getString(HttpUtil.URL);
		if(!validate.urlExist(url) && !works.containsKey(url) && !inCache(url)){
			try {
				urlChannel.send(url);
				putCache(url, message);
//				waitQueue.put(url, message);
			} catch (Exception e) {
				Log.log(logger, Level.DEBUG, "urlChannel new  ----------------------------- url:%s;   body:%s", url,body);			
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Does it exist in the cache 
	 * @param url
	 */
	public boolean inCache(String url){
		return RedisPool.hExist(CoreBase.FIND_NEW_URL_BUT_NO_GRAB_AND_CACHE_IFREESHARE_COM, url);
	}
	
	
	public void putCache(String url, JsonObject message){
		RedisPool.hSet(CoreBase.FIND_NEW_URL_BUT_NO_GRAB_AND_CACHE_IFREESHARE_COM, url, message.toString());
	}
	
	public void delCache(String url){
		RedisPool.delfield(CoreBase.FIND_NEW_URL_BUT_NO_GRAB_AND_CACHE_IFREESHARE_COM, url);
	}
	
	public JsonObject getCache(String url){
		String result = RedisPool.getFieldValue(CoreBase.FIND_NEW_URL_BUT_NO_GRAB_AND_CACHE_IFREESHARE_COM, url);
		if(result != null){
			return new JsonObject(result);
		}
		return null;
	}
	
	public long getCacheLen(){
		return RedisPool.hLen(CoreBase.FIND_NEW_URL_BUT_NO_GRAB_AND_CACHE_IFREESHARE_COM);
	}
	
}

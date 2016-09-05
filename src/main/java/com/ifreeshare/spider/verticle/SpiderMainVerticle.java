package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.spider.Runner;
import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.verticle.msg.MessageType;

public class SpiderMainVerticle extends AbstractVerticle  {

	private static final Logger logger  = Log.register(SpiderMainVerticle.class.getName());
	
	public static final String MAIN_ADDRESS = "com.ifreeshare.spider.verticle.SpiderMainVerticle";
	private static Set<String> urls = new HashSet<String>();
	
	
	
	private Map<String, JsonObject> works = new HashMap<String, JsonObject>();
	
	
	public SpiderMainVerticle(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
		urls.add("");
		urls.add(null);
		
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
			vertx.eventBus().send(SpiderHeaderVerticle.WORKER_ADDRESS, message);
		}
		
		vertx.setPeriodic(5000, id -> {
			System.out.println("size---"+urls.size());
		});
	}

	
	//	
//	public void urlDistribution(String url){
//		vertx.eventBus().send(SpiderWorkerVerticle.WORKER_ADDRESS, url);
//	}




	private void failUrl(JsonObject body) {
		
	}




	private void succUrl(JsonObject body) {
		String url = body.getString(HttpUtil.URL);
		urls.add(url);
		works.remove(works);
		Log.log(logger, Level.WARN, "success ----------------------------- url:%s;   body:%s", url,body);
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
	
	
	
	
	public void urlCheck(JsonObject message){
		
		JsonObject body =  message.getJsonObject(MessageType.MESSAGE_BODY);
		
		String url = body.getString(HttpUtil.URL);
		
		if(!urls.contains(url) && !works.containsKey(url)){
			works.put(url, body);
			vertx.eventBus().send(SpiderHeaderVerticle.WORKER_ADDRESS, message);
			Log.log(logger, Level.WARN, "new  ----------------------------- url:%s;   body:%s", url,body);
		}
		
		
		
		
	}
	
	
	
	

}

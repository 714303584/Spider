package com.ifreeshare.spider.verticle;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import antlr.debug.Event;
import co.paralleluniverse.asm.Type;

import com.ifreeshare.spider.App;
import com.ifreeshare.spider.verticle.msg.MessageType;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class SpiderMainVerticle extends AbstractVerticle  {

	
	public static final String MAIN_ADDRESS = "com.ifreeshare.spider.verticle.SpiderMainVerticle";
	private static Set<String> urls = new HashSet<String>();
	public SpiderMainVerticle(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
		
	}

	
	
	
	@Override
	public void start() throws Exception {
		
		vertx.eventBus().consumer(MAIN_ADDRESS,msg -> {
			JsonObject mbody = (JsonObject) msg.body();
			
			int type = mbody.getInteger(MessageType.MESSAGE_TYPE);
			Object body = mbody.getValue(MessageType.MESSAGE_BODY);
			switch (type) {
			case MessageType.NEW_URL:
				urlCheck(body.toString());
				break;
			default:
				break;
			}
			
		});
		
		
		System.out.println(this.context.get(App.BASE_ARRAY));
		
		JsonArray baseUrls = this.context.get(App.BASE_ARRAY);
		
		for (int i = 0; i < baseUrls.size(); i++) {
			String baseUrl = baseUrls.getString(i);
			JsonObject message = createMessage(MessageType.URL_DISTR, new JsonObject().put("url", baseUrl));
			vertx.eventBus().send(SpiderWorkerVerticle.WORKER_ADDRESS, message);
		}
		
		vertx.setPeriodic(5000, id -> {
			System.out.println("size---"+urls.size());
		});
	}

	
	//	
//	public void urlDistribution(String url){
//		vertx.eventBus().send(SpiderWorkerVerticle.WORKER_ADDRESS, url);
//	}




	@Override
	public void init(Vertx vertx, Context context) {
		
	}
	
	public JsonObject createMessage(int type ,JsonObject body){
		JsonObject message = new JsonObject();
		message.put(MessageType.MESSAGE_TYPE, type);
		message.put(MessageType.MESSAGE_BODY, body);
		return message;
	}
	
	
	
	public void urlCheck(String url){
//		String regex = "([\\w-]+\\.)+.taobao.com([\\w-]+\\.)+$";
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher(url);
		
		if(url.contains(".taobao.com")){
			if(url.startsWith("/")&& !urls.contains(url)){
				urls.add(url);
				String url2 = "https:"+url;
				vertx.eventBus().send(SpiderWorkerVerticle.WORKER_ADDRESS, createMessage(MessageType.URL_DISTR, new JsonObject().put("url", url2)));
			}
		}
		
	}
	
	
	
	

}

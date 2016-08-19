package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;


import java.util.Iterator;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import co.paralleluniverse.fibers.Fiber;


import com.ifreeshare.spider.jsoup.JsoupUtil;
import com.ifreeshare.spider.verticle.msg.MessageType;

public class SpiderWorkerVerticle extends AbstractVerticle {
	
//	FiberScheduler fs = null;

	public static final String WORKER_ADDRESS = "com.ifreeshare.spider.verticle.SpiderWorkerVerticle";
	
	

	public SpiderWorkerVerticle(Vertx vertx , Context context) {
		this.vertx = vertx;
		this.context = context;
//		fs =  new  FiberForkJoinScheduler("scheduler-worker", 10000);
		
	}
	
	
	@Override
	public void start() throws Exception {
		
		vertx.eventBus().consumer(WORKER_ADDRESS, message -> {
			JsonObject mbody = (JsonObject) message.body();
			processor(mbody);
		});
		
		
	}

	

	@Override
	public void init(Vertx vertx, Context context) {
	}


	private void  processor(JsonObject message) {
		int type  =  message.getInteger(MessageType.MESSAGE_TYPE);
		JsonObject body = message.getJsonObject(MessageType.MESSAGE_BODY);
		switch (type) {
		case MessageType.URL_DISTR:
			processUrl(body);
			break;

		default:
			break;
		}

	}


	private void processUrl(JsonObject body) {
		new Fiber(() -> {
			try {
				String url = body.getString("url");
				Document doc = Jsoup.connect(url).get();
				Elements links = doc.getElementsByTag(JsoupUtil.LINK_A);
				Iterator<Element> eleIt = links.iterator();
				while(eleIt.hasNext()){
					Element a = eleIt.next();
					String href = a.attr(JsoupUtil.LINK_A_HREF);
					JsonObject newURl = new JsonObject();
					newURl.put(MessageType.MESSAGE_TYPE, MessageType.NEW_URL);
					newURl.put(MessageType.MESSAGE_BODY, href);
					vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, newURl);
				}
				
				if(url.contains("item.taobao.com")){
					System.out.println(url);
					String title = doc.title();
					System.out.println(title);
					String[] urlSplit =  url.split("?");
					if(urlSplit.length > 1){
						String[] params = url.split("&");
						String id = null;
						for (int i = 0; i < params.length; i++) {
							String param = params[i];
							if(param.startsWith("id=")){
								 id =  param.split("=")[1];
								 break;
							}
							
							if(id != null){
								String iteminfourl = "https://detailskip.taobao.com/service/getData/1/p2/item/detail/sib.htm?itemId="+id
										+ "&modules=qrcode,viewer,price,contract,duty,xmpPromotion,dynStock,delivery,upp,activity,fqg,zjys,coupon&callback=onSibRequestSuccess";
								
								Document priceDoc = Jsoup.connect(iteminfourl).header("Referer", "https://item.taobao.com/item.htm?id=534016703208").get();
								System.out.println(priceDoc.html());
							}
						}
						
						
//					  	Elements metas = doc.getElementsByTag("meta");
//					  	Elements keywords = metas.attr("name","keywords"); 
//					  	Element keyword =  keywords.get(0);
//					  	System.out.println(keyword.html());
					}
					
					
					
				  	
				  	
					
					
					
					
				}
				
			} catch (Exception e) {
				System.out.println(body);
				e.printStackTrace();
			}
		}).start();
	}
	
	
	
	
	
	
	
	

}

package com.ifreeshare.spider;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.persistence.elasticsearch.ElasticSearchPersistence;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.SpiderHttpServer;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.verticle.PersistenceVertical;
import com.ifreeshare.spider.verticle.SpiderAdminHttpVerticle;
import com.ifreeshare.spider.verticle.SpiderFileVerticle;
import com.ifreeshare.spider.verticle.SpiderHeaderVerticle;
import com.ifreeshare.spider.verticle.SpiderHtmlVerticle;
import com.ifreeshare.spider.verticle.SpiderImageVerticle;
import com.ifreeshare.spider.verticle.SpiderMainVerticle;

/**
 * @author zhuss
 * @date 2016-11-3-PM6:55:21
 * @description  Used to start all modules and partial configuration initialization 
 */
public class Runner {
	private static Logger logger = Log.register(Runner.class.getName());
	
	public static Vertx vertx = null;

	public final static String BASE_ARRAY = "Base_URLs";

	public final static String PASS_ARRAY = "Pass_URLs";
	
	public static final String defaultConfigPah = "/etc/ifreeshare/spider-config.xml";

	static {
		
		System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers",
				"false");
	}
	

	public static void main(String[] args) {
		try {
			vertx = Vertx.vertx();
			CoreBase.vertx = vertx;
			Context context = vertx.getOrCreateContext();
			
			Configuration.load(defaultConfigPah, SpiderHttpServer.class.getResource("/spider-config.xml").getPath());
			
			JsonArray baseUrls = new JsonArray();
			baseUrls.add("https://alphacoders.com/");
//			baseUrls.add("http://www.jb51.net/");

			JsonArray regular = new JsonArray();
			regular.add("^[.]+.taobao.com([\\w/]+)\\.$");
			regular.add("^[.]+alphacoders.com([\\w/]+)\\.$");

			context.put(BASE_ARRAY, baseUrls);
			context.put(PASS_ARRAY, regular);
			
			ElasticSearchPersistence persistence = new ElasticSearchPersistence();

			vertx.deployVerticle(new SpiderHeaderVerticle(vertx, context));
			vertx.deployVerticle(new SpiderHtmlVerticle(vertx, context));
			vertx.deployVerticle(new SpiderImageVerticle(vertx, context));
			vertx.deployVerticle(new SpiderFileVerticle(vertx, context));
			vertx.deployVerticle(new PersistenceVertical(vertx, context, persistence));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

			vertx.deployVerticle(new SpiderMainVerticle(vertx, context));
			vertx.deployVerticle(new SpiderAdminHttpVerticle(vertx, context));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	
	}
	
	
	/**
	 * Initialize global configuration file 
	 * @param context  Global Context   Use In Verticle Constructor  and Initialization method will Replace This(context).
	 */
	public static void init(Context context){
		
	}
}

package com.ifreeshare.spider;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.verticle.SpiderFileVerticle;
import com.ifreeshare.spider.verticle.SpiderHeaderVerticle;
import com.ifreeshare.spider.verticle.SpiderHtmlVerticle;
import com.ifreeshare.spider.verticle.SpiderImageVerticle;
import com.ifreeshare.spider.verticle.SpiderMainVerticle;

/**
 * Hello world!
 *
 */
public class Runner {
	private static Logger logger = Log.register(Runner.class.getName());

	public final static String BASE_ARRAY = "Base_URLs";

	public final static String PASS_ARRAY = "Pass_URLs";

	static {
		System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers",
				"false");
	}

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();
		Context context = vertx.getOrCreateContext();
		
		String defaultConfigPah = "/etc/ifreeshare/spider-config.xml";
		
		

		JsonArray baseUrls = new JsonArray();
		 baseUrls.add("https://alphacoders.com/");
//		baseUrls.add("http://www.jb51.net/");

		JsonArray regular = new JsonArray();
		regular.add("^[.]+.taobao.com([\\w/]+)\\.$");
		regular.add("^[.]+alphacoders.com([\\w/]+)\\.$");

		context.put(BASE_ARRAY, baseUrls);
		context.put(PASS_ARRAY, regular);

		vertx.deployVerticle(new SpiderHeaderVerticle(vertx, context));
		vertx.deployVerticle(new SpiderHtmlVerticle(vertx, context));
		vertx.deployVerticle(new SpiderImageVerticle(vertx, context));
		vertx.deployVerticle(new SpiderFileVerticle(vertx, context));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		vertx.deployVerticle(new SpiderMainVerticle(vertx, context));

	}
}

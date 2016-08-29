package com.ifreeshare.spider;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;

import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.taskdefs.Sleep;

import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.verticle.SpiderHeaderVerticle;
import com.ifreeshare.spider.verticle.SpiderHtmlVerticle;
import com.ifreeshare.spider.verticle.SpiderMainVerticle;


/**
 * Hello world!
 *
 */
public class Runner 
{
	private static  Logger logger  = Log.register(Runner.class.getName());
	
	public final static String BASE_ARRAY = "Base_URLs";
	
	public final static String PASS_ARRAY = "Pass_URLs";
	
    public static void main( String[] args )
    {
       
    	System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers","false");
    	
    	
    	Vertx vertx = Vertx.vertx();
    	Context context = vertx.getOrCreateContext();
    	
    	JsonArray baseUrls = new JsonArray();
//    	baseUrls.add("https://alphacoders.com/");
    	baseUrls.add("http://www.jb51.net/");
    	
    	
    	JsonArray regular = new JsonArray();
    	regular.add("^[.]+.taobao.com([\\w/]+)\\.$");
    	regular.add("^[.]+alphacoders.com([\\w/]+)\\.$");
    	
    	
    	context.put(BASE_ARRAY, baseUrls);
    	context.put(PASS_ARRAY, regular);
    	
    	vertx.deployVerticle(new SpiderHeaderVerticle(vertx, context));
    	vertx.deployVerticle(new SpiderHtmlVerticle(vertx, context));
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	
    	vertx.deployVerticle(new SpiderMainVerticle(vertx, context));
    
    	
    	
    	
    	
    }
}

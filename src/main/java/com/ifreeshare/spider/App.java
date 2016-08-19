package com.ifreeshare.spider;

import com.ifreeshare.spider.verticle.SpiderMainVerticle;
import com.ifreeshare.spider.verticle.SpiderWorkerVerticle;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;


/**
 * Hello world!
 *
 */
public class App 
{
	
	public final static String BASE_ARRAY = "Base_URLs";
	
	public final static String PASS_ARRAY = "Pass_URLs";
	
    public static void main( String[] args )
    {
       
    	System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers","false");
    	
    	
    	Vertx vertx = Vertx.vertx();
    	Context context = vertx.getOrCreateContext();
    	
    	JsonArray baseUrls = new JsonArray();
    	baseUrls.add("https://www.taobao.com");
    	
    	JsonArray regular = new JsonArray();
    	regular.add("^[.]+.taobao.com([\\w/]+)\\.$");
    	
    	
    	context.put(BASE_ARRAY, baseUrls);
    	context.put(PASS_ARRAY, regular);
    	vertx.deployVerticle(new SpiderWorkerVerticle(vertx, context));
    	
    	vertx.deployVerticle(new SpiderMainVerticle(vertx, context));
    	
    	
    	
    	
    }
}

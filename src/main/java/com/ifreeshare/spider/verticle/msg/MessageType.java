package com.ifreeshare.spider.verticle.msg;

import io.vertx.core.json.JsonObject;

public class MessageType {
	
	public static final int URL_DISTR = 1 ;
	public static final int NEW_URL = 2;
	public static final int SUCC_URL = 3;
	
	public static final int Fail_URL = 4;
	
	
	public static final String MESSAGE_TYPE = "type";
	
	public static final String MESSAGE_BODY = "body";
	
	
	
//	public JsonObject createMessage(int type,JsonObject body){
//		
//		
//		
//		
//		
//	}
	

}

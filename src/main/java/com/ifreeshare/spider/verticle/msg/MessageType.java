package com.ifreeshare.spider.verticle.msg;

/**
 * Message sent to EventBuss 
 * @author zhuss
 */
public class MessageType {
	
	// Message type  
	public static final int URL_DISTR = 1 ;
	public static final int NEW_URL = 2;
	public static final int SUCC_URL = 3;
	public static final int Fail_URL = 4;
	public static final int KEYWORD_REPLACE = 5;
	public static final int KEYWORD_REMOVE = 6;
	// Message type  
	
	
	
	public static final String MESSAGE_TYPE = "type";
	
	public static final String MESSAGE_BODY = "body";

}

package com.ifreeshare.spider.verticle.validate;

import io.vertx.core.json.JsonObject;

public interface MainVerticleValidate {
	
	public boolean addOrUpdateUrl(String url,JsonObject info);
	
	public boolean urlExist(String url);
	
//	public JsonObject getUrlInfo(String url);
	
	public boolean delUrl(String url);

}

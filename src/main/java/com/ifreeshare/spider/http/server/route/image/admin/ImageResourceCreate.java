package com.ifreeshare.spider.http.server.route.image.admin;

import java.util.UUID;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.redis.RedisPool;
import com.ifreeshare.spider.verticle.PersistenceVertical;

public class ImageResourceCreate extends BaseRoute {

	public ImageResourceCreate() {
		super("/admin/image/resource/create/", BaseRoute.POST, "templates/images/admin/keyword_replace.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		
		String keywords = request.getParam(CoreBase.HTML_KEYWORDS);
		String title = request.getParam(CoreBase.HTML_TITLE);
		String description = request.getParam(CoreBase.HTML_DESCRIPTION);
		String path = request.getParam(CoreBase.PATH);
		String thumbnail = request.getParam(CoreBase.DOC_THUMBNAIL);
		
		String resouceInfo =  RedisPool.hGet(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_RESOURCE_KEY_IFREESHARE_COM, title);
		
		if(resouceInfo != null){
			response.end("exist");
		}else{
			String uuid = UUID.randomUUID().toString();
			JsonObject json = new JsonObject();
			json.put(CoreBase.HTML_KEYWORDS, keywords);
			json.put(CoreBase.HTML_DESCRIPTION, description);
			json.put(CoreBase.DOC_THUMBNAIL, thumbnail);
			json.put(CoreBase.HTML_TITLE, title);
			json.put(CoreBase.PATH, path);
			json.put(CoreBase.INDEX, CoreBase.IMAGES);
			json.put(CoreBase.TYPE, CoreBase.RESOURCES);
			json.put(CoreBase.OPERATE, CoreBase.OPERATE_I);
			
			vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, json);
			response.end("ok");	
//			if(RedisPool.hSet(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_RESOURCE_KEY_IFREESHARE_COM, uuid, json.toString())){
//				response.end("ok");	
//			}else{
//				response.end("field");	
//			}
		}
	}
	
	
	
	public static void main(String[] args) {
		
		
		
	}
}

package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;

public class ShowResouceRouter extends BaseRoute {
	
	public ShowResouceRouter() {
		super("/public/show/image/resource/", BaseRoute.GET, "templates/images/rshow.ftl");
	}


	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam("id");
		
		String info = RedisPool.hGet(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_RESOURCE_KEY_IFREESHARE_COM,id);
		
		JsonObject docJson = new JsonObject(info);
		String keywords = docJson.getString(CoreBase.HTML_KEYWORDS);
		
		PageDocument doc = new PageDocument();
		doc.setUuid(docJson.getString(CoreBase.UUID));
		doc.setKeywords(keywords);
		doc.setDescription(docJson.getString(CoreBase.HTML_DESCRIPTION));
		doc.setName(docJson.getString(CoreBase.HTML_TITLE));
		doc.setTitle(docJson.getString(CoreBase.HTML_TITLE));
		doc.setSrc(docJson.getString(CoreBase.PATH));
		
		Log.log(logger, Level.DEBUG, "router[%s],image[%s]", this.getUrl(), docJson);
	 	File file = new File("G:\\nginx-1.9.4\\html\\iresource\\"+doc.getSrc());
	 	File[] childs = file.listFiles();
	 	
	 	for (int i = 0; i < childs.length; i++) {
	 		File child = childs[i];
	 		String name = child.getName();
	 		doc.getTags().add("/iresource/"+doc.getSrc()+"/"+name);
		}
		context.put("doc", doc);
		render(context);
	}
	
	
	
	
	
}

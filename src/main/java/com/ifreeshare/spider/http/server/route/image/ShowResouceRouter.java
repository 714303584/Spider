package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;

public class ShowResouceRouter extends BaseRoute {
	
	IDataSearch<JsonObject> search = IDataSearch.instance();
	public ShowResouceRouter() {
		super("/public/show/image/resource/", BaseRoute.GET, "templates/images/resources/show.ftl");
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam("id");
		
//		String info = RedisPool.hGet(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_RESOURCE_KEY_IFREESHARE_COM,id);
		
		
		JsonObject docJson = search.getValueById(CoreBase.IMAGES, CoreBase.RESOURCES, id);
		
		String keywords = docJson.getString(CoreBase.HTML_KEYWORDS);
		
		PageDocument doc = new PageDocument();
		doc.setKeywords(keywords);
		doc.setDescription(docJson.getString(CoreBase.HTML_DESCRIPTION));
		doc.setName(docJson.getString(CoreBase.HTML_TITLE));
		doc.setTitle(docJson.getString(CoreBase.HTML_TITLE));
		doc.setSrc(docJson.getString(CoreBase.PATH));
		doc.setUuid(id);
		
		Log.log(logger, Level.DEBUG, "router[%s],image[%s]", this.getUrl(), docJson);
		
		String resourcePath = Configuration.getConfig(CoreBase.IRESOURCE, CoreBase.STORAGE);
		
	 	File file = new File(resourcePath+doc.getSrc()+"/thumbnail/");
	 	File[] childs = file.listFiles();
	 	
	 	Map<String, String> map = new HashMap<String, String>();
	 	
	 	for (int i = 0; i < childs.length; i++) {
	 		File child = childs[i];
	 		String name = child.getName();
	 		map.put("/iresource/"+doc.getSrc()+"/thumbnail/"+name, "/iresource/"+doc.getSrc()+"/"+name);
//	 		doc.getTags().add("/iresource/"+doc.getSrc()+"/thumbnail/"+"/"+name);
		}
		context.put("doc", doc);
		context.put("rimages", map);
		render(context);
	}
	
	
	
	
	
}

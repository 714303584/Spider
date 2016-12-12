package com.ifreeshare.spider.http.server.route.image.admin;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.verticle.SpiderImageVerticle;
import com.ifreeshare.spider.verticle.msg.MessageType;

public class ImageAdminKeywordReplacePost extends BaseRoute {
	
	
	public ImageAdminKeywordReplacePost() {
		super("/admin/image/replace/keyword/:itype/:otype/", BaseRoute.POST, "templates/images/admin/keyword_replace.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		
		String old_keyword = request.getParam(CoreBase.OLD_KEYWORDS);
		
		String new_keyword = request.getParam(CoreBase.NEW_KEYWORDS);
		
		if(old_keyword == null){
			render(context);
			return;
		}
		
		if(new_keyword == null){
			render(context);
			return;
		}
		
		JsonObject message = new JsonObject();
		message.put(MessageType.MESSAGE_TYPE, MessageType.KEYWORD_REPLACE);
		message.put(CoreBase.OLD_KEYWORDS, old_keyword);
		message.put(CoreBase.NEW_KEYWORDS, new_keyword);
		
		vertx.eventBus().send(SpiderImageVerticle.IMAGE_CHANGE_ADDRESS, message);
		response.end("OK");
		
	}

}

package com.ifreeshare.spider.http.server.route.classif;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.verticle.PersistenceVertical;

public class ClassificationCreate extends BaseRoute {

	public ClassificationCreate() {
		super("/admin/classif/create/", BaseRoute.POST, "templates/images/classif/create.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String name = request.getParam(CoreBase.NAME);
		String alias = request.getParam(CoreBase.ALIAS);
		String parent = request.getParam(CoreBase.PARENT);
		String keywords = request.getParam(CoreBase.HTML_KEYWORDS);
		String description = request.getParam(CoreBase.HTML_DESCRIPTION);
		String tags = request.getParam(CoreBase.TAGS);
		
		JsonObject json = new JsonObject();
		json.put(CoreBase.NAME, name);
		json.put(CoreBase.ALIAS, alias);
		if(parent == null || parent.trim().length() == 0){
			parent = CoreBase.PARENT_TOP;
		}
		json.put(CoreBase.PARENT, parent);
		json.put(CoreBase.HTML_KEYWORDS, keywords);
		json.put(CoreBase.HTML_DESCRIPTION, description);
		json.put(CoreBase.TAGS, tags);
		
		
		json.put(CoreBase.INDEX, CoreBase.INDEX_CLASSIFICATION);
		json.put(CoreBase.TYPE, CoreBase.TYPE_IMAGE);
		json.put(CoreBase.OPERATE, CoreBase.OPERATE_I);
			
		vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, json);
		doRedirect(response, "/admin/classif/list/");
	}
}

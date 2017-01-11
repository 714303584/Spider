package com.ifreeshare.spider.http.server.route.image.admin;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.verticle.PersistenceVertical;

public class ImageAdminUpdatePost extends BaseRoute {
	
	IDataSearch<JsonObject> searcher = IDataSearch.instance();
	
	public ImageAdminUpdatePost() {
		super("/admin/image/update/form/html/", BaseRoute.POST, "templates/images/admin/edit.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		
		JsonObject update =  new JsonObject();
		String id =  request.getParam(CoreBase.UUID);
		update.put(CoreBase.UUID, id);
		String keyword = request.getParam(CoreBase.HTML_KEYWORDS);
		update.put(CoreBase.HTML_KEYWORDS, keyword);
		
		update.put(CoreBase.INDEX,CoreBase.INDEX_HTML);
		update.put(CoreBase.TYPE, CoreBase.TYPE_IMAGE);
		update.put(CoreBase.OPERATE, CoreBase.OPERATE_U);
		vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, update);
		response.end("ok");
//		PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS
		
//		render(context);
	}
	
//	

}

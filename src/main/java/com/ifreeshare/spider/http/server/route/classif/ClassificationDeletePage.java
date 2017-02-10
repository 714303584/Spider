package com.ifreeshare.spider.http.server.route.classif;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.verticle.PersistenceVertical;


public class ClassificationDeletePage extends BaseRoute {

	public ClassificationDeletePage() {
		super("/admin/classif/delete/", BaseRoute.GET, "templates/images/classif/create.ftl");
	}
	
	IDataSearch<JsonObject> searcher = IDataSearch.instance();
	String imageSavePath = Configuration.getConfig(CoreBase.IMAGES, CoreBase.STORAGE);
	String thumbnailPath = Configuration.getConfig(CoreBase.IMAGES, CoreBase.DOC_THUMBNAIL);
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam(CoreBase.UUID);
		String referer =  request.getHeader("Referer");
		JsonObject document =  searcher.getValueById(CoreBase.INDEX_CLASSIFICATION, CoreBase.TYPE_IMAGE, id);
		if(document != null){
			Log.log(logger, Level.DEBUG, "router[%s],image[%s]", this.getUrl(), document);
			document.put(CoreBase.UUID, id);
			document.put(CoreBase.INDEX,CoreBase.INDEX_CLASSIFICATION);
			document.put(CoreBase.TYPE, CoreBase.TYPE_IMAGE);
			document.put(CoreBase.OPERATE, CoreBase.OPERATE_R);
			vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, document);
			doRedirect(response, referer);
		}else{
			doRedirect(response, "/admin/classif/list/");
//			response.end("NOT FOUND!");
		}
	}
}

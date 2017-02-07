package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.persistence.elasticsearch.ElasticSearchSearch;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;

public class GetImageRouter extends BaseRoute {

	IDataSearch<JsonObject> search = IDataSearch.instance();
	public GetImageRouter(FreeMarkerTemplateEngine freeMarkerTemplateEngine) {
		super("/public/image/:id/:itype/:otype/", BaseRoute.GET, "templates/images/edit.ftl", freeMarkerTemplateEngine);
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam("id");
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		
		if(!CoreBase.DATA_TYPE_GET.equals(iType)){
			faultRequest(response, ErrorBase.DATA_I_TYPE_ERROR);
			return;
		}
		
//		String info = RedisPool.hGet(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY,id);
		JsonObject docJson = search.getValueById(CoreBase.INDEX_HTML, CoreBase.TYPE_IMAGE, id);
		if(docJson == null){
			response.end("Temporarily not available ");
			return;
		}
		
		Log.log(logger, Level.DEBUG, "router[%s],id[%s], image info[%s]", this.getUrl(), id , docJson);
//		JsonObject  = new JsonObject(info);
		PageDocument doc = new PageDocument();
		doc.setUuid(docJson.getString(CoreBase.UUID));
		doc.setKeywords(docJson.getString(CoreBase.HTML_KEYWORDS));
		doc.setDescription(docJson.getString(CoreBase.HTML_DESCRIPTION));
		doc.setName(docJson.getString(CoreBase.FILE_NAME));
		doc.setTitle(docJson.getString(CoreBase.HTML_TITLE));
		doc.setOrigin(docJson.getString(CoreBase.URL));
		doc.setSrc(docJson.getString(CoreBase.FILE_URL_PATH));
		doc.setResolution(docJson.getString(CoreBase.RESOLUTION));
		context.put("doc", doc);
		render(context);
	}
}

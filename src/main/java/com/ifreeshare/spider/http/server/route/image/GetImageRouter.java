package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.redis.RedisPool;

public class GetImageRouter extends BaseRoute {

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
		
		String info = RedisPool.hGet(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY,id);
		if(CoreBase.DATA_TYPE_JSON.equals(oType)){
			response.end(info);
			return;
		}else if(CoreBase.DATA_TYPE_XML.equals(oType)){
			response.end("Temporarily not available ");
			return;
		}
		
		JsonObject docJson = new JsonObject(info);
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

package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;

public class UpdateImageRouter extends BaseRoute {
	
	public UpdateImageRouter(FreeMarkerTemplateEngine freeMarkerTemplateEngine) {
		super("/public/image/:id/:itype/:otype/", BaseRoute.POST, "templates/images/edit.ftl", freeMarkerTemplateEngine);
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam("id");
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		
		if(!postItypeCheck(iType)){
			faultRequest(response, ErrorBase.DATA_I_TYPE_ERROR);
			return;
		}
		
		if(!otypeCheck(oType)){
			faultRequest(response, ErrorBase.DATA_O_TYPE_ERROR);
			return;
		}
		System.out.println(context.getBodyAsString());
		request.params().forEach(entity -> {
			System.out.println("forEach --- key:["+entity.getKey()+"] value:["+entity.getValue()+"]");
		});
		
		
		response.end("success"+request.getParam("filename"));
	}
	
	
	
	
	
	

}

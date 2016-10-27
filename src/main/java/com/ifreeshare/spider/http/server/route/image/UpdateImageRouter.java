package com.ifreeshare.spider.http.server.route.image;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import com.ifreeshare.spider.http.server.route.BaseRoute;

public class UpdateImageRouter extends BaseRoute {
	
	public UpdateImageRouter(FreeMarkerTemplateEngine freeMarkerTemplateEngine) {
		super("/image/:id/:itype/:otype/", BaseRoute.GET, "templates/images/edit.ftl", freeMarkerTemplateEngine);
	}

	@Override
	public void process(RoutingContext context) {
		
		
		
		
		
	}
	
	
	
	
	
	

}

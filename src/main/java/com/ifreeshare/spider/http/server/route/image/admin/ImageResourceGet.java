package com.ifreeshare.spider.http.server.route.image.admin;

import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.http.server.route.BaseRoute;

public class ImageResourceGet extends BaseRoute {

	public ImageResourceGet() {
		super("/admin/image/resource/create/", BaseRoute.GET, "templates/images/resources/resource_create_get.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		render(context);
	}
}

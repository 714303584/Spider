package com.ifreeshare.spider.http.server.route.classif;

import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.http.server.route.BaseRoute;


public class ClassificationCreatePage extends BaseRoute {

	public ClassificationCreatePage() {
		super("/admin/classif/create/", BaseRoute.GET, "templates/images/classif/create.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		render(context);
	}
}

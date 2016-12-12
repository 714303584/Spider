package com.ifreeshare.spider.http.server.route.image.admin;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;

public class ImageAdminKeywordReplace extends BaseRoute {
	
	
	public ImageAdminKeywordReplace() {
		super("/admin/image/replace/keyword/:itype/:otype/", BaseRoute.GET, "templates/images/admin/keyword_replace.ftl");
	}
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		
		if(!CoreBase.DATA_TYPE_GET.equals(iType)){
			faultRequest(response, ErrorBase.DATA_I_TYPE_ERROR);
			return;
		}
		
		render(context);
	}

}

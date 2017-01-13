package com.ifreeshare.spider.http.server.route.users;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;

/**
 * @author zhuss
 * @date 2016-11-7PM6:44:09
 * @description  User registration function
 */
public class UserDetailsPageRouter extends BaseRoute {

	public UserDetailsPageRouter() {
		super("/user/details/:itype/:otype/", BaseRoute.GET, "/templates/users/details.ftl");
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		if(!getItypeCheck(iType)){
			faultRequest(response, ErrorBase.DATA_I_TYPE_ERROR);
			return;
		}
		if(!otypeCheck(oType)){
			faultRequest(response, ErrorBase.DATA_O_TYPE_ERROR);
			return;
		}
		render(context);	
	}

}

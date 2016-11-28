package com.ifreeshare.spider.http.server.route.users;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.redis.RedisPool;

/**
 * @author zhuss
 * @date 2016-11-7PM6:44:09
 * @description  User registration function
 */
public class UserRegistPostRouter extends BaseRoute {

	public UserRegistPostRouter() {
		super("/user/regist/:itype/:otype/", BaseRoute.POST, "/templates/users/details.ftl");
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();
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
		
		MultiMap params = request.params();
		
		String username = params.get(CoreBase.USERNAME);
		String password = params.get(CoreBase.PASSWORD);
		
		if(RedisPool.hExist(CoreBase.USERNAME_USER_INFO_IFREESHARE_COM, username)){
			
			
		}else{
			JsonObject userInfo = new JsonObject().put(CoreBase.USERNAME, username).put(CoreBase.PASSWORD, password);
			RedisPool.hSet(CoreBase.USERNAME_USER_INFO_IFREESHARE_COM, username, userInfo.toString());
		}
		
		render(context);
	}
	
	
	

}

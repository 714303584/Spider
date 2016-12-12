package com.ifreeshare.spider.http.server.route.users;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
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
public class UserLoginPostRouter extends BaseRoute {

	public UserLoginPostRouter() {
		super("/user/login/:itype/:otype/", BaseRoute.POST, "/templates/users/details.ftl");
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
		
		String userInfo = RedisPool.hGet(CoreBase.USERNAME_USER_INFO_IFREESHARE_COM, username);
		
		if(userInfo != null && username.trim().length() > 0){
			JsonObject userJson = new JsonObject(userInfo);
			String userPassword = userJson.getString(CoreBase.PASSWORD);
			if(userPassword.equals(password)){
				render(context);	
			}else{
				
			}
		}
			
			
		
		
	}
	
	
	

}

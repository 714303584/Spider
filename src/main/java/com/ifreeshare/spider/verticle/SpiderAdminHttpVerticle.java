package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.spider.http.server.SpiderHttpServer;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.http.server.route.image.ShowResouceRouter;
import com.ifreeshare.spider.http.server.route.image.admin.ImageAdminKeywordReplace;
import com.ifreeshare.spider.http.server.route.image.admin.ImageAdminKeywordReplacePost;
import com.ifreeshare.spider.http.server.route.image.admin.ImageAdminUpdateHtml;
import com.ifreeshare.spider.http.server.route.image.admin.ImageResourceCreate;
import com.ifreeshare.spider.http.server.route.image.admin.ImageResourceGet;
import com.ifreeshare.spider.http.server.route.image.admin.ImagesAdminList;
import com.ifreeshare.spider.http.server.route.users.UserDetailsPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserLoginPageRouter;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;

/**
 * Used to manage the captured data 
 * @author zhuss
 */
public class SpiderAdminHttpVerticle extends AbstractVerticle {
	
	//Output log instance 
	private static  Logger logger  = Log.register(SpiderHttpServer.class.getName());
	
	//Website routing 
	Set<BaseRoute> routers = new HashSet<BaseRoute>();
	
	
	public SpiderAdminHttpVerticle(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
	}

	@Override
	public void start() throws Exception {
		
		Vertx vertx = Vertx.vertx();
		HttpServer httpServer = vertx.createHttpServer();
		FreeMarkerTemplateEngine freeMarkerTemplateEngine = FreeMarkerTemplateEngine.create();
		
		routers.add(new ImagesAdminList());
		routers.add(new ImageAdminUpdateHtml());
		routers.add(new ImageAdminKeywordReplace());
		routers.add(new ImageAdminKeywordReplacePost());
		routers.add(new UserLoginPageRouter());
		routers.add(new ImageResourceGet());
		routers.add(new ImageResourceCreate());
		routers.add(new ShowResouceRouter());
		
		Iterator<BaseRoute> rit = routers.iterator();
		Router router = Router.router(vertx);
	    //cookies, sessions and request bodies
	  	router.route().handler(BodyHandler.create());
	  	router.route().handler(CookieHandler.create());
	  	router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
	  		
  		/**
  		 * login
  		 */
  		AuthProvider authProvider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, new JsonObject());
  		
  	    router.route().handler(UserSessionHandler.create(authProvider));
  		router.route("/admin/*").handler(RedirectAuthHandler.create(authProvider, "/public/login/get/html/"));
  		router.route("/login/").handler(FormLoginHandler.create(authProvider).setDirectLoggedInOKURL("/admin/search/image/get/html/?keys=11"));
	    
//	    SessionStore store = ClusteredSessionStore.create(vertx);
//	    SessionHandler sessionHandler = SessionHandler.create(store);
//	    router.route().handler(sessionHandler);

		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		while (rit.hasNext()) {
			BaseRoute r = rit.next();
			Log.log(logger, Level.WARN, "router[%s]", r.toString());
			switch (r.getMethod()) {
			case BaseRoute.GET:
				router.get(r.getUrl()).handler(context -> {
					r.process(context);
				});
				break;
			case BaseRoute.POST:
				router.post(r.getUrl()).handler(context -> {
					r.process(context);
				});
				break;
			case BaseRoute.PATCH:
				router.patch(r.getUrl()).handler(context -> {
					r.process(context);
				});
				break;


			default:
				break;
			}
		}
		
		httpServer.requestHandler(router::accept).listen(15888);
		Log.log(logger, Level.WARN, "httpserver listen port[15888]");
		
		
	}

}

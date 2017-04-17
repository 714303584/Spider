package com.ifreeshare.dht.crawler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.framework.HttpServerShell;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.SpiderHttpServer;
import com.ifreeshare.spider.log.Log;


public class DHTServer extends AbstractVerticle {

		private static  Logger logger  = Log.register(DHTServer.class.getName());
		public static Vertx vertx = null;
		
		public DHTServer(Vertx vertx, Context context) {
			this.vertx = vertx;
			this.context = context;
		}

		@Override
		public void start() throws Exception {

			Vertx vertx = Vertx.vertx();
			CoreBase.vertx = vertx;
			HttpServer httpServer = vertx.createHttpServer();
			Router router = Router.router(vertx);
			
			//cookies, sessions and request bodies
			router.route().handler(BodyHandler.create());
			router.route().handler(CookieHandler.create());
			router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
			router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
			
			HttpServerShell hss = new HttpServerShell(httpServer, router , "com.ifreeshare.dht.pub.controller");
			hss.setFreeMarkerTemplateEngine(FreeMarkerTemplateEngine.create());
			hss.initRouter();
			
			httpServer.requestHandler(router::accept).listen(8001);
		}

		@Override
		public void init(Vertx vertx, Context context) {

		}

		public static void main(String[] args) {
			Vertx vertx = Vertx.vertx();
			Context context = vertx.getOrCreateContext();
			
//			ElasticSearchPersistence persistence = new ElasticSearchPersistence();

//			vertx.deployVerticle(new SpiderHeaderVerticle(vertx, context));
//			vertx.deployVerticle(new SpiderHtmlVerticle(vertx, context));
//			vertx.deployVerticle(new SpiderImageVerticle(vertx, context));
//			vertx.deployVerticle(new SpiderFileVerticle(vertx, context));
//			vertx.deployVerticle(new PersistenceVertical(vertx, context, persistence));

			
			vertx.deployVerticle(new DHTServer(vertx, context));

		}

}

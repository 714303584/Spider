package com.ifreeshare.spider.http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.ScoreDoc;

import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.http.server.route.image.GetImageRouter;
import com.ifreeshare.spider.http.server.route.image.LoveImageRouter;
import com.ifreeshare.spider.http.server.route.image.SearchImageRouter;
import com.ifreeshare.spider.http.server.route.image.ShowImageRouter;
import com.ifreeshare.spider.http.server.route.image.UpdateImageRouter;
import com.ifreeshare.spider.http.server.route.users.UserDetailsPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserLoginPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserLoginPostRouter;
import com.ifreeshare.spider.http.server.route.users.UserRegistPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserRegistPostRouter;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;

/**
 * User services 
 * @author zhuss
 */
public class SpiderHttpServer extends AbstractVerticle {
	private static  Logger logger  = Log.register(SpiderHttpServer.class.getName());
	
	
	Set<BaseRoute> routers = new HashSet<BaseRoute>();
	
	
	public SpiderHttpServer(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
	}

	@Override
	public void start() throws Exception {

		Vertx vertx = Vertx.vertx();
		HttpServer httpServer = vertx.createHttpServer();
		FreeMarkerTemplateEngine freeMarkerTemplateEngine = FreeMarkerTemplateEngine.create();
		// init Route 
		routers.add(new GetImageRouter(freeMarkerTemplateEngine));
		routers.add(new UpdateImageRouter(freeMarkerTemplateEngine));
		routers.add(new UserRegistPageRouter());
		routers.add(new SearchImageRouter());
		routers.add(new UserRegistPostRouter());
		routers.add(new UserLoginPostRouter());
		routers.add(new UserLoginPageRouter());
		routers.add(new LoveImageRouter());
		routers.add(new ShowImageRouter());
		routers.add(new UserDetailsPageRouter());
		
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
		
		
//		authProvider.authenticate(new JsonObject(), result -> {
//			User user = result.result();
//		});
//		
		
	    router.route().handler(UserSessionHandler.create(authProvider));
		router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/login/get/html/"));
		router.route("/login/").handler(FormLoginHandler.create(authProvider).setDirectLoggedInOKURL("/user/details/get/html/"));
		
	    router.route().handler(context -> {
	    	HttpServerRequest request = context.request();
	    	SocketAddress clientAddress = request.remoteAddress();
	    	String ipAddress = clientAddress.host();
	    	String path = request.path();
	    	Log.log(logger, Level.INFO, "client host[%s], request path[%s]", ipAddress, path);
	    	context.next();
	    });
	    
	    router.route().handler(CookieHandler.create());
//	    SessionStore store = ClusteredSessionStore.create(vertx);
//	    SessionHandler sessionHandler = SessionHandler.create(store);
//	    router.route().handler(sessionHandler);
	    
	    router.route("/logout").handler(context -> {
	        context.clearUser();
	        // Redirect back to the index page
	        context.response().putHeader("location", "/").setStatusCode(302).end();
	      });
	    

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
		
		router.get("/private/mypage").handler(context -> {
			context.response().end("ok");
		});
		

		router.get("/file/search").handler(context -> {
			String keys = context.request().getParam("keys");
			String[] value = { keys, keys, keys };
			String[] field = { CoreBase.HTML_TITLE, CoreBase.HTML_KEYWORDS, CoreBase.HTML_DESCRIPTION };
			Occur[] occur = { Occur.SHOULD, Occur.SHOULD, Occur.SHOULD };

			ScoreDoc scoreDoc = null;
			Document[] documents = LuceneFactory.search(value, 100, field, occur, scoreDoc);

			List<PageDocument> page = new ArrayList<PageDocument>();

			JsonObject result = new JsonObject();
			JsonArray objects = new JsonArray();

			for (int i = 0; i < documents.length; i++) {
				Document document = documents[i];
				String uuid = document.get(CoreBase.UUID);
				String keywords = document.get(CoreBase.HTML_KEYWORDS);
				String description = document.get(CoreBase.HTML_DESCRIPTION);
				String title = document.get(CoreBase.HTML_TITLE);
				String file_url_path = document.get(CoreBase.FILE_URL_PATH);
				JsonObject docJson = new JsonObject();
				PageDocument pd = new PageDocument();
				pd.setUuid(uuid);
				
				try {
					pd.setThumbnail(file_url_path);
					pd.setName(title);
					pd.setKeywords(keywords);
					pd.setDescription(description);
					page.add(pd);
					Log.log(logger, Level.WARN, "File Info [%s]", pd.toString());
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			objects.add(docJson);
		}

		context.put("pages", page);
		context.put("keys", keys);

		freeMarkerTemplateEngine.render(context, "templates/search.ftl", res -> {
			if (res.succeeded()) {
				// context.response.putHeader("content-type",
				// "application/json;charset=UTF-8");
				context.response().end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	});
		
		String listen_port = Configuration.getConfig(CoreBase.HTTP_SERVER, CoreBase.LISTEN_PORT);
		httpServer.requestHandler(router::accept).listen(Integer.parseInt(listen_port));
	}

	@Override
	public void init(Vertx vertx, Context context) {

	}

	public static void main(String[] args) {
		Configuration.load("",SpiderHttpServer.class.getResource("/spider-config.xml").getPath());
		// System.setProperty("vertx.disableFileCaching", "true");
		Vertx vertx = Vertx.vertx();
		Context context = vertx.getOrCreateContext();
		vertx.deployVerticle(new SpiderHttpServer(vertx, context));

	}

}

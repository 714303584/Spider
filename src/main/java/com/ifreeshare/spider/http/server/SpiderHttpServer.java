package com.ifreeshare.spider.http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
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
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;


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
		
		Iterator<BaseRoute> rit = routers.iterator();
		Router router = Router.router(vertx);
		
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
		
		
		
		
		
		router.post("/conference/listener/").handler(context -> {
			HttpServerRequest request = context.request();
			HttpServerResponse response = context.response();
			request.bodyHandler(body -> {
				System.out.println(body.toString());
				response.end("success");
			});
		});
	

		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));

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
		
//		router.get().handler(context -> {
//				
//			
//			
//			
//			
//		});
		
		router.post("/create/image/:type/").handler(context -> {
			
		});
		
		
		
		router.get("/images/search").handler(context -> {
			String keys = context.request().getParam("keys");
			String[] value = { keys, keys, keys };
			String[] field = { CoreBase.HTML_TITLE, CoreBase.HTML_KEYWORDS, CoreBase.HTML_DESCRIPTION };
			Occur[] occur = { Occur.SHOULD, Occur.SHOULD, Occur.SHOULD };

			ScoreDoc scoreDoc = null;
			Document[] documents = LuceneFactory.search("H:\\imagesLucene", value, 100, field, occur, scoreDoc);

			List<PageDocument> page = new ArrayList<PageDocument>();

			JsonObject result = new JsonObject();
			JsonArray objects = new JsonArray();

			for (int i = 0; i < documents.length; i++) {
				Document document = documents[i];
				String uuid = document.get(CoreBase.UUID);
				String keywords = document.get(CoreBase.HTML_KEYWORDS);
				String description = document.get(CoreBase.HTML_DESCRIPTION);
				String title = document.get(CoreBase.HTML_TITLE);
				String thumbnail = document.get(CoreBase.FILE_URL_PATH);
				JsonObject docJson = new JsonObject();
				PageDocument pd = new PageDocument();
				pd.setUuid(uuid);
				try {
					pd.setUuid(uuid);
					pd.setName(title);
					pd.setKeywords(keywords);
					pd.setDescription(description);
					pd.setThumbnail(thumbnail);
					page.add(pd);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			objects.add(docJson);
		}

		context.put("pages", page);
		context.put("keys", keys);

		freeMarkerTemplateEngine.render(context, "templates/show.ftl", res -> {
			if (res.succeeded()) {
				// context.response.putHeader("content-type",
				// "application/json;charset=UTF-8");
				context.response().end(res.result());
			} else {
				context.fail(res.cause());
			}
		});

	});
		
		
		

		router.get("/policy/v1/service/configuration").handler(context -> {
			System.err.println("/policy/v1/service/configuration");
			HttpServerRequest request = context.request();

			Iterator<String> it = request.params().names().iterator();
			while (it.hasNext()) {
				String key = it.next();

				System.out.println("keys-----" + key + "      value-----" + request.getParam(key));
			}
			JsonObject result = new JsonObject();
			result.put("status", "success");
			result.put("action", "continue");

			context.response().setStatusCode(200).headers().add("Content-Type", "application/json");
			context.response().end(result.toString());

		});

		router.get("/policy/v1/participant/location").handler(context -> {
			System.err.println("/policy/v1/participant/location");
			HttpServerRequest request = context.request();

			Iterator<String> it = request.params().names().iterator();
			while (it.hasNext()) {
				String key = it.next();

				System.out.println("keys-----" + key + "      value-----" + request.getParam(key));
			}
			JsonObject result = new JsonObject();
			result.put("status", "success");
			result.put("action", "continue");

			context.response().setStatusCode(200).headers().add("Content-Type", "application/json");
			context.response().end(result.toString());

		});

		router.get("/policy/v1/participant/avatar/:alias").handler(context -> {
			System.err.println("/policy/v1/participant/location");
			HttpServerRequest request = context.request();

			Iterator<String> it = request.params().names().iterator();
			while (it.hasNext()) {
				String key = it.next();

				System.out.println("keys-----" + key + "      value-----" + request.getParam(key));
			}
			JsonObject result = new JsonObject();
			result.put("status", "success");
			result.put("action", "continue");

			context.response().setStatusCode(200).headers().add("Content-Type", "application/json");
			context.response().end(result.toString());

		});

		router.get("/policy/v1/participant/audioavatar/:alias").handler(context -> {
			System.err.println("/policy/v1/participant/location");
			HttpServerRequest request = context.request();

			Iterator<String> it = request.params().names().iterator();
			while (it.hasNext()) {
				String key = it.next();

				System.out.println("keys-----" + key + "      value-----" + request.getParam(key));
			}
			JsonObject result = new JsonObject();
			result.put("status", "success");
			result.put("action", "continue");

			context.response().setStatusCode(200).headers().add("Content-Type", "application/json");
			context.response().end(result.toString());

		});

		//
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

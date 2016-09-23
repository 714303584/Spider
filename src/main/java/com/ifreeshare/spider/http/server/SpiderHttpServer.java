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

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause.Occur;

import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.spider.core.CoreBase;


public class SpiderHttpServer extends AbstractVerticle{

	public SpiderHttpServer(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
	}
	
	
	@Override
	public void start() throws Exception {
		HttpServer httpServer = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		FreeMarkerTemplateEngine freeMarkerTemplateEngine = FreeMarkerTemplateEngine.create();
		
		
		
		
		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		router.get("/images/search").handler(context -> {
		 	String keys =  context.request().getParam("keys");
		 	String[] value = {keys, keys , keys};
		 	String[] field = {CoreBase.HTML_TITLE,CoreBase.HTML_KEYWORDS,CoreBase.HTML_DESCRIPTION};
		 	Occur[] occur = {Occur.SHOULD,Occur.SHOULD,Occur.SHOULD};
		 	
		 	Document[] documents = LuceneFactory.search(value, 100, field, occur, null);
		 	JsonObject result = new JsonObject();
		 	JsonArray objects = new JsonArray();
		 	
		 	for (int i = 0; i < documents.length; i++) {
				Document document = documents[i];
				String uuid = document.get(CoreBase.UUID);
				String keywords = document.get(CoreBase.HTML_KEYWORDS);
				String description = document.get(CoreBase.HTML_DESCRIPTION);
				String title = document.get(CoreBase.HTML_TITLE);
				JsonObject docJson = new JsonObject();
				docJson.put(CoreBase.UUID, uuid);
				try {
					docJson.put(CoreBase.HTML_TITLE, title);
					docJson.put(CoreBase.HTML_KEYWORDS, keywords);
					docJson.put(CoreBase.HTML_DESCRIPTION, description);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				objects.add(docJson);
			}
		 	HttpServerResponse response = context.response();
		 	response.putHeader("content-type", "application/json;charset=UTF-8");
		 	result.put(CoreBase.OBJECTS, objects);
		 	response.end(result.toString());
		});
		
	
		
		
		router.post("/conference/listener/").handler(context -> {
			HttpServerRequest request =  context.request();
			HttpServerResponse response =  context.response();
			
			request.bodyHandler(body -> {
				System.out.println(body.toString());
				response.end("success");
			});
		});
		
		
		
		router.get("/holle/").handler(ctx -> {
			ctx.put("name", "Vert.x Web");
			
			freeMarkerTemplateEngine.render(ctx, "templates/index.ftl", res -> {
				
				if(res.succeeded()){
					ctx.response().end(res.result());
				}else{
					ctx.fail(res.cause());
				}
				
				
			});
		});
		
		
		
		httpServer.requestHandler(router::accept).listen(8000);
	}
	
	
	@Override
	public void init(Vertx vertx, Context context) {
	
	}
	
	
	public static void main(String[] args) {
		
		 System.setProperty("vertx.disableFileCaching", "true");
		Vertx vertx = Vertx.vertx();
    	Context context = vertx.getOrCreateContext();
    	
    	vertx.deployVerticle(new SpiderHttpServer(vertx, context));
    
	}
	
	
	

}

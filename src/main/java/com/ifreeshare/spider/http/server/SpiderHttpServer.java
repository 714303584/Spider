package com.ifreeshare.spider.http.server;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause.Occur;

import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.verticle.SpiderFileVerticle;
import com.ifreeshare.spider.verticle.SpiderHeaderVerticle;
import com.ifreeshare.spider.verticle.SpiderHtmlVerticle;
import com.ifreeshare.spider.verticle.SpiderImageVerticle;
import com.ifreeshare.spider.verticle.SpiderMainVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class SpiderHttpServer extends AbstractVerticle{

	public SpiderHttpServer(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
	}
	
	
	
	@Override
	public void start() throws Exception {
		HttpServer httpServer = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
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
				JsonObject docJson = new JsonObject();
				docJson.put(CoreBase.UUID, uuid);
				docJson.put(CoreBase.HTML_KEYWORDS, keywords);
				docJson.put(CoreBase.HTML_DESCRIPTION, description);
				objects.add(docJson);
			}
		 	result.put(CoreBase.OBJECTS, objects);
		 	context.response().end(result.toString());
		});
		
		httpServer.requestHandler(router::accept).listen(8000);
	}
	
	
	@Override
	public void init(Vertx vertx, Context context) {
	
	}
	
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
    	Context context = vertx.getOrCreateContext();
    	
    	vertx.deployVerticle(new SpiderHttpServer(vertx, context));
    
	}
	
	
	

}

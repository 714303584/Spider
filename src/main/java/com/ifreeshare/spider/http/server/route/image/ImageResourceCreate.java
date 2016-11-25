package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import org.apache.lucene.index.IndexWriter;

import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.redis.RedisPool;

public class ImageResourceCreate extends BaseRoute {
	
	public static final IndexWriter indexWriter = LuceneFactory.getIndexWriter("H:\\imageresource");

	public ImageResourceCreate() {
		super("/image/resource/:itype/:otype/", BaseRoute.POST, "templates/images/search.ftl");
	}

	/**
	 * Create a collection of pictures 
	 */
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam("id");
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
		
		String resourceName = request.getParam(CoreBase.NAME);
		
		if(!RedisPool.hExist(CoreBase.IMAGE_RESOURSE_HASH_KEY, resourceName)){
			String keywords = request.getParam(CoreBase.HTML_KEYWORDS);
			String description = request.getParam(CoreBase.HTML_DESCRIPTION);
			String title = request.getParam(CoreBase.HTML_TITLE);
			
			
			
		}else{
			
		}
		
		
		
		
		
	}
	
	
	
	public void createIndex(){
		
	}
	
	
	
	
	

}

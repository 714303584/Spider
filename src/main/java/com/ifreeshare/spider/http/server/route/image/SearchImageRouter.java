package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause.Occur;

import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.util.RegExpValidatorUtils;

/**
 * @author zhuss
 * @date 2016-11-13PM4:46:27
 * @description  Image search and paging
 */
public class SearchImageRouter extends BaseRoute {

	public SearchImageRouter() {
		super("/search/image/:itype/:otype/", BaseRoute.GET, "templates/images/search.ftl");
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		
		String keys = request.getParam("keys");
		String index = request.getParam("index");
		String size = request.getParam("size");
		String[] value = { keys, keys, keys };
		String[] field = { CoreBase.HTML_TITLE, CoreBase.HTML_KEYWORDS, CoreBase.HTML_DESCRIPTION };
		Occur[] occur = { Occur.SHOULD, Occur.SHOULD, Occur.SHOULD };
		
		int pageIndex = 1;
		if(index != null && RegExpValidatorUtils.IsIntNumber(index)){
			pageIndex = Integer.parseInt(index);
		}
		
		int pageSize = 10;
		if(size != null  && RegExpValidatorUtils.IsIntNumber(size)){
			pageSize = Integer.parseInt(size);
		}
		
		Document[] documents = LuceneFactory.search("H:\\imagesLucene", value, pageSize, field, occur, pageIndex);
		
		if(documents == null){
			response.end("No More Can Give You!");
		}else{
			List<PageDocument> page = new ArrayList<PageDocument>();
			JsonObject result = new JsonObject();
			JsonArray objects = new JsonArray();
			for (int i = 0; i < documents.length; i++) {
				Document document = documents[i];
				String uuid = document.get(CoreBase.UUID);
				String keywords = document.get(CoreBase.HTML_KEYWORDS);
				String description = document.get(CoreBase.HTML_DESCRIPTION);
				String title = document.get(CoreBase.HTML_TITLE);
				String thumbnail = document.get(CoreBase.DOC_THUMBNAIL);
				String src = document.get(CoreBase.FILE_URL_PATH);
				JsonObject docJson = new JsonObject();
				PageDocument pd = new PageDocument();
				pd.setUuid(uuid);
				try {
					pd.setUuid(uuid);
					pd.setName(title);
					pd.setKeywords(keywords);
					pd.setDescription(description);
					pd.setThumbnail(thumbnail);
					pd.setSrc(src);
					Log.log(logger, Level.DEBUG, "router[%s],image[%s]", this.getUrl(), pd);
					page.add(pd);
				} catch (Exception e) {
					e.printStackTrace();
				}
				objects.add(docJson);
			}
			
			if(page.size() == pageSize){
				context.put("nextp", (pageIndex+1));
			}else{
				context.put("nextp", pageIndex);
			}
			
			if(pageIndex > 1){
				context.put("previous", (pageIndex - 1));
			}else{
				context.put("previous", pageIndex);
			}
			
			context.put("sizep", pageSize);
			context.put("pages", page);
			context.put("keys", keys);
		
			render(context);
		}

		
	}

}

package com.ifreeshare.spider.http.server.route.image.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.pdfbox.util.operator.NextLine;

import redis.clients.jedis.ScanResult;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;
import com.ifreeshare.util.RegExpValidatorUtils;

public class ImagesAdminList extends BaseRoute {
	public ImagesAdminList() {
		super("/admin/search/image/:itype/:otype/", BaseRoute.GET, "templates/images/admin/search.ftl");
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
//		String[] field = { 
////				CoreBase.HTML_TITLE,
//				CoreBase.HTML_KEYWORDS
////				, CoreBase.HTML_DESCRIPTION 
//				};
//		Occur[] occur = { Occur.SHOULD, Occur.SHOULD, Occur.SHOULD };
		
		int pageIndex = 0;
		if(index != null && RegExpValidatorUtils.IsIntNumber(index)){
			pageIndex = Integer.parseInt(index);
		}else{
			index = "0";
		}
		
		int pageSize = 10;
		if(size != null  && RegExpValidatorUtils.IsIntNumber(size)){
			pageSize = Integer.parseInt(size);
		}
		
		ScanResult<Map.Entry<String, String>> sr = RedisPool.hScan(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY, index, pageSize);
		List<PageDocument> page = new ArrayList<>();
		String nextCursor = sr.getStringCursor();
		
		List<Map.Entry<String, String>> result =  sr.getResult();
		Iterator<Map.Entry<String, String>> it = result.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String getValue = entry.getValue();
			JsonObject obj = new JsonObject(getValue);
			PageDocument pd = new PageDocument();
			pd.setUuid(obj.getString(CoreBase.UUID));
//			pd.setName(obj.getString(CoreBase.HTML_TITLE));
			pd.setKeywords(obj.getString(CoreBase.HTML_KEYWORDS));
//			pd.setDescription(obj.getString(CoreBase.HTML_DESCRIPTION));
			pd.setThumbnail(obj.getString(CoreBase.DOC_THUMBNAIL));
			pd.setSrc(obj.getString(CoreBase.FILE_URL_PATH));
			Log.log(logger, Level.DEBUG, "router[%s], pageDocument[%s]", this.getUrl(), pd);
			page.add(pd);
		}
		
		if(page.size() == pageSize){
			context.put("nextp", nextCursor);
		}else{
			context.put("nextp", nextCursor);
		}
		
		if(pageIndex > 1){
			context.put("previous", index);
		}else{
			context.put("previous", index);
		}
		
		context.put("sizep", pageSize);
		context.put("pages", page);
		context.put("keys", keys);
	
		render(context);
		
		
		
		
		
		
	}
	
	
}

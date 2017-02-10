package com.ifreeshare.spider.http.server.route.classif;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.Classification;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

public class ClassificationManager extends BaseRoute {
	public ClassificationManager() {
		super("/admin/classif/list/", BaseRoute.GET, "templates/images/classif/list.ftl");
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.log(logger, Level.DEBUG, "router[%s],TransportClient[%s]", this.getUrl(), e.getMessage());
		}
	}
	
	TransportClient client = null;

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();

		String keys = request.getParam("keys");
		String index = request.getParam("index");
		String size = request.getParam("size");

		int pageIndex = 0;
		if ((index != null && RegExpValidatorUtils.IsIntNumber(index))) {
			pageIndex = Integer.parseInt(index);
		}

		int pageSize = 50;
		if (size != null && RegExpValidatorUtils.IsIntNumber(size)) {
			pageSize = Integer.parseInt(size);
		}
		
		SearchRequestBuilder srb = client.prepareSearch(CoreBase.INDEX_CLASSIFICATION).setTypes(CoreBase.TYPE_IMAGE);

		if (keys != null && keys.trim().length() != 0) {
			QueryBuilder qb = QueryBuilders.matchQuery(CoreBase.HTML_KEYWORDS, keys);
			srb.setQuery(qb);
		}else{
//			srb.addSort(CoreBase.CREATE_DATE, SortOrder.DESC);
			keys="";
		}

		int pageFrom =  pageIndex*pageSize;
		SearchResponse scrollResp = srb.setFrom(pageFrom).setSize(pageSize).get();

		SearchHits sh = scrollResp.getHits();
		long totalCount = sh.getTotalHits();
		Log.log(logger, Level.DEBUG, "router[%s],SearchHits.size[%d]", this.getUrl(), totalCount );
		List<Classification> result = new ArrayList<Classification>();
		for (SearchHit hit : sh.getHits()) {
			JsonObject document = new JsonObject(hit.getSourceAsString());
			
			String uuid = hit.getId();
			String keywords = document.getString(CoreBase.HTML_KEYWORDS);
			String name = document.getString(CoreBase.NAME);
			String description = document.getString(CoreBase.HTML_DESCRIPTION);
			String alias = document.getString(CoreBase.ALIAS);
			String parent = document.getString(CoreBase.PARENT);
			String tags = document.getString(CoreBase.TAGS);
			Classification classif = new Classification();
			classif.setId(uuid);
			try {
				classif.setKeywords(keywords);
				classif.setDescription(description);
				classif.setAlias(alias);
				classif.setName(name);
				if(parent == null){
					parent = CoreBase.PARENT_TOP;
				}
				
				if(tags == null){
					tags = new String();
				}
				classif.setTags(tags);
				
				classif.setParent(parent);
				
				Log.log(logger, Level.DEBUG, "router[%s],image[%s]", this.getUrl(), classif);
				result.add(classif);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		DefaultPage<Classification> pages = new DefaultPage<Classification>(pageIndex, pageSize, result, totalCount);
		context.put("pages", pages);
		context.put("keys", keys);
		render(context);
	}
	
	
}

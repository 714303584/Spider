package com.ifreeshare.spider.http.server.route.image;

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
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.Classification;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

/**
 * @author zhuss
 * @date 2016-11-13PM4:46:27
 * @description Image search and paging
 */
public class SearchImageTagRouter extends BaseRoute {
	TransportClient client = IDataSearch.instance().getSearchClient();

	public SearchImageTagRouter() {
		super("/public/classic/image/:keys/", BaseRoute.GET, "templates/images/classif/search.ftl");
//		try {
//			client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//			Log.log(logger, Level.DEBUG, "router[%s],TransportClient[%s]", this.getUrl(), e.getMessage());
//		}
	}

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
		
		 JsonObject classif = (JsonObject) IDataSearch.instance().getValueById(CoreBase.INDEX_CLASSIFICATION, CoreBase.TYPE_IMAGE, keys);
		 String alias = classif.getString(CoreBase.ALIAS);
		 String ckeywords = classif.getString(CoreBase.HTML_KEYWORDS);
		 String cdescription = classif.getString(CoreBase.HTML_DESCRIPTION);
		 String name = classif.getString(CoreBase.NAME);
		 
		 Classification classification = new Classification();
		 classification.setId(classif.getString(CoreBase.ID));
		 classification.setName(name);
		 classification.setKeywords(ckeywords);
		 classification.setDescription(cdescription);
		
		SearchRequestBuilder srb = client.prepareSearch(CoreBase.INDEX_HTML).setTypes(CoreBase.TYPE_IMAGE);

//		if (keys != null && keys.trim().length() != 0) {
			QueryBuilder qb = QueryBuilders.matchQuery(CoreBase.ENGLISH_KEYWORDS, alias);
			srb.setQuery(qb);
//		}else{
//			srb.addSort(CoreBase.CREATE_DATE, SortOrder.DESC);
//			keys="";
//		}
		
		

		int pageFrom =  pageIndex*pageSize;
		SearchResponse scrollResp = srb.setFrom(pageFrom).setSize(pageSize).get();

		SearchHits sh = scrollResp.getHits();
		long totalCount = sh.getTotalHits();
		Log.log(logger, Level.DEBUG, "router[%s],SearchHits.size[%d]", this.getUrl(), totalCount );
		List<PageDocument> result = new ArrayList<PageDocument>();
		for (SearchHit hit : sh.getHits()) {
			JsonObject document = new JsonObject(hit.getSourceAsString());
			String uuid = document.getString(CoreBase.UUID);
			String keywords = document.getString(CoreBase.HTML_KEYWORDS);
			String description = document.getString(CoreBase.HTML_DESCRIPTION);
			String title = document.getString(CoreBase.HTML_TITLE);
			String thumbnail = document.getString(CoreBase.DOC_THUMBNAIL);
			String src = document.getString(CoreBase.FILE_URL_PATH);
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
				result.add(pd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		DefaultPage<PageDocument> pages = new DefaultPage<PageDocument>(pageIndex, pageSize, result, totalCount);
		context.put("pages", pages);
		context.put("classification", classification);
		render(context);
	}

}

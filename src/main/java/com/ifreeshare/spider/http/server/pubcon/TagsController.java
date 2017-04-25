package com.ifreeshare.spider.http.server.pubcon;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.ifreeshare.framework.annotation.Controller;
import com.ifreeshare.framework.web.annotation.RequestMapping;
import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.SpiderUtils;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.page.Tags;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

@Controller("tagsController")
@RequestMapping(value = { "/public/tags/" })
public class TagsController {
	
	
	TransportClient client = IDataSearch.instance().getSearchClient();
	
	@RequestMapping(value = { "list/:id/" }, method = { HttpMethod.GET })
	public String search(RoutingContext context){
		context.put("domain", BaseRoute.DOMAIN);
		context.put("imgclassi", SpiderUtils.imageClassification);
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();

		String id = request.getParam("id");
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

		JsonObject tagsJson = (JsonObject) IDataSearch.instance().getValueById(CoreBase.TAGS, CoreBase.IMAGES, id);

		if (tagsJson == null) {
			return "template:templates/error/404.ftl";
		}

		String ckeywords = tagsJson.getString(CoreBase.CHINESE_KEYWORDS);
		String cdescription = tagsJson.getString(CoreBase.HTML_DESCRIPTION);
		String name = tagsJson.getString(CoreBase.NAME);
		
		Tags tags = new Tags();
		tags.setName(name);
		tags.setTitle(tagsJson.getString(CoreBase.HTML_TITLE));
		tags.setKeywords(tagsJson.getString(CoreBase.HTML_KEYWORDS));
		tags.setChkeywords(ckeywords);
		tags.setDescription(cdescription);
		tags.setEnkeywords(tagsJson.getString(CoreBase.ENGLISH_KEYWORDS));
		tags.setId(id);

		String tag = tagsJson.getString(CoreBase.TAG);
		

		SearchRequestBuilder srb = client.prepareSearch(CoreBase.INDEX_HTML).setTypes(CoreBase.TYPE_IMAGE);

		// if (keys != null && keys.trim().length() != 0) {
		QueryBuilder qb = QueryBuilders.matchQuery(CoreBase.TAGS, tag);
		srb.setQuery(qb);
		// }else{
		 srb.addSort(CoreBase.CREATE_DATE, SortOrder.DESC);
		// keys="";
		// }

		int pageFrom = pageIndex * pageSize;
		SearchResponse scrollResp = srb.setFrom(pageFrom).setSize(pageSize).get();

		SearchHits sh = scrollResp.getHits();
		long totalCount = sh.getTotalHits();
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
				result.add(pd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		DefaultPage<PageDocument> pages = new DefaultPage<PageDocument>(pageIndex, pageSize, result, totalCount);
		context.put("pages", pages);
		context.put("tags", tags);
		return "template:templates/images/tags/search.ftl";
	}
	
	
	
	
	@RequestMapping(value = { "page/" }, method = { HttpMethod.GET })
	public String page(RoutingContext context){
		
		HttpServerRequest request = context.request();
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

		SearchRequestBuilder srb = client.prepareSearch(CoreBase.TAGS).setTypes(CoreBase.IMAGES);
		
		srb.setQuery(QueryBuilders.termQuery(CoreBase.STATUS, 1));
		
		int pageFrom = pageIndex * pageSize;
		SearchResponse scrollResp = srb.setFrom(pageFrom).setSize(pageSize).get();

		SearchHits sh = scrollResp.getHits();
		long totalCount = sh.getTotalHits();
		List<Tags> result = new ArrayList<Tags>();
		for (SearchHit hit : sh.getHits()) {
			String uuid = hit.getId();
			JsonObject document = new JsonObject(hit.getSourceAsString());
			String keywords = document.getString(CoreBase.HTML_KEYWORDS);
			String description = document.getString(CoreBase.HTML_DESCRIPTION);
			String title = document.getString(CoreBase.HTML_TITLE);
			String enkeywords = document.getString(CoreBase.ENGLISH_KEYWORDS);
			String chkeywords = document.getString(CoreBase.CHINESE_KEYWORDS);
			String tag = document.getString(CoreBase.TAG);
			String name = document.getString(CoreBase.NAME);
			int status = document.getInteger(CoreBase.STATUS);

			Tags pd = new Tags();
			pd.setId(uuid);
			try {
				pd.setName(name == null ? "" : name);
				pd.setKeywords(keywords == null ? "" : keywords);
				pd.setStatus(status);
				pd.setTag(tag);
				pd.setDescription(description == null ? "" : description);
				pd.setChkeywords(chkeywords == null ? "" : chkeywords);
				pd.setEnkeywords(enkeywords == null ? "" : enkeywords);
				result.add(pd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		DefaultPage<Tags> pages = new DefaultPage<Tags>(pageIndex, pageSize, result, totalCount);
		context.put("domain", BaseRoute.DOMAIN);
		context.put("imgclassi", SpiderUtils.imageClassification);
		context.put("pages", pages);
		return "template:templates/images/tags/publist.ftl";


		
	}
	
	
	

}

package com.ifreeshare.spider.http.server.controller;

import io.vertx.core.Vertx;
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
import com.ifreeshare.spider.http.server.SpiderHttpServer;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.page.Tags;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.verticle.PersistenceVertical;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

@Controller("tagsController")
@RequestMapping(value = { "/admin/tags/" })
public class TagsController {

	TransportClient client = IDataSearch.instance().getSearchClient();

	@RequestMapping(value = { "list/" }, method = { HttpMethod.GET })
	public String list(RoutingContext context) {
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

		SearchRequestBuilder srb = client.prepareSearch(CoreBase.TAGS).setTypes(CoreBase.IMAGES);
		
		if (keys != null && keys.trim().length() != 0) {
			QueryBuilder qb = QueryBuilders.matchQuery(CoreBase.HTML_KEYWORDS, keys);
			srb.setQuery(qb);
		}else{
			keys="";
		}
		
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
			int status = document.getInteger(CoreBase.STATUS);

			Tags pd = new Tags();
			pd.setId(uuid);
			try {
				pd.setName(title == null ? "" : title);
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
		context.put("pages", pages);
		context.put("keys", keys == null ? "" : keys);

		return "template:templates/images/tags/list.ftl";

	}

	@RequestMapping(value = { "add/" }, method = { HttpMethod.POST })
	public void add(RoutingContext context) {

	}

	@RequestMapping(value = { "add/" }, method = { HttpMethod.GET })
	public void addPage(RoutingContext context) {

	}

	@RequestMapping(value = { "edit/:id/" }, method = { HttpMethod.GET })
	public String editPage(RoutingContext context) {
		HttpServerRequest request = context.request();
		String id = request.getParam(CoreBase.ID);
		JsonObject document = (JsonObject) IDataSearch.instance().getValueById(CoreBase.TAGS, CoreBase.IMAGES, id);
		String name = document.getString(CoreBase.NAME);
		String keywords = document.getString(CoreBase.HTML_KEYWORDS);
		String description = document.getString(CoreBase.HTML_DESCRIPTION);
		String title = document.getString(CoreBase.HTML_TITLE);
		String enkeywords = document.getString(CoreBase.ENGLISH_KEYWORDS);
		String chkeywords = document.getString(CoreBase.CHINESE_KEYWORDS);
		String tag = document.getString(CoreBase.TAG);
		int status = document.getInteger(CoreBase.STATUS);
		Tags pd = new Tags();
		pd.setId(id);
		pd.setName(name == null ? "" : name);
		pd.setKeywords(keywords == null ? "" : keywords);
		pd.setStatus(status);
		pd.setDescription(description == null ? "" : description);
		pd.setChkeywords(chkeywords == null ? "" : chkeywords);
		pd.setEnkeywords(enkeywords == null ? "" : enkeywords);
		pd.setTitle(title == null ? "" : title);
		pd.setTag(tag);
		
		context.put("imgclassi",SpiderUtils.imageClassification);
		context.put("tag", pd);
		return "template:templates/images/tags/edit.ftl";
	}

	@RequestMapping(value = { "edit/" }, method = { HttpMethod.POST })
	public String  edit(RoutingContext context) {
		HttpServerRequest request = context.request();
		
		String id  = request.getParam(CoreBase.ID);
		String name = request.getParam(CoreBase.NAME);
		String keywords = request.getParam(CoreBase.HTML_KEYWORDS);
		String description = request.getParam(CoreBase.HTML_DESCRIPTION);
		String parent = request.getParam(CoreBase.PARENT);
		
		JsonObject document = new JsonObject();
		document.put(CoreBase.UUID, id);
		document.put(CoreBase.NAME, name);
		document.put(CoreBase.PARENT, parent);
		document.put(CoreBase.HTML_KEYWORDS, keywords);
		document.put(CoreBase.HTML_DESCRIPTION, description);
		document.put(CoreBase.HTML_TITLE, request.getParam(CoreBase.HTML_TITLE));
		document.put(CoreBase.CHINESE_KEYWORDS, request.getParam(CoreBase.CHINESE_KEYWORDS));
		document.put(CoreBase.ENGLISH_KEYWORDS, request.getParam(CoreBase.ENGLISH_KEYWORDS));
		document.put(CoreBase.STATUS, Integer.parseInt(request.getParam(CoreBase.STATUS)));
		
		
		document.put(CoreBase.INDEX, CoreBase.TAGS);
		document.put(CoreBase.TYPE, CoreBase.IMAGES);
		document.put(CoreBase.OPERATE, CoreBase.OPERATE_U);
		
		CoreBase.vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, document);
		System.out.println(document.encodePrettily());
		
		return "redirect:/admin/tags/edit/"+id+"/";
		
	}

}

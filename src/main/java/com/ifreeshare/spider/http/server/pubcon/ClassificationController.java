package com.ifreeshare.spider.http.server.pubcon;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.ifreeshare.framework.annotation.Controller;
import com.ifreeshare.framework.web.annotation.RequestMapping;
import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.SpiderUtils;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.Classification;
import com.ifreeshare.spider.http.server.page.Tags;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

@Controller("tagsController")
@RequestMapping(value = { "/public/classic/" })
public class ClassificationController {
	
	TransportClient client = IDataSearch.instance().getSearchClient();
	
	@RequestMapping(value = { "tags/:id/" }, method = { HttpMethod.GET })
	public String tags(RoutingContext context){
		HttpServerRequest request = context.request();
		
		
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
		
		JsonObject classif = (JsonObject) IDataSearch.instance().getValueById(CoreBase.INDEX_CLASSIFICATION, CoreBase.TYPE_IMAGE, id);

		if (classif == null) {
		}

		String alias = classif.getString(CoreBase.ALIAS);
		String ckeywords = classif.getString(CoreBase.HTML_KEYWORDS);
		String cdescription = classif.getString(CoreBase.HTML_DESCRIPTION);
		String name = classif.getString(CoreBase.NAME);

		Classification classification = new Classification();
		classification.setId(classif.getString(CoreBase.ID));
		classification.setName(name);
		classification.setKeywords(ckeywords);
		classification.setDescription(cdescription);

		SearchRequestBuilder srb = client.prepareSearch(CoreBase.TAGS).setTypes(CoreBase.IMAGES);
		
		srb.setQuery(QueryBuilders.termQuery(CoreBase.PARENT, id));
		
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
			String tName = document.getString(CoreBase.NAME);
			int status = document.getInteger(CoreBase.STATUS);

			Tags pd = new Tags();
			pd.setId(uuid);
			try {
				pd.setName(tName == null ? "" : tName);
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
		context.put("classification", classification);
		context.put("pages", pages);
		return "template:templates/images/classif/tags.ftl";
	}
	
	

}

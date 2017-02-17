package com.ifreeshare.spider;

import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.server.page.Classification;

public class SpiderUtils {
	
	public static List<Classification> imageClassification = new ArrayList<Classification>();
	
	static{
		
//		TransportClient client = IDataSearch.instance().getSearchClient();
//		QueryBuilder qb = QueryBuilders.termQuery("parent", "0");
//		SearchResponse scrollResp = client.prepareSearch("classification").setTypes("image").setQuery(qb)
//		        .setQuery(qb)
//		        .setSize(100).get();
//		SearchHits sh = scrollResp.getHits();
//		long totalCount = sh.getTotalHits();
//		for (SearchHit hit : sh.getHits()) {
//			String id =  hit.getId();
//			JsonObject classification = new JsonObject(hit.getSourceAsString());
//			Classification classic = new Classification();
//			classic.setId(id);
//			classic.setName(classification.getString(CoreBase.NAME));
//			classic.setAlias(classification.getString(CoreBase.ALIAS));
//			imageClassification.add(classic);
//		}
		
		
	}

}

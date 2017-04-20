package com.ifreeshare.dht.pub.controller;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.ifreeshare.dht.crawler.structure.Info;
import com.ifreeshare.dht.crawler.structure.SubFile;
import com.ifreeshare.dht.crawler.structure.Torrent;
import com.ifreeshare.dht.crawler.util.StringUtil;
import com.ifreeshare.framework.annotation.Controller;
import com.ifreeshare.framework.web.annotation.RequestMapping;
import com.ifreeshare.persistence.elasticsearch.ElasticSearchSearch;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

@Controller("PublicController")
@RequestMapping(value = { "/" })
public class PublicController {
	
	ElasticSearchSearch ess = new ElasticSearchSearch();
	
	@RequestMapping(value = { "index.html" }, method = { HttpMethod.GET })
	public String index(RoutingContext context){
		TransportClient client = ess.getSearchClient();
		
		int pageIndex = 0;
		int pageSize = 10;
		SearchRequestBuilder srb = client.prepareSearch("torrent").setTypes("info");
		QueryBuilder qb = QueryBuilders.termQuery(CoreBase.STATUS, 1);
		srb.setQuery(qb);
		srb.addSort("creationDate", SortOrder.DESC);
		int pageFrom =  pageIndex*pageSize;
		SearchResponse scrollResp = srb.setFrom(pageFrom).setSize(pageSize).get();

		
		SearchHits sh = scrollResp.getHits();
		long totalCount = sh.getTotalHits();
		List<Torrent> result = new ArrayList<Torrent>();
		for (SearchHit hit : sh.getHits()) {
			JsonObject document = new JsonObject(hit.getSourceAsString());
			Torrent torrent = new Torrent();
			
			Info info = new Info();
			info.setName(document.getString("name"));
			info.setLength(document.getLong("fileSize"));
			torrent.setCreationDate(new Date(document.getLong("creationDate")));
			String subfileString = document.getString("subfiles");
			JsonArray subFileArrays = new JsonArray(subfileString);
			torrent.setInfo_hash(document.getString("info_hash"));
			torrent.setInfo(info);
			result.add(torrent);
		}

		DefaultPage<Torrent> pages = new DefaultPage<Torrent>(pageIndex, pageSize, result, totalCount);
		context.put("pages", pages);
		return "template:templates/index.ftl";
	}
	
	/**
	 * Search for magnetic links
	 * @param context
	 * @return
	 */
	@RequestMapping(value = { "search.html" }, method = { HttpMethod.GET })
	public String search(RoutingContext context){
		HttpServerRequest request = context.request();
		
		TransportClient client = ess.getSearchClient();

		String keys = request.getParam("keys");
		String index = request.getParam("index");
		String size = request.getParam("size");

		int pageIndex = 0;
		if ((index != null && RegExpValidatorUtils.IsIntNumber(index))) {
			pageIndex = Integer.parseInt(index);
		}

		int pageSize = 20;
		if (size != null && RegExpValidatorUtils.IsIntNumber(size)) {
			pageSize = Integer.parseInt(size);
		}
		
		SearchRequestBuilder srb = client.prepareSearch("torrent").setTypes("info");

		if (keys != null && keys.trim().length() != 0) {
			QueryBuilder qb = QueryBuilders.matchQuery(CoreBase.NAME, keys);
			QueryBuilder qb2 = QueryBuilders.termQuery(CoreBase.STATUS, 1);
			BoolQueryBuilder bqb = QueryBuilders.boolQuery().must(qb).must(qb2);
			srb.setQuery(bqb);
		}else{
			keys="";
			srb.addSort("creationDate", SortOrder.DESC);
			QueryBuilder qb = QueryBuilders.termQuery(CoreBase.STATUS, 1);
			srb.setQuery(qb);
		}
		
	

		int pageFrom =  pageIndex*pageSize;
		SearchResponse scrollResp = srb.setFrom(pageFrom).setSize(pageSize).get();

		SearchHits sh = scrollResp.getHits();
		long totalCount = sh.getTotalHits();
		List<Torrent> result = new ArrayList<Torrent>();
		for (SearchHit hit : sh.getHits()) {
			JsonObject document = new JsonObject(hit.getSourceAsString());
			Torrent torrent = new Torrent();
			
			Info info = new Info();
			info.setName(document.getString("name"));
			info.setLength(document.getLong("fileSize"));
			torrent.setCreationDate(new Date(document.getLong("creationDate")));
			String type = document.getString("filetype");
			torrent.setType( type == null ? "未知" : type );
			String subfileString = document.getString("subfiles");
			JsonArray subFileArrays = new JsonArray(subfileString);
			
			List<SubFile> files = new ArrayList<SubFile>();
			for (int i = 0; i < subFileArrays.size(); i++) {
					JsonObject subFile = subFileArrays.getJsonObject(i);
					SubFile file = new SubFile();
					file.setPath(subFile.getString("file_path"));
					file.setLength( subFile.getString("fileSize"));
					files.add(file);
			}
			
			torrent.setInfo_hash(document.getString("info_hash"));
			torrent.setInfo(info);
			info.setFiles(files);
			result.add(torrent);
		}

		DefaultPage<Torrent> pages = new DefaultPage<Torrent>(pageIndex, pageSize, result, totalCount);
		context.put("pages", pages);
		context.put("keys", keys);
		return "template:templates/search.ftl";
	}
	
	
	@RequestMapping(value = { "btinfo.html" }, method = { HttpMethod.GET })
	public String info(RoutingContext context){
		HttpServerRequest request = context.request();
		HttpServerResponse response = context.response();

		String hash = request.getParam("hash");
		if(hash == null){
			return "参数错误!";
		}else{
			JsonObject result = ess.getValueById("torrent", "info", hash);
			if(result == null){
				return "template:templates/error/404.ftl";
			}
			
			Torrent torrent = new Torrent();
			
			Info info = new Info();
			info.setName(result.getString("name"));
			info.setLength(result.getLong("fileSize"));
			String type = result.getString("filetype");
			torrent.setType( type == null ? "未知" : type );
			torrent.setCreationDate(new Date(result.getLong("creationDate")));
			String subfileString = result.getString("subfiles");
			JsonArray subFileArrays = new JsonArray(subfileString);
			
			List<SubFile> files = new ArrayList<SubFile>();
			for (int i = 0; i < subFileArrays.size(); i++) {
					JsonObject subFile = subFileArrays.getJsonObject(i);
					SubFile file = new SubFile();
					file.setPath(subFile.getString("file_path"));
					file.setLength( subFile.getString("fileSize"));
					files.add(file);
			}
			
			torrent.setInfo_hash(result.getString("info_hash"));
			torrent.setInfo(info);
			info.setFiles(files);
			
			context.put("sSize", StringUtil.formatSize((double)torrent.getInfo().getLength()));
			context.put("torrent", torrent);
		}
		return "template:templates/btinfo.ftl";
	}
	
	

}

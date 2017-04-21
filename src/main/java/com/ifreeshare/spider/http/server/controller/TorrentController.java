package com.ifreeshare.spider.http.server.controller;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.ifreeshare.dht.crawler.structure.Info;
import com.ifreeshare.dht.crawler.structure.SubFile;
import com.ifreeshare.dht.crawler.structure.Torrent;
import com.ifreeshare.framework.annotation.Controller;
import com.ifreeshare.framework.web.annotation.RequestMapping;
import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.persistence.elasticsearch.ElasticSearchPersistence;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.util.DefaultPage;
import com.ifreeshare.util.RegExpValidatorUtils;

@Controller("torrentController")
@RequestMapping(value = { "/admin/torrent/" })
public class TorrentController {
	TransportClient client = IDataSearch.instance().getSearchClient();
	
	ElasticSearchPersistence esp = new ElasticSearchPersistence();
	
	@RequestMapping(value = { "list/" }, method = { HttpMethod.GET })
	public String list(RoutingContext context){
		
		HttpServerRequest request = context.request();
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
			BoolQueryBuilder bqb = QueryBuilders.boolQuery().must(qb);
			srb.setQuery(bqb);
		}else{
			keys="";
			srb.addSort("creationDate", SortOrder.DESC);
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
			
			if(document.containsKey(CoreBase.STATUS)){
				torrent.setStatus(document.getInteger(CoreBase.STATUS));
			}else{
				torrent.setStatus(0);	
			}
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
		return "template:templates/admin/search.ftl";
	}
	
	@RequestMapping(value = { "update/" }, method = { HttpMethod.GET })
	public String show(RoutingContext context){
		JsonObject update = new JsonObject();
		HttpServerRequest request = context.request();
		String statusValue = request.getParam("status");
		int status = 1;
		if(statusValue != null && RegExpValidatorUtils.IsNumber(statusValue)){
			status = Integer.parseInt(statusValue);
			update.put(CoreBase.STATUS, status);
		}
		
		String rtype = request.getParam("rtype");
		
		if(rtype != null  && RegExpValidatorUtils.IsNumber(rtype) ){
			int rTypeValue = Integer.parseInt(rtype);
			update.put("rtype", rTypeValue);
		}
		
		update.put(CoreBase.UUID, request.getParam("hash"));
		update.put(CoreBase.INDEX, "torrent");
		update.put(CoreBase.TYPE, "info");
		
		esp.update(update);
		return "redirect:/admin/torrent/list/";
	}
	

}

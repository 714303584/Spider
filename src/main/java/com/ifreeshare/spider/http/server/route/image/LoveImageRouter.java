package com.ifreeshare.spider.http.server.route.image;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
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
public class LoveImageRouter extends BaseRoute {

	public LoveImageRouter() {
		super("/love/image/:itype/:otype/:uuid/", BaseRoute.GET, "templates/images/search.ftl");
	}

	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String iType = request.getParam(CoreBase.DATA_I_TYPE);
		String oType = request.getParam(CoreBase.DATA_O_TYPE);
		
		String uuid = request.getParam(CoreBase.UUID);
		
		SocketAddress address = request.remoteAddress();
		String remoteAddress = address.toString();
		System.out.println(remoteAddress);
		response.end();
	}

}

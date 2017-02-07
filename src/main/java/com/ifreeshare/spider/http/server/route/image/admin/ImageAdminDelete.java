package com.ifreeshare.spider.http.server.route.image.admin;

import java.io.File;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.ifreeshare.persistence.IDataSearch;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.http.server.page.PageDocument;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;
import com.ifreeshare.spider.verticle.PersistenceVertical;
/**
 * 
 * @author zhuss 
 */
public class ImageAdminDelete extends BaseRoute  {
	
	IDataSearch<JsonObject> searcher = IDataSearch.instance();
	String imageSavePath = Configuration.getConfig(CoreBase.IMAGES, CoreBase.STORAGE);
	String thumbnailPath = Configuration.getConfig(CoreBase.IMAGES, CoreBase.DOC_THUMBNAIL);
	
	public ImageAdminDelete() {
		super("/admin/image/delete/get/html/", BaseRoute.GET,"");
	}
	
	@Override
	public void process(RoutingContext context) {
		HttpServerRequest request =  context.request();
		HttpServerResponse response = context.response();
		String id = request.getParam(CoreBase.UUID);
		String referer =  request.getHeader("Referer");
		JsonObject document =  searcher.getValueById(CoreBase.INDEX_HTML, CoreBase.TYPE_IMAGE, id);
		if(document != null){
			Log.log(logger, Level.DEBUG, "router[%s],image[%s]", this.getUrl(), document);
			String uuid = document.getString(CoreBase.UUID);
			String keywords = document.getString(CoreBase.HTML_KEYWORDS);
			String description = document.getString(CoreBase.HTML_DESCRIPTION);
			String title = document.getString(CoreBase.HTML_TITLE);
			String thumbnail = document.getString(CoreBase.DOC_THUMBNAIL);
			String src = document.getString(CoreBase.FILE_URL_PATH);
			String thum = thumbnailPath+thumbnail;
			String filePath = imageSavePath + "/" + src;
			File thumFile = new File(thum);
			if(thumFile.exists() && thumFile.isFile()){
				Log.log(logger, Level.DEBUG, "router[%s], delete thumbnail[%s]", this.getUrl(), thum);
				thumFile.delete();
			}
			
			File realFile = new File(filePath);
			if(realFile.exists() && realFile.isFile()){
				realFile.delete();
				Log.log(logger, Level.DEBUG, "router[%s], delete image[%s]", this.getUrl(), filePath);
			}
			
			document.put(CoreBase.INDEX,CoreBase.INDEX_HTML);
			document.put(CoreBase.TYPE, CoreBase.TYPE_IMAGE);
			document.put(CoreBase.OPERATE, CoreBase.OPERATE_R);
			vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, document);
			doRedirect(response, referer);
		}else{
			response.end("NOT FOUND!");
		}
	}
}

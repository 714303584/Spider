package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;


import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.apache.bcel.generic.NEW;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;


import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.persistence.IDataPersistence;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.http.parse.AlphacodersComParser;
import com.ifreeshare.spider.http.parse.BaseParser;
import com.ifreeshare.spider.http.parse.HtmlParser;
import com.ifreeshare.spider.http.parse.JB51NetParser;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.verticle.msg.MessageType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
/**
 * @author zhuss
 * @date 2016-11-28PM5:57:38
 * @description  Request and parse HTML 
 */
public class SpiderHtmlVerticle extends AbstractVerticle {
	private static  Logger logger  = Log.register(SpiderHtmlVerticle.class.getName());
	
	FiberScheduler fs = null;

	public static final String WORKER_ADDRESS = "com.ifreeshare.spider.verticle.SpiderHtmlVerticle";
	
	Channel<JsonObject> urlsChannel = Channels.newChannel(10000);
	
	
	public static Map<String, HtmlParser> websiteMapParser = new HashMap<String, HtmlParser>();
	
	
	private long loadValue;
	
	OkHttpClient   sClient;

	public SpiderHtmlVerticle(Vertx vertx , Context context) {
		this.vertx = vertx;
		this.context = context;
		fs =  new  FiberForkJoinScheduler(WORKER_ADDRESS, 10000);
		
		sClient  = new FiberOkHttpClient();
		
		
		sClient.setConnectTimeout(2, TimeUnit.MINUTES);
		sClient.setReadTimeout(2, TimeUnit.MINUTES);
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[]{new X509TrustManager() {
			     @Override
			     public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			     }

			     @Override
			     public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			     }

			     @Override
			     public X509Certificate[] getAcceptedIssuers() {
			         return null;
			     }
			 }}, new SecureRandom());
			 sClient.setSslSocketFactory(sc.getSocketFactory());
			 sClient.setHostnameVerifier(new HostnameVerifier() {
			     @Override
			     public boolean verify(String hostname, SSLSession session) {
			         return true;
			     }
			 });
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		websiteMapParser.put("alphacoders.com", new AlphacodersComParser());
		websiteMapParser.put("jb51.net", new JB51NetParser());
		
	}
	
	
	@Override
	public void start() throws Exception {
		
		vertx.eventBus().consumer(WORKER_ADDRESS, message -> {
			JsonObject mbody = (JsonObject) message.body();
			processor(mbody);
		});
		
		processUrl();
		
		
	}

	

	@Override
	public void init(Vertx vertx, Context context) {
		
		
		
	}


	private void  processor(JsonObject message) {
		int type  =  message.getInteger(MessageType.MESSAGE_TYPE);
		switch (type) {
		case MessageType.URL_DISTR:
			urlDistr(message);
			break;

		default:
			break;
		}

	}
	
	public void  urlDistr(JsonObject message){
		try {
			boolean succ = urlsChannel.send(message, 10000, TimeUnit.SECONDS);
			if(!succ){
				JsonObject newURl = new JsonObject();
				newURl.put(MessageType.MESSAGE_TYPE, MessageType.Fail_URL);
				newURl.put(MessageType.MESSAGE_BODY, message);
				vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, newURl);
			}
			
		} catch (SuspendExecution e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method for processing HTML 
	 */
	private void processUrl() {
 		Fiber fiber = new Fiber(() -> {
				JsonObject message = null;
				while ((message = urlsChannel.receive()) != null) {
					try {
						JsonObject body = message.getJsonObject(MessageType.MESSAGE_BODY);  
						
						String url = body.getString(HttpUtil.URL);
						
						Request request = new Request.Builder().url(url).get().build();
						
						Response  response = sClient.newCall(request).execute();
						
						String html = null;
						
						String charset = body.getString(HttpUtil.CHARSET);
						byte[] b = response.body().bytes();
						if(charset == null){
							html = new String(b);
							 Document htmlDoc = Jsoup.parse(html);
							 Element charsetE = htmlDoc.select("meta[charset]").first();
							 if(charsetE != null) charset = charsetE.attr(HttpUtil.CHARSET);
							 if(charset == null){
								Elements chasetHttp_equiv =  htmlDoc.select("meta[http-equiv]");
								Iterator<Element> it = chasetHttp_equiv.iterator();
								while (it.hasNext()) {
									Element httpEquiv =  it.next();
									if(HttpUtil.Content_Type.equals(httpEquiv.attr("http-equiv"))){
										String[] types = httpEquiv.attr("content").split("=");
										if(types.length > 1) charset = types[1];
									}
								}
							 }
						}
						
						charset = charset == null ? "UTF-8" : charset;
						
						html = new String(b, charset);
						 
						 Document htmlDoc = Jsoup.parse(html);
						 htmlDoc.setBaseUri(url);
						 
//						 Element charsetE = htmlDoc.select("meta[charset]").first();
//						 if(charsetE != null) charset = charsetE.attr(HttpUtil.CHARSET);
						 
						 String domain = HttpUtil.getDomain(url);
						 HtmlParser parser = getParserByWebsite(domain);
						 Set<JsonObject> links = parser.getLinkValue(htmlDoc);
						 
//						 if(!message.containsKey(HttpUtil.HTML_TITLE)){
//							 message.put(HttpUtil.HTML_TITLE, parser.getTitle(htmlDoc));
//							 message.put(HttpUtil.HTML_KEYWORDS, parser.getKeywords(htmlDoc));
//							 message.put(HttpUtil.HTML_DESCRIPTION, parser.getDescription(htmlDoc));
//						 }
						 
						 body.put(IDataPersistence.INDEX, CoreBase.INDEX_HTML);
						 body.put(IDataPersistence.TYPE, CoreBase.TYPE_TEXTHTML);
						 body.put(CoreBase.CREATE_DATE, System.currentTimeMillis());
						 vertx.eventBus().send(PersistenceVertical.PERSISTENCE_VERTICAL_ADDRESS, body);
						 
						 message.put(MessageType.MESSAGE_TYPE, MessageType.SUCC_URL);
						 vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, message);
						 Iterator<JsonObject> it = links.iterator();
						 while (it.hasNext()) {
							 JsonObject href = it.next();
							 if(href != null){
								 JsonObject newUrl = new JsonObject();
								 newUrl.put(MessageType.MESSAGE_TYPE, MessageType.URL_DISTR);
								 newUrl.put(MessageType.MESSAGE_BODY, href);
								 vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, newUrl);
							 }else{
								 
							 }
						}
						Log.log(logger, Level.WARN, "html pase success ----------------------------- url:[%s];   e.message:[%s]", url,body);
					} catch (Exception e) {
						e.printStackTrace();
						Log.log(logger, Level.WARN, "html paser ----------------------------- Message:[%s];   e.message:[%s]", message,e.getMessage());
						JsonObject newURl = new JsonObject();
						newURl.put(MessageType.MESSAGE_TYPE, MessageType.Fail_URL);
						newURl.put(MessageType.MESSAGE_BODY, message);
						vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, newURl);
					}
				}
		});
 		
 		fiber.start();
	}
	
	
	
	
	/**
	 * Get
	 * @param webDomain
	 * @return
	 */
	public HtmlParser getParserByWebsite(String webDomain){
		HtmlParser parser =  websiteMapParser.get(webDomain);
		if(parser == null){
			Iterator<String> it = websiteMapParser.keySet().iterator();
			while (it.hasNext()) {
				String domain = it.next();
				if(webDomain.endsWith(domain)){
					Log.log(logger, Level.WARN, "return domain parser ----------------------------- parserKey:[%s];  webDomain:[%s]", domain,webDomain);
					parser = websiteMapParser.get(domain);
					websiteMapParser.put(webDomain, parser);
					return parser;
				}
			}
			if(parser == null){
				Log.log(logger, Level.WARN, "return default parser -----------------------------  webDomain:[%s]", webDomain);
				return new BaseParser();
			}
		}
		
		Log.log(logger, Level.WARN, "get webdomain parser ----------------------------- parserKey:[%s];  parserClass:[%s]", webDomain, parser.getClass().getName());
		return parser;
	}
	
	/**
	 * 
	 * @param webDomain Domain name 
	 * @param className  Parser name 
	 * @return
	 */
	public boolean registerParser(String webDomain,String className){
		boolean flag = false;
		try {
			Object obj = Class.forName(className).newInstance();
			if(obj instanceof HtmlParser){
				websiteMapParser.put(webDomain, (HtmlParser)obj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return flag;
		
	}
	
	
//	 org.apache.lucene.document.Document document = new  org.apache.lucene.document.Document();
//	 document.add( new Field(CoreBase.UUID, UUID.randomUUID().toString(), TextField.TYPE_STORED));
//	 document.add(new Field(CoreBase.HTML_KEYWORDS, message.getString(CoreBase.HTML_KEYWORDS), TextField.TYPE_STORED));
//	document.add(new Field(CoreBase.HTML_TITLE, message.getString(CoreBase.HTML_TITLE), TextField.TYPE_STORED));
//	document.add(new Field(CoreBase.HTML_DESCRIPTION, message.getString(CoreBase.HTML_DESCRIPTION), TextField.TYPE_STORED));
//		IndexWriter writer = LuceneFactory.getIndexWriter(OpenMode.CREATE);
//		writer.addDocument(document);
//		writer.flush();
//		writer.commit();
	
//	Map<String, List<String>> headers = response.headers().toMultimap();
//  	
//  	Iterator<String> keyIt = headers.keySet().iterator();
//  	
//  	while (keyIt.hasNext()) {
//  		String key = keyIt.next();
//  		System.out.println("-----------------key:"+key);
//  		Iterator<String> valIt = headers.get(key).iterator();
//  		while (valIt.hasNext()) {
//		String value =  valIt
//				.next();
//		
//		System.out
//				.println("value:"+value);
//		
//	}
//}
	
	
	
	
	

}

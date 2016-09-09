package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



import org.apache.logging.log4j.Logger;



import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;



import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.http.parse.AlphacodersComParser;
import com.ifreeshare.spider.http.parse.BaseParser;
import com.ifreeshare.spider.http.parse.HtmlParser;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.verticle.msg.MessageType;
import com.ifreeshare.util.DateUtil;
import com.ifreeshare.util.FileAccess;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SpiderFileVerticle extends AbstractVerticle {
	private static  Logger logger  = Log.register(SpiderFileVerticle.class.getName());
	

	public static final String WORKER_ADDRESS = "com.ifreeshare.spider.verticle.SpiderFileVerticle";
	
	Channel<JsonObject> urlsChannel = Channels.newChannel(10000);
	
	public static Map<String, HtmlParser> websiteMapParser = new HashMap<String, HtmlParser>();
	
	
	private String imageSavePath;
	
	private long loadValue;
	
	OkHttpClient   sClient;

	public SpiderFileVerticle(Vertx vertx , Context context) {
		this.vertx = vertx;
		this.context = context;
		
		sClient  = new FiberOkHttpClient();
		
		imageSavePath = "H:\\files";
		
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
	
	
	@Suspendable
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
	


	private void processUrl() {
 		Fiber fiber = new Fiber(() -> {
				JsonObject message = null;
				while ((message = urlsChannel.receive()) != null) {
					try {
						JsonObject body = message.getJsonObject(MessageType.MESSAGE_BODY);  
						
						String url = body.getString(HttpUtil.URL);
						
						Request request = new Request.Builder().url(url).get().build();
						
						Response  response = sClient.newCall(request).execute();
						
						InputStream is = response.body().byteStream();
						
						Date date = new Date();
						int year = DateUtil.getYear(date);
						int month = DateUtil.getMonth(date);
						int day = DateUtil.getDay(date);
						
						String todayImagePath = imageSavePath+"//"+year+""+month+""+day;
						
						if(FileAccess.createMkdir(todayImagePath)){
							String filename = body.getString("filename");
							if(filename == null){
								String[] imageType = url.split("/");
								filename = imageType[imageType.length-1];
							}
							
							String contentType = body.getString(HttpUtil.Content_Type);
							String fileType = HttpUtil.getFileType(contentType);
							
							
							String filePath = null;
							if(filename != null){
								filePath = todayImagePath + "/"+filename;
								if(fileType == null){
									String[] fileSplit =  filename.split("\\.");
									fileType = fileSplit[fileSplit.length-1];
								}
							}else{
								filePath = todayImagePath + "/" + UUID.randomUUID() + fileType;
							}
							
							File file = new File(filePath);
							if(file.exists()){
								filePath = todayImagePath + "/" + UUID.randomUUID() + fileType;
							}else{
								FileOutputStream os = new FileOutputStream(file);
							 	byte[] buffer = new byte[1024];
							  	int  byteRead = 0;
							  	while ((byteRead = is.read(buffer)) != -1) {
							  		os.write(buffer, 0, byteRead);
							  	}
							  	os.flush();
							  	os.close();
							}
							
							
							
						}else{
							
						}
						
					
//						FileInputStream fis = new FileInputStream();
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.log(logger, Level.WARN, "e.printStackTrace() ----------------------------- Message:%s;   e.message:%s", message,e.getMessage());
					}
					 message.put(MessageType.MESSAGE_TYPE, MessageType.SUCC_URL);
					 vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, message);
				}
				
				
				
				
//				Elements links = doc.getElementsByTag(JsoupUtil.LINK_A);
//				Iterator<Element> eleIt = links.iterator();
//				while(eleIt.hasNext()){
//					Element a = eleIt.next();
//					String href = a.attr(JsoupUtil.LINK_A_HREF);
//					JsonObject newURl = new JsonObject();
//					newURl.put(MessageType.MESSAGE_TYPE, MessageType.NEW_URL);
//					newURl.put(MessageType.MESSAGE_BODY, href);
//					vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, newURl);
//				}
//				
//				if(url.contains("item.taobao.com")){
//					System.out.println(url);
//					String title = doc.title();
//					System.out.println(title);
//					String[] urlSplit =  url.split("?");
//					if(urlSplit.length > 1){
//						String[] params = url.split("&");
//						String id = null;
//						for (int i = 0; i < params.length; i++) {
//							String param = params[i];
//							if(param.startsWith("id=")){
//								 id =  param.split("=")[1];
//								 break;
//							}
//							
//							if(id != null){
//								String iteminfourl = "https://detailskip.taobao.com/service/getData/1/p2/item/detail/sib.htm?itemId="+id
//										+ "&modules=qrcode,viewer,price,contract,duty,xmpPromotion,dynStock,delivery,upp,activity,fqg,zjys,coupon&callback=onSibRequestSuccess";
//								
//								Document priceDoc = Jsoup.connect(iteminfourl).header("Referer", "https://item.taobao.com/item.htm?id=534016703208").get();
//								System.out.println(priceDoc.html());
//							}
//						}
//						
//						
////					  	Elements metas = doc.getElementsByTag("meta");
////					  	Elements keywords = metas.attr("name","keywords"); 
////					  	Element keyword =  keywords.get(0);
////					  	System.out.println(keyword.html());
//					}
//				}
		});
 		
 		fiber.start();
	}
	
	
	
	
	
	public HtmlParser getParserByWebsite(String webDomain){
		HtmlParser parser =  websiteMapParser.get(webDomain);
		if(parser == null){
			parser = new BaseParser();
		}
		return parser;
	}
	
	
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

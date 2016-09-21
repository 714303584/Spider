package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.awt.Image;
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

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import com.ifreeshare.lucene.LuceneFactory;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.http.parse.AlphacodersComParser;
import com.ifreeshare.spider.http.parse.BaseParser;
import com.ifreeshare.spider.http.parse.HtmlParser;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;
import com.ifreeshare.spider.redis.RedisPool;
import com.ifreeshare.spider.verticle.msg.MessageType;
import com.ifreeshare.util.DateUtil;
import com.ifreeshare.util.FileAccess;
import com.ifreeshare.util.MD5Util;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SpiderImageVerticle extends AbstractVerticle {
	private static  Logger logger  = Log.register(SpiderImageVerticle.class.getName());

	private static String FILE_PATH = "FILE_PATH";
	private static String FILE_STATUS = "STATUS";

	public static final String WORKER_ADDRESS = "com.ifreeshare.spider.verticle.SpiderImageVerticle";
	
	Channel<JsonObject> urlsChannel = Channels.newChannel(10000);
	
	Channel<JsonObject> imageChannel = Channels.newChannel(100000);
	
	public static Map<String, HtmlParser> websiteMapParser = new HashMap<String, HtmlParser>();
	
	
	
	
	private String imageSavePath;
	
	private long loadValue;
	
	OkHttpClient   sClient;

	public SpiderImageVerticle(Vertx vertx , Context context) {
		this.vertx = vertx;
		this.context = context;
		
		sClient  = new FiberOkHttpClient();
		
		imageSavePath = "H:\\images";
		
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
//		processImage();
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
	
	
	
	private void processImage(){
		Fiber imageFiber = new Fiber(() -> {
			JsonObject imageJson = null;
			while ((imageJson = imageChannel.receive()) != null) {
				try {
					String uuid = UUID.randomUUID().toString();
					String imagePath = imageJson.getString(FILE_PATH);
					File file = new File(imagePath);
					if(file.exists() && file.isFile()){
						
						String sha512  = MD5Util.getFileSHA512(file);
						String Md5  = MD5Util.getFileMD5(file);
						String sha1  = MD5Util.getFileSHA1(file);
						
						String md5GetUuid = RedisPool.getFieldValue(CoreBase.MD5_UUID_IMAGE, Md5);
						String sha1GetUuid = RedisPool.getFieldValue(CoreBase.SHA1_UUID_IMAGE, sha1);
						String sha512GetUuid = RedisPool.getFieldValue(CoreBase.SHA512_UUID_IMAGE, sha512);
						
						if(md5GetUuid == null && sha1GetUuid == null && sha512GetUuid == null){
							Image image = ImageIO.read(file);
							int width = image.getWidth(null);
							int height = image.getHeight(null);
							
							String resolution = width+"X"+height;
							
							imageJson.put(CoreBase.RESOLUTION, resolution);
							imageJson.put(CoreBase.MD5, Md5);
							imageJson.put(CoreBase.UUID, uuid);
							imageJson.put(CoreBase.SHA1, sha1);
							
							RedisPool.addfield(CoreBase.MD5_UUID_IMAGE, Md5, uuid);
							RedisPool.addfield(CoreBase.SHA1_UUID_IMAGE, sha1, uuid);
							RedisPool.addfield(CoreBase.SHA512_UUID_IMAGE, sha512, uuid);
							RedisPool.addfield(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY, uuid, imageJson.toString());
							
							Document document = new Document();
							
							StringField uuidField = new StringField(CoreBase.UUID, uuid, Store.YES);
							document.add(uuidField);
							StringField keywordsField = new StringField(CoreBase.HTML_KEYWORDS, imageJson.getString(CoreBase.HTML_KEYWORDS), Store.YES);
							document.add(keywordsField);
							StringField titleField = new StringField(CoreBase.HTML_TITLE, imageJson.getString(CoreBase.HTML_TITLE), Store.YES);
							document.add(titleField);
							StringField descriptionField = new StringField(CoreBase.HTML_DESCRIPTION, imageJson.getString(CoreBase.HTML_DESCRIPTION), Store.YES);
							document.add(descriptionField);
							StringField resolutionField = new StringField(CoreBase.RESOLUTION, resolution, Store.YES);
							document.add(resolutionField);
							
							IndexWriter writer = LuceneFactory.getIndexWriter(OpenMode.CREATE);
							writer.addDocument(document);
							writer.flush();
							
						}else{
							RedisPool.addfield(CoreBase.MD5_SHA1_SHA512_EXIST_IMAGES_KEY, uuid, imageJson.toString());
						}
						
					}else{
						imageJson.put(FILE_STATUS, -1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					
				}finally{
					
				}
				
			}
			
		});
		
		imageFiber.start();
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
						
						String todayImagePath = imageSavePath+"//"+year+month+day;
						
						if(FileAccess.createMkdir(todayImagePath)){
							String filename = body.getString("filename");
							if(filename == null){
								String[] imageType = url.split("/");
								filename = imageType[imageType.length-1];
							}
							String filePath = null;
							if(filename != null){
								filePath = todayImagePath + "/"+filename;
							}else{
								String contentType = body.getString(HttpUtil.Content_Type);
								String fileType = HttpUtil.getFileType(contentType);
								filePath = todayImagePath + "/" + UUID.randomUUID() + fileType;
							}
							
							
							body.put(FILE_PATH, filePath);
							FileOutputStream os = new FileOutputStream(filePath);
							
							long fileSize = 0;
						 	byte[] buffer = new byte[1024];
						  	int  byteRead = 0;
						  	while ((byteRead = is.read(buffer)) != -1) {
						  		os.write(buffer, 0, byteRead);
						  		fileSize = fileSize + byteRead;
						  	}
						  	os.flush();
						  	os.close();
						  	body.put(CoreBase.FILE_SIZE, fileSize);
						}
//						FileInputStream fis = new FileInputStream();
						message.put(MessageType.MESSAGE_TYPE, MessageType.SUCC_URL);
						vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, message);
						imageChannel.send(body);
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.log(logger, Level.WARN, "e.printStackTrace() ----------------------------- Message:%s;   e.message:%s", message,e.getMessage());
						 message.put(MessageType.MESSAGE_TYPE, MessageType.SUCC_URL);
						 vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, message);
					}
					 
				}
				
				
				
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

}

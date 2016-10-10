package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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
import com.ifreeshare.util.ZipUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SpiderFileVerticle extends AbstractVerticle {
	private static  Logger logger  = Log.register(SpiderFileVerticle.class.getName());
	

	public static final String WORKER_ADDRESS = "com.ifreeshare.spider.verticle.SpiderFileVerticle";
	
	Channel<JsonObject> urlsChannel = Channels.newChannel(10000);
	
	Channel<JsonObject> fileChannel = Channels.newChannel(100000);
	
	public static Map<String, HtmlParser> websiteMapParser = new HashMap<String, HtmlParser>();
	
	private String FileSavePath;
	
	private String fileCachePath;
	
	private String filePdfPath;
	
	private long loadValue;
	
	OkHttpClient   sClient;

	public SpiderFileVerticle(Vertx vertx , Context context) {
		this.vertx = vertx;
		this.context = context;
		
		sClient  = new FiberOkHttpClient();
		
		FileSavePath = "H:\\files";
		fileCachePath = "H:\\fileCaches";
		filePdfPath = "H:\\filePdfs";
		
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
		
		processCompressedFile();
		
		
		processPdfFile();
	}


	private void processPdfFile() {
		
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
	
	/**
	 * Fiber process download file
	 */
	private void processCompressedFile() {
		Fiber compressedFileFiber = new Fiber(() -> {
			
			//Get a full-text index path for compressed files
			IndexWriter indexWriter = LuceneFactory.getIndexWriter("D://compressedfile");
			JsonObject fileInfo = null;
			while ((fileInfo = fileChannel.receive()) != null) {
				try {
					String filePath = fileInfo.getString(CoreBase.FILE_PATH);
					File sourcefile = new File(filePath);
					String fileName = sourcefile.getName();
					//download file type
					String fileType = FileAccess.getFileType(fileName);
					
					//SHA512 of file
					String sha512  = MD5Util.getFileSHA512(sourcefile);
					//Md5 of file
					String Md5  = MD5Util.getFileMD5(sourcefile);
					//sha1 of file
					String sha1  = MD5Util.getFileSHA1(sourcefile);
					
					String uuid = UUID.randomUUID().toString();
					
					
					Date date = new Date();
					int year = DateUtil.getYear(date);
					int month = DateUtil.getMonth(date);
					int day = DateUtil.getDay(date);
					
					String todayCachePath = fileCachePath+"//"+year+""+month+""+day;
					if(FileAccess.createMkdir(todayCachePath)){
						String filesCachePath = todayCachePath + "//" + uuid;
						FileAccess.createMkdir(filesCachePath);
						if(CoreBase.FILE_TYPE_RAR.equals(fileType)){
							ZipUtil.unrar(filePath, filesCachePath);
						}else if(CoreBase.FILE_TYPE_ZIP.equals(fileType)){
							ZipUtil.zipDecompressing(filePath, filesCachePath);
						}else if(CoreBase.FILE_TYPE_PDF.equals(fileType)){
							
//							String destName = null;
							
//							sourcefile.renameTo(dest)
							//Move the file to new path
//							FileAccess.Move(filePath, this.filePdfPath);
							
							// Save the file information to Redis
							saveToLucene(uuid, fileInfo);
							// Save the file information to Redis
							saveToRedis(uuid,Md5, sha1, sha512, fileInfo);
							continue;
						}else if(CoreBase.FILE_TYPE_DOC.equals(fileType)){
							
						}
						
						
						File cachePath = new File(filesCachePath);
						
						File[] decompreFiles = cachePath.listFiles();
						
						for (int i = 0; i < decompreFiles.length; i++) {
							File file = decompreFiles[i];
							String decopresFileName =  file.getName();
							//The type of file after decompression
							String decompresFileType = FileAccess.getFileType(decopresFileName);
							if(CoreBase.FILE_TYPE_PDF.equals(decompresFileType)){
								
							}
							
						}
						
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}

	
	public void processPdf(JsonObject fileInfo){
		
		
		
		
	}
	
	/**
	 * Add Data Information Full Text Search
	 * @param uuid 
	 * @param fileInfo
	 * @throws IOException
	 */
	public void saveToLucene(String uuid,JsonObject fileInfo) throws IOException{
		Document document = new  Document();
		document.add( new StringField(CoreBase.UUID, uuid, Store.YES));
		document.add( new StringField(CoreBase.FILE_PATH, fileInfo.getString(CoreBase.FILE_PATH), Store.YES));
		document.add(new Field(CoreBase.HTML_KEYWORDS, fileInfo.getString(CoreBase.HTML_KEYWORDS), TextField.TYPE_STORED));
		document.add(new Field(CoreBase.HTML_TITLE, fileInfo.getString(CoreBase.HTML_TITLE), TextField.TYPE_STORED));
		document.add(new Field(CoreBase.HTML_DESCRIPTION, fileInfo.getString(CoreBase.HTML_DESCRIPTION), TextField.TYPE_STORED));
		IndexWriter writer = LuceneFactory.getIndexWriter(OpenMode.CREATE);
		writer.addDocument(document);
		writer.flush();
		writer.commit();
	}
	
	/**
	 * Save the file information to Redis
	 * @param uuid The UUID Of File, Uniqueness
	 * @param md5  The MD5 Of File , Uniqueness
	 * @param sha1 The SHA1 Of File , Uniqueness
	 * @param sha512 The SHA512 Of File , Uniqueness
	 * @param json the File Information
	 */
	private void saveToRedis(String uuid, String md5,String sha1,String sha512,JsonObject json){
		RedisPool.addfield(CoreBase.MD5_UUID_FILE, md5, uuid);
		RedisPool.addfield(CoreBase.SHA1_UUID_FILE, sha1, uuid);
		RedisPool.addfield(CoreBase.SHA512_UUID_FILE, sha512, uuid);
		RedisPool.addfield(CoreBase.UUID_MD5_SHA1_SHA512_FILE_KEY, uuid, json.toString());
	}
	
	/**
	 * if the url is download a file, the Function process
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
						
						InputStream is = response.body().byteStream();
						
						Date date = new Date();
						int year = DateUtil.getYear(date);
						int month = DateUtil.getMonth(date);
						int day = DateUtil.getDay(date);
						
						String todayImagePath = FileSavePath+"//"+year+""+month+""+day;
						
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
							
							String localFileName = file.getName();
							String localFileType = FileAccess.getFileType(localFileName);
							message.put(CoreBase.FILE_PATH, filePath);
							// File Type ---get--> Channel
							getChannelByFileType(fileType).send(message);
//							fileChannel.send(message);
						}else{
							
						}
					} catch (Exception e) {
						e.printStackTrace();
						Log.log(logger, Level.WARN, "e.printStackTrace() ----------------------------- Message:%s;   e.message:%s", message,e.getMessage());
					}
					 message.put(MessageType.MESSAGE_TYPE, MessageType.SUCC_URL);
					 vertx.eventBus().send(SpiderMainVerticle.MAIN_ADDRESS, message);
				}
		});
 		
 		fiber.start();
	}
	
	
	/**
	 * Get Channel By File Type
	 * @param fileType  Need to Process the type of file
	 * @return
	 */
	public Channel getChannelByFileType(String fileType){
		switch (fileType) {
		case CoreBase.FILE_TYPE_RAR:
		case CoreBase.FILE_TYPE_ZIP:
			return fileChannel;

		default:
			break;
		}
		return null;
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

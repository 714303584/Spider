package com.ifreeshare.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ClImageGet {
	
	
	public static Set<String> imageSet = new HashSet<String>();
	
	
	public static void main(String[] args) {
		
    	System.setProperty("co.paralleluniverse.fibers.detectRunawayFibers","false");
		
		final Channel<Integer> channel = Channels.newChannel(10000);
		
		
		Fiber fiber = new Fiber(()->{
			
			
			MediaType MEDIA_TYPE_MARKDOWN
	        = MediaType.parse("text/x-markdown; charset=gb2312");
			
			OkHttpClient   sClient;
			
			
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
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
			
			
				 
				 
			
			
			
			int i = -1;
			while((i = channel.receive()) > -1){
				
				try {
					 String url = "http://cl.iqiyf.com/thread0806.php?fid=8&search=&page="+i;
					Request request = new Request.Builder().url(url).get().build();
					
					Response response = sClient.newCall(request).execute();
				  	byte[] responseBodyString = response.body().bytes();
				  	
				  	String responseValue =  new String(responseBodyString, "gb2312");
				  	
				  	
				  	
				  	Document doc = Jsoup.parse(responseValue);
				  	doc.title();
				  	
				  	
				  	Elements as = doc.getElementsByTag("a");
				  	
				  	Iterator<Element> ait = as.iterator();
				  	
				  	while (ait.hasNext()) {
						Element aEle = ait.next();
						
						String aHref = aEle.attr("href");
						
						if (aHref != null && aHref.startsWith("htm_data") && !imageSet.contains(aHref)) {
							imageSet.add(aHref);
							System.out.println(aHref);
							String topicUrl = "http://cl.iqiyf.com/"+aHref;
							
							Request topicRequest = new Request.Builder().url(topicUrl).get().build();
							
							Response topicResponse  = sClient.newCall(topicRequest).execute();
							
							byte[] topicResponseByte = topicResponse.body().bytes();
						  	String topicResponseString =  new String(topicResponseByte, "gb2312");
						  	
							Document topicDoc = Jsoup.parse(topicResponseString);
						  	
						  	Elements titles = topicDoc.getElementsByTag("title");
						  	Element title =  titles.get(0);
						  	String name = title.text().split(" ")[0];
						  	
						  	System.err.println(name);
						  	
						  	File file = new File("H:\\clImage\\"+name);
						  	
						  	if(!file.exists()){
						  		
						  		file.mkdir();
						  		
						  		
						  		Elements inputs = topicDoc.getElementsByTag("input");
						  		Elements images = inputs.attr("type", "image");
						  		
						  		Iterator<Element> it = images.iterator();
						  		while (it.hasNext()) {
						  			Element imageElement = it.next();
						  			
						  			String imageUrl = imageElement.attr("src");
						  			
						  			if (imageUrl != null && imageUrl.trim().length() > 0) {
						  				System.out.println(imageUrl);
						  				
						  				String[] imageType = imageUrl.split("/");
						  				
						  				Request imageRequest = new Request.Builder().url(imageUrl).get().build();
						  				
						  				Response response2 = sClient.newCall(imageRequest).execute();
						  			  	String imageName = imageType[imageType.length-1];
						  			  	
						  			  	Map<String, List<String>> headers = response2.headers().toMultimap();
						  			  	
						  			  	Iterator<String> keyIt = headers.keySet().iterator();
						  			  	
						  			  	while (keyIt.hasNext()) {
						  			  		String key = keyIt.next();
						  			  		System.out.println("-----------------key:"+key);
						  			  		Iterator<String> valIt = headers.get(key).iterator();
						  			  		while (valIt.hasNext()) {
												String value =  valIt
														.next();
												
												System.out
														.println("value:"+value);
												
											}
										}
						  			  	
						  			  	
						  			  	String imagePath = file.getAbsolutePath()+"\\"+imageName; 
						  			  	System.out.println(imagePath);
						  			  	
						  			  	OutputStream  outputStream = new FileOutputStream(imagePath);
						  			  	
//						  			  	byte[] imgByte = response.body().bytes();
						  			  	
						  			  	InputStream ins = response2.body().byteStream(); 
						  			  	
						  			  	byte[] buffer = new byte[1024];
						  			  	
//						  			  	outputStream.write(buffer, 0, imgByte.length);
						  			  	
						  			  	int  byteRead = 0;
						  			  	
						  			  	while ((byteRead = ins.read(buffer)) != -1) {
						  			  		outputStream.write(buffer, 0, byteRead);
										}
//						  			  	
						  			  	
						  			  	outputStream.flush();
						  			  	outputStream.close();
						  				
						  				
						  				
									}
									
								}
						  		
						  		
						  	}
							
						}
						
					}
				  	
				} catch (Exception e) {
					e.printStackTrace();
					channel.send(i);
				}
				
				
			}
			
			
			
		});
		
		fiber.start();
		
		
		
		
		new Fiber(() -> {
			MediaType MEDIA_TYPE_MARKDOWN
	        = MediaType.parse("text/x-markdown; charset=gb2312");
			
			OkHttpClient   sClient;
			
			
			sClient  = new FiberOkHttpClient();
			
			sClient.setConnectTimeout(2, TimeUnit.MINUTES);
			sClient.setReadTimeout(2, TimeUnit.MINUTES);
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				
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
				 
				 
				 
				 
				 for (int i = 0; i < 420; i++) {
						try {
							
							System.out.println("-----------------------------"+i);
							 String url = "http://cl.iqiyf.com/thread0806.php?fid=8&search=&page="+i;
							Request request = new Request.Builder().url(url).get().build();
							
							Response response = sClient.newCall(request).execute();
						  	byte[] responseBodyString = response.body().bytes();
						  	
						  	String responseValue =  new String(responseBodyString, "gb2312");
						  	
						  	
						  	
						  	Document doc = Jsoup.parse(responseValue);
						  	
						  	
						  	Elements as = doc.getElementsByTag("a");
						  	
						  	Iterator<Element> ait = as.iterator();
						  	
						  	while (ait.hasNext()) {
								Element aEle = ait.next();
								
								String aHref = aEle.attr("href");
								
								if (aHref != null && aHref.startsWith("htm_data") && !imageSet.contains(aHref)) {
									imageSet.add(aHref);
									System.out.println(aHref);
									String topicUrl = "http://cl.iqiyf.com/"+aHref;
									
									Request topicRequest = new Request.Builder().url(topicUrl).get().build();
									
									Response topicResponse  = sClient.newCall(topicRequest).execute();
									
									byte[] topicResponseByte = topicResponse.body().bytes();
								  	String topicResponseString =  new String(topicResponseByte, "gb2312");
								  	
									Document topicDoc = Jsoup.parse(topicResponseString);
								  	
								  	Elements titles = topicDoc.getElementsByTag("title");
								  	Element title =  titles.get(0);
								  	String name = title.text().split(" ")[0];
								  	
								  	System.err.println(name);
								  	
								  	File file = new File("H:\\clImage\\"+name);
								  	
								  	if(!file.exists()){
								  		
								  		file.mkdir();
								  		
								  		
								  		Elements inputs = topicDoc.getElementsByTag("input");
								  		Elements images = inputs.attr("type", "image");
								  		
								  		Iterator<Element> it = images.iterator();
								  		while (it.hasNext()) {
								  			Element imageElement = it.next();
								  			
								  			String imageUrl = imageElement.attr("src");
								  			
								  			if (imageUrl != null && imageUrl.trim().length() > 0) {
								  				System.out.println(imageUrl);
								  				
								  				String[] imageType = imageUrl.split("/");
								  				
								  				Request imageRequest = new Request.Builder().url(imageUrl).get().build();
								  				
								  				Response response2 = sClient.newCall(imageRequest).execute();
								  			  	String imageName = imageType[imageType.length-1];
								  			  	
								  			  	Map<String, List<String>> headers = response2.headers().toMultimap();
								  			  	
								  			  	Iterator<String> keyIt = headers.keySet().iterator();
								  			  	
								  			  	while (keyIt.hasNext()) {
								  			  		String key = keyIt.next();
								  			  		System.out.println("-----------------key:"+key);
								  			  		Iterator<String> valIt = headers.get(key).iterator();
								  			  		while (valIt.hasNext()) {
														String value =  valIt
																.next();
														
														System.out
																.println("value:"+value);
														
													}
												}
								  			  	
								  			  	
								  			  	String imagePath = file.getAbsolutePath()+"\\"+imageName; 
								  			  	System.out.println(imagePath);
								  			  	
								  			  	OutputStream  outputStream = new FileOutputStream(imagePath);
								  			  	
//								  			  	byte[] imgByte = response.body().bytes();
								  			  	
								  			  	InputStream ins = response2.body().byteStream(); 
								  			  	
								  			  	byte[] buffer = new byte[1024];
								  			  	
//								  			  	outputStream.write(buffer, 0, imgByte.length);
								  			  	
								  			  	int  byteRead = 0;
								  			  	
								  			  	while ((byteRead = ins.read(buffer)) != -1) {
								  			  		outputStream.write(buffer, 0, byteRead);
												}
//								  			  	
								  			  	
								  			  	outputStream.flush();
								  			  	outputStream.close();
								  				
								  				
								  				
											}
											
										}
								  		
								  		
								  	}
									
								}
								
								
							}
							
							
						} catch (Exception e) {
							channel.send(i);
							e.printStackTrace();
						}
					
				}
				 
				 
				 
				 
				 	
				 
				 
				 
				 
				 
				
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
		}).start();
		
		
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				
				while (true) {
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
				}
			}
		});
		
		thread.start();
		
	}
	
	
	
	
	
	

}

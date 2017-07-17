package com.ifreeshare.spider;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class CLR {
	

	
	
	
	
	public static void main(String[] args) {
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
			e1.printStackTrace();
		}
		
		
		String uri = "http://cl.gtta.pw/htm_data/2/1703/2300199.html";
		try {
			Request request = new Request.Builder().url(uri).build();
			Response response = sClient.newCall(request).execute();
			
			byte[] responseBodyString = response.body().bytes();
			
			String responseValue =  new String(responseBodyString, "gb2312");
			System.out.println("statusCode:"+response.code());
			Map<String, List<String>> headers =  response.headers().toMultimap();
			Iterator<String> it = headers.keySet().iterator();
			
			while (it.hasNext()) {
				String key = it.next();
				System.out.println("keys:" + key);
				Iterator<String> vIt = headers.get(key).iterator();
				while (vIt.hasNext()) {
					System.out.println(vIt.next());
				}
			}
			
			
			
			
			System.out.println(responseValue);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
			 
			 
		
	}

}

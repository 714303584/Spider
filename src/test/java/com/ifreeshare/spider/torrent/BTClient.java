package com.ifreeshare.spider.torrent;

import io.vertx.core.json.JsonObject;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class BTClient {
	
	public static  OkHttpClient   sClient;
	
	
	static {
		
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
	}
	
	
	public static void main(String[] args) {
		
		
	}
	
	
	
	public byte[] tracker(Torrent torrent){
		List<String> urls = torrent.getHttpUrls();
		
		for (int i = 0; i < urls.size(); i++) {
			String url = urls.get(i);
			System.err.println(url);
			try {
				byte[] peerId = getPeerId();
			 	byte[] b = getBytes(0, 20, torrent.getInfo().getPieces());
//			 	url = url +"?peer_id=" + peerId + "&Info_ hash=" + get16String(b) + "&port=6881&uploaded=0&Downloaded=0" ;
			 	
			 	HttpUrl httpurl = HttpUrl.parse(url);
			 	
			 	HttpUrl.Builder urlBuilder = new HttpUrl.Builder()

				.scheme(httpurl.scheme())

				.host(httpurl.host())

				.port(httpurl.port())
				
				.addPathSegment(httpurl.encodedPath().substring(1))

				.addEncodedQueryParameter("info_hash",hexEncode(b))

				.addEncodedQueryParameter("peer_id", hexEncode(peerId))

				.addQueryParameter("port", "6881")

				// TODO Add support for IP field

				.addQueryParameter("uploaded", "0")

				.addQueryParameter("downloaded", "0")

				// TODO Add support for BEP-23, for now enforce non-compact results.

				.addQueryParameter("compact-mode", "0")
				.addQueryParameter("left", "0");
			 	
			 			System.out.println(urlBuilder.build().encodedQuery());
			 	Request request = new Request.Builder().url(urlBuilder.build()).build();
				
				Response  response = sClient.newCall(request).execute();
				
				System.out.println(response.code());
				
				if(response.code() == 200){
				 	byte[] resByte = response.body().bytes();
					ByteArrayInputStream	bin = new ByteArrayInputStream(resByte);
					System.out.println(new String(resByte));
					Map<String, Object> map =  (Map<String, Object>) BenCodingParse.parseBenCoding(bin);
					JsonObject jsonObject = new JsonObject(map);
					
					System.out.println(jsonObject.encodePrettily());
				}else{
					System.out.println(response.body().string());
				}
				
				
				
				
			} catch (Exception e) {
				System.out.println(url);
				e.printStackTrace();
			}
			
		}
		
		return null;
		
	}
	
	
	
	private String hexEncode(byte[] bytes) {

		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : bytes) {
			stringBuilder.append("%");
			String encoded = Integer.toUnsignedString(Byte.toUnsignedInt(b), 16);
			if (encoded.length() == 1) {
				stringBuilder.append("0");
			}
			stringBuilder.append(encoded);
		}
		return stringBuilder.toString();

	}
	
	
	public static  String get16String(byte[] b){
		BigInteger bigInteger = new BigInteger(b);
		return bigInteger.toString(16);
	}
	
	
	public static byte[] getBytes(int from, int len, byte[] bytes){
		byte[] result = new byte[len];
		int end = from + len;
		int index = 0;
		for (; from < end; from++) {
			byte b = bytes[from];
			result[index] = b;
			index ++;
		}
		return result;
	}
	
	
	public static  byte[] getPeerId(){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			digest.update(UUID.randomUUID().toString().getBytes());
			byte[] b = digest.digest();
			BigInteger bigInt = new BigInteger(1, b);
			return b;
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	

}

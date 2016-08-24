package com.ifreeshare.spider;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;

import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;

import com.ifreeshare.spider.http.HttpUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class JsoupTest {

	static CookieStore cookieStore = null; 
	static HttpClientContext context = null; 
	
	public static void main(String[] args) throws IOException {
		
		
		 String urlS = "http://xz6.jb51.net:81/201607/books/JMeterzwsc_jb51.rar";
		
		
		
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
			 
			 
			 
			 Request request = new Request.Builder().url(urlS).head().build();
				
			 Response response = sClient.newCall(request).execute();
			 
			String contentType =  response.header("Content-Type");
			
			String fileType = null;


			
			Map<String, List<String>> headers = response.headers().toMultimap();
			  	
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
			
			
			if(contentType.startsWith("\"")){
				contentType = contentType.substring(1, contentType.length()-1);
			}
			
			
		 	fileType =  HttpUtil.getFileType(contentType);
			
		 	if(fileType == null || fileType.length() == 0){
		 		String[] urlSp = urlS.split("\\.");
		 		
		 		fileType = "."+urlSp[urlSp.length-1];
		 		
		 	}
			
			
			String contentLength =  response.header("Content-Length");
			
			String imagePath = "D:\\aaaa"+fileType;
			
			System.out.println(response.body().string());
		  	
//			  	OutputStream  outputStream = new FileOutputStream(imagePath);
//			  	
////			  	byte[] imgByte = response.body().bytes();
//			  	
//			  	InputStream ins = response.body().byteStream(); 
//			  	
//			  	BufferedInputStream bis = new BufferedInputStream(ins);
//			  	
//			  	
//			  	byte[] buffer = new byte[1024];
//			  	
////			  	outputStream.write(buffer, 0, imgByte.length);
//			  	
//			  	
//			  	int  byteRead = 0;
//			  	
//			  	while ((byteRead = ins.read(buffer)) != -1) {
//			  		outputStream.write(buffer, 0, byteRead);
//			}
////			  	
//			  	
//			  	outputStream.flush();
//			  	outputStream.close();
//				
				
			
			
			 
			 
			 
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		Connection connction = Jsoup.connect("https://sec.taobao.com/query.htm?action=QueryAction&amp;event_submit_do_css=ok&amp;smApp=list&amp;smPolicy=list-list_itemlist_jsonapi-anti_Spider-checklogin&amp;smCharset=utf-8&amp;smTag=MTExLjIwNi44Ny4xODIsLDYwMTA0ZjI1NzA1YzQ4OWZhZTM0NDFhNGMyZjYzNzEz&amp;smReturn=https%3A%2F%2Flist.taobao.com%2Fitemlist%2Fmini%2Flist.htm%3FpSize%3D60%26json%3Don%26_input_charset%3Dutf-8%26spm%3Da21bo.50862.201867-links-0.3.zdRzFD%26oetag%3D%25206745%26seller_type%3Dtaobao%26_ksTS%3D1467885477819_840%26callback%3Djsonp841&amp;smSign=XLYOcft3jYUBs8EnYd23Fw%3D%3D");
//		
//		connction.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
//		connction.header("Accept-Encoding", "gzip, deflate, br");
//		connction.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//		connction.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		connction.header("Connection", "keep-alive");
//		connction.header("Referer", "https://style.taobao.com/?spm=a21bo.50862.201867-links-0.3.zdRzFD");
		
		
		
//		Document res = connction.get();
//		
//		
//		System.out.println(res.headers());
//		System.out.println(res.cookies());
		
//		System.out.println(res.html());
		
//		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println(dataFormat.format(new Date(1467907199000L)));
		
		
//		CloseableHttpClient client = HttpClients.createDefault(); 
//		
//		HttpGet get = new HttpGet("https://detailskip.taobao.com/service/getData/1/p2/item/detail/sib.htm?itemId=534016703208&modules=qrcode,viewer,price,contract,duty,xmpPromotion,dynStock,delivery,upp,activity,fqg,zjys,coupon&callback=onSibRequestSuccess");
//		
//		get.setHeader("Referer", "");
//		CloseableHttpResponse response =  client.execute(get);
//		
//		Header[] headers = response.getAllHeaders();
//		for (int i = 0; i < headers.length; i++) {
//			Header header = headers[i];
//			System.out.println(header.getName()+"-------------"+header.getValue());
//			
//		}
//		 
//		
//		 System.out.println(EntityUtils.toString(response.getEntity()));
		 
		
		
		
		
		
	}
}

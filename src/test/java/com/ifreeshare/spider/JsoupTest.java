package com.ifreeshare.spider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import co.paralleluniverse.fibers.okhttp.FiberOkHttpClient;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class JsoupTest {

	static CookieStore cookieStore = null;
	static HttpClientContext context = null;

	public static void main(String[] args) throws IOException {

		Properties tags = new Properties();
		String urlS = "https://alphacoders.com/tags?page=";

		OkHttpClient sClient;

		sClient = new FiberOkHttpClient();

		sClient.setConnectTimeout(2, TimeUnit.MINUTES);
		sClient.setReadTimeout(2, TimeUnit.MINUTES);
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new X509TrustManager() {
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
			} }, new SecureRandom());
			sClient.setSslSocketFactory(sc.getSocketFactory());
			sClient.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			String nextPage = urlS;
			for (int i = 2; i < 168; i++) {
				System.out.println(nextPage);
				Request request = new Request.Builder().url(nextPage).get().build();
				Response response = sClient.newCall(request).execute();
				String string = new String(response.body().bytes());

				Document document = Jsoup.parse(string);
				document.setBaseUri(urlS);
				
				Elements tables = document.getElementsByClass("panel-primary");
				Iterator<Element> it = tables.iterator();
				while (it.hasNext()) {
					Element next = it.next();
					Elements links = next.getElementsByTag("a");
					Iterator<Element> linkIt = links.iterator();
					while (linkIt.hasNext()) {
						String text = linkIt.next().ownText();
						tags.put(text, "a");
						System.out.println(text);
					}	
				}
				Thread.sleep(2000);
				
				nextPage = urlS + i;
			}
			
			tags.store(new FileOutputStream("dic.properties"), "all tag");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}

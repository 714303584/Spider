package com.ifreeshare.spider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupTest {

	static CookieStore cookieStore = null; 
	static HttpClientContext context = null; 
	
	public static void main(String[] args) throws IOException {
		
		Connection connction = Jsoup.connect("https://sec.taobao.com/query.htm?action=QueryAction&amp;event_submit_do_css=ok&amp;smApp=list&amp;smPolicy=list-list_itemlist_jsonapi-anti_Spider-checklogin&amp;smCharset=utf-8&amp;smTag=MTExLjIwNi44Ny4xODIsLDYwMTA0ZjI1NzA1YzQ4OWZhZTM0NDFhNGMyZjYzNzEz&amp;smReturn=https%3A%2F%2Flist.taobao.com%2Fitemlist%2Fmini%2Flist.htm%3FpSize%3D60%26json%3Don%26_input_charset%3Dutf-8%26spm%3Da21bo.50862.201867-links-0.3.zdRzFD%26oetag%3D%25206745%26seller_type%3Dtaobao%26_ksTS%3D1467885477819_840%26callback%3Djsonp841&amp;smSign=XLYOcft3jYUBs8EnYd23Fw%3D%3D");
		
		connction.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		connction.header("Accept-Encoding", "gzip, deflate, br");
		connction.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		connction.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connction.header("Connection", "keep-alive");
		connction.header("Referer", "https://style.taobao.com/?spm=a21bo.50862.201867-links-0.3.zdRzFD");
		
		
		
		Document res = connction.get();
//		
//		
//		System.out.println(res.headers());
//		System.out.println(res.cookies());
		
		System.out.println(res.html());
		
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

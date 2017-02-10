package com.ifreeshare.spider.http.parse;

import io.vertx.core.json.JsonObject;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.http.server.SpiderHttpServer;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.util.CharUtil;
import com.ifreeshare.util.PropertiesUtil;

/**
 * Analysis of web pages 
 * The use of the tool is JSOUP 
 * @author zhuss
 */
public class BaseParser implements HtmlParser {
	
	protected static  Logger logger  = Log.register(BaseParser.class.getName());
	
	public static final String KEYWORD_SEPARATOR = ",";
	
	public static final String A = "a";
	
	public static final String META= "meta";
	
	static Properties dic = null;
	
	static{
		try {
			dic = PropertiesUtil.getProperties(BaseParser.class.getResourceAsStream("/dic.properties"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);;
		} 
	}

	/**
	 * Gets the title of the web page 
	 */
	@Override
	public String getTitle(Document doc) {
		String title = null;
		Element ele =  doc.getElementsByTag("title").first();
		if(ele != null) title = ele.text();
		return title;
	}

	/**
	 * Get the keyword for the web page 
	 */
	@Override
	public String getKeywords(Document doc) {
		return doc.head().select("meta[name=keywords]").attr("content");
	}

	/**
	 * Get a description of the web page 
	 */
	@Override
	public String getDescription(Document doc) {
		return doc.head().select("meta[name=description]").attr("content");
	}

	/**
	 * Gets the hyperlink to the web page 
	 */
	@Override
	public Elements getLinks(Document doc) {
		return doc.getElementsByTag(A);
	}

	@Override
	public Elements getImages(Document doc) {
		return null;
	}

	/**
	 * Access to web pages 
	 */
	@Override
	public Set<JsonObject> getLinkValue(Document doc) {
		HashSet<JsonObject> results = new HashSet<JsonObject>();
		Iterator<Element> it = doc.select("a[href]").iterator();
		HashSet<String> links = new HashSet<>();
		while (it.hasNext()) {
			Element ele = it.next();
			String link = ele.attr("abs:"+HttpUtil.LINK_A_HREF);
			String value = ele.attr(HttpUtil.LINK_A_HREF);
			if(link.startsWith("http") && !"#".equals(value) && value != null && value.trim().length() > 0){
				if(links.contains(link)){
					continue;
				}
				
				links.add(link);
				JsonObject linkJson = new JsonObject();
				linkJson.put(HttpUtil.URL, link);
//				 newBody.put(HttpUtil.CHARSET, charset);
//				linkJson.put(HttpUtil.HTML_TITLE, getTitle(doc));
				linkJson.put(HttpUtil.HTML_KEYWORDS, getKeywords(doc));
//				linkJson.put(HttpUtil.HTML_DESCRIPTION, getDescription(doc));
				results.add(linkJson);
			}
		}
		return results;
	}
	
	
	/**
	 * Verify the legitimacy of the connection 
	 * @param link
	 * @param value
	 * @return
	 */
	public boolean validatUrl(String link, String value){
		return link.startsWith("http") && !"#".equals(value) && value != null && value.trim().length() > 0;
		
	}
	
	/**
	 * Remove duplicate keywords 
	 * Use a comma to separate
	 * @param key
	 * @return
	 */
	public static  String keywordDeWeight(String key){
		String[] keys = key.toLowerCase().split(BaseParser.KEYWORD_SEPARATOR);
		StringBuffer sb = new StringBuffer();
		
		int size = keys.length;
		int end = size - 1;
		
		for (int i = 0; i < size; i++) {
			String one = keys[i].trim();
			if(sb.indexOf(one) > -1 || one.length() == 0 ){
				continue;
			}
			
			String zh_cn = dic.getProperty(one);
			if(zh_cn != null && sb.indexOf(zh_cn) == -1){
				sb.insert(0, zh_cn+",");
				System.out.println(zh_cn);
			}
			
			if(i == end){
				sb.append(one);
			}else{
				sb.append(one).append(BaseParser.KEYWORD_SEPARATOR);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static String chinaKeyword(String key){
		String[] keys = key.toLowerCase().split(BaseParser.KEYWORD_SEPARATOR);
		StringBuffer sb = new StringBuffer();
		
		int size = keys.length;
		int end = size - 1;
		
		for (int i = 0; i < size; i++) {
			String one = keys[i].trim();
			if(sb.indexOf(one) > -1 || one.length() == 0 ){
				continue;
			}
			
			String zh_cn = dic.getProperty(one);
			if(zh_cn != null && sb.indexOf(zh_cn) == -1){
				sb.insert(0, zh_cn+",");
			}
		}
		return sb.toString();
	}
	
	public static String enKeywords(String key){
		String[] keys = key.toLowerCase().split(BaseParser.KEYWORD_SEPARATOR);
		StringBuffer sb = new StringBuffer();
		
		int size = keys.length;
		for (int i = 0; i < size; i++) {
			String one = keys[i].trim();
			if(sb.indexOf(one) > -1 || one.length() == 0 || !CharUtil.isEnglish(one) ){
				continue;
			}
			sb.insert(0, one+",");
		}
		
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return null;
		
	}
	
	
	
	public static String zhKeyowrds(String key){
		String[] keys = key.toLowerCase().split(BaseParser.KEYWORD_SEPARATOR);
		StringBuffer sb = new StringBuffer();
		
		int size = keys.length;
		for (int i = 0; i < size; i++) {
			String one = keys[i].trim();
			if(sb.indexOf(one) > -1 || one.length() == 0  || !CharUtil.isChinese(one.charAt(0))){
				continue;
			}
			sb.insert(0, one+",");
		}
		
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			dic = PropertiesUtil.getProperties(SpiderHttpServer.class.getResource("/dic.properties").getPath());
			Enumeration enums = dic.propertyNames();
			while (enums.hasMoreElements()) {
				String object = (String) enums.nextElement();
				System.out.println(object);
				System.out.println(dic.getProperty(object));
				
			}
		System.out.println(dic.getProperty("bruce lee"));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);;
		} 
	}

	
}

package com.ifreeshare.spider.http.parse;

import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.log.Log;

public class BaseParser implements HtmlParser {
	
	protected static  Logger logger  = Log.register(BaseParser.class.getName());
	
	
	public static final String A = "a";
	
	public static final String META= "meta";

	@Override
	public String getTitle(Document doc) {
		String title = null;
		Element ele =  doc.getElementsByTag("title").first();
		if(ele != null) title = ele.text();
		return title;
	}

	@Override
	public String getKeywords(Document doc) {
		return doc.head().select("meta[name=keywords]").attr("content");
	}

	@Override
	public String getDescription(Document doc) {
		return doc.head().select("meta[name=description]").attr("content");
	}

	@Override
	public Elements getLinks(Document doc) {
		return doc.getElementsByTag(A);
	}

	@Override
	public Elements getImages(Document doc) {
		return null;
	}

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
	
	public boolean validatUrl(String link, String value){
		return link.startsWith("http") && !"#".equals(value) && value != null && value.trim().length() > 0;
		
	}

	
}

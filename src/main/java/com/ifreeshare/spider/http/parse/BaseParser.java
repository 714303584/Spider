package com.ifreeshare.spider.http.parse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ifreeshare.spider.http.HttpUtil;

public class BaseParser implements HtmlParser {
	
	
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
	public Set<String> getLinkValue(Document doc) {
		HashSet<String> results = new HashSet<String>();
		Iterator<Element> it = doc.select("a[href]").iterator();
		while (it.hasNext()) {
			Element ele = it.next();
			String link = ele.attr("abs:"+HttpUtil.LINK_A_HREF);
			String value = ele.attr(HttpUtil.LINK_A_HREF);
			if(link.startsWith("http") && !"#".equals(value) && value != null && value.trim().length() > 0){
				results.add(link);
			}
		}
		return results;
	}

	
}

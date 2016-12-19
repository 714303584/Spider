package com.ifreeshare.spider.http.parse;

import io.vertx.core.json.JsonObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;

/**
 * Analytic website (alphacoders.com)
 * 
 * @author zhuss
 */
public class AlphacodersComParser extends BaseParser  {

	@Override
	public Set<JsonObject> getLinkValue(Document doc) {
		HashSet<JsonObject> results = new HashSet<JsonObject>();
		Map<String, JsonObject> url_relation_info = new HashMap<String, JsonObject>();
		//Picture display div class(item)
		Elements items =  doc.getElementsByClass("item");
		Iterator<Element> it = items.iterator();
		while (it.hasNext()) {
			Element item = it.next();
			//Hyperlink to image label 
			Elements links =  item.getElementsByTag("a");
			Iterator<Element> lit = links.iterator();
			//Keyword for this image , Separator is a ",". 
			StringBuffer sb = new StringBuffer();
			while(lit.hasNext()){
				Element link = lit.next();
				String href =  link.absUrl("href");
				String value = link.attr(HttpUtil.LINK_A_HREF);
				//Image keyword acquisition 
				String text =  link.ownText();
				String title = link.attr("title");
				try {
					String domain = HttpUtil.getDomain(href);
					if(!domain.endsWith("alphacoders.com")){
						continue;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					Log.log(logger, Level.DEBUG, "href[%s]",href);
				}
		
				
				if(!validatUrl(href, value)){
					continue;
				}
				
				String keywords = text+ BaseParser.KEYWORD_SEPARATOR+ title;
				
//				if(text.contains(title)){
//				 keywords = text;	
//				}else{
//					keywords = text + BaseParser.KEYWORD_SEPARATOR + title;
//				}
				
				JsonObject newLink = new JsonObject();
				newLink.put(CoreBase.URL, href);
				newLink.put(CoreBase.HTML_KEYWORDS, BaseParser.keywordDeWeight(keywords));
			
				if(sb.indexOf(text) == -1){
					sb.append(text + BaseParser.KEYWORD_SEPARATOR);
				}
				
				if(url_relation_info.containsKey(href)){
					continue;
				}
				url_relation_info.put(href, newLink);
//				if(sb.indexOf(title) == -1){
//					sb.append(title + BaseParser.KEYWORD_SEPARATOR);
//				}
					
				
			}
			
			String dataKeyword = BaseParser.keywordDeWeight(sb.toString());
			

			//Picture storage address 
			Elements dataHrefs = item.getElementsByAttribute("data-href");
			Iterator<Element> itdata = dataHrefs.iterator();
			while (itdata.hasNext()) {
				Element dataHref = itdata.next();
				String dataHrefValue = dataHref.attr("data-href");
				JsonObject downLink = new JsonObject();
				
				downLink.put(CoreBase.URL, dataHrefValue);
				downLink.put(CoreBase.HTML_KEYWORDS, dataKeyword);
				
				if(url_relation_info.containsKey(dataHrefValue)){
					continue;
				}
				url_relation_info.put(dataHrefValue, downLink);
			}
			item.remove();
		}
		
		Set<JsonObject> superLinks = super.getLinkValue(doc);
		results.addAll(superLinks);
		results.addAll(url_relation_info.values());
		
//		Iterator<Element> it = doc.select("a[href]").iterator();
//		while (it.hasNext()) {
//			Element ele = it.next();
//			String link = ele.attr("abs:"+HttpUtil.LINK_A_HREF);
//			String value = ele.attr(HttpUtil.LINK_A_HREF);
//			if(link.startsWith("http") && !"#".equals(value) && value != null && value.trim().length() > 0){
//				try {
//					String domain = HttpUtil.getDomain(link);
//					if(domain.endsWith("alphacoders.com")){
//						results.add(link);
//					}
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		Elements dataHrefs = doc.getElementsByAttribute("data-href");
//		Iterator<Element> itdata = dataHrefs.iterator();
//		while (itdata.hasNext()) {
//			Element dataHref = itdata.next();
//			String dataHrefValue = dataHref.attr("data-href");
//			try {
//				String domain = HttpUtil.getDomain(dataHrefValue);
//				if(domain.endsWith("alphacoders.com")){
//					results.add(dataHrefValue);
//				}
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//		}
		return results;
	}

	
	
	

}

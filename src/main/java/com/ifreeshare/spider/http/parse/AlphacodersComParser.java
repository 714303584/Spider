package com.ifreeshare.spider.http.parse;

import java.util.Iterator;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class AlphacodersComParser extends BaseParser  {

	@Override
	public Set<String> getLinkValue(Document doc) {
		Set<String> result = super.getLinkValue(doc);
		Elements dataHrefs = doc.getElementsByAttribute("data-href");
		Iterator<Element> it = dataHrefs.iterator();
		while (it.hasNext()) {
			Element dataHref = it.next();
			String dataHrefValue = dataHref.attr("data-href");
			result.add(dataHrefValue);
		}
		return result;
	}

	
	
	

}

package com.ifreeshare.spider.http.parse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.nodes.Document;

import com.ifreeshare.util.RegExpValidatorUtils;

public class JB51NetParser extends BaseParser {

	@Override
	public Set<String> getLinkValue(Document doc) {
		
		Set<String> result = new HashSet<String>();
		
		Set<String> allHrefValue = super.getLinkValue(doc);
		Iterator<String> it = allHrefValue.iterator();
		while (it.hasNext()) {
			String value = it.next();
			if(value.contains("books")){
				result.add(value);
				
			}
		}
		return result;
	}
	

}

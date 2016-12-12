package com.ifreeshare.spider.http.parse;

import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.nodes.Document;

import com.ifreeshare.spider.http.HttpUtil;
import com.ifreeshare.util.RegExpValidatorUtils;

public class JB51NetParser extends BaseParser {

	@Override
	public Set<JsonObject> getLinkValue(Document doc) {
		Set<JsonObject> result = new HashSet<JsonObject>();
		Set<JsonObject> allHrefValue = super.getLinkValue(doc);
		Iterator<JsonObject> it = allHrefValue.iterator();
		while (it.hasNext()) {
			JsonObject value = it.next();
			if(value.getString(HttpUtil.URL).contains("books")){
				result.add(value);
			}
		}
		return result;
	}
	

}

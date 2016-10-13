package com.ifreeshare.spider.config;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;

/**
 * Configuration file parsing 
 * @author zhuss
 * @date 2016-10-13  18:06:12
 */
public class Configuration {
	
	public static Document config = null;
	
	/**
	 * load profile ---- xml 
	 * @param configPath  The path of the configuration file 
	 * @param demoPath Configuration file default path 
	 */
	public static void load(String configPath, String demoPath){
		try {
			File configFile = new File(configPath);
			if(configFile.exists()){
				config = Jsoup.parse(configFile, "UTF-8");
			}else{
				config = Jsoup.parse(new File(demoPath), "UTF-8");
			}
		} catch (Exception e) {
			System.out.println("spider-config.xml not found");
			System.exit(0);
		}
	}
	
	/**
	 * Gets the information about the configuration file 
	 * @param tagName XML node name 
	 * @param attrOrChildTag Attribute name of node Or Child node name 
	 * @return Attribute Value OR Child Node Text
	 */
	public static String getConfig(String tagName, String attrOrChildTag){
		//Get all nodes 
		Elements eles =  config.getElementsByTag(tagName);
		if(eles.size() > 0){
			//Get the first node 
			Element ele = eles.get(0);
			//Get attribute values 
			String value = ele.attr(attrOrChildTag);
			//not null return value
			if(value != null){
				return value;
			}
			
			//get child nodes 
			Elements childs = ele.getElementsByTag(attrOrChildTag);
			
			if(childs.size() == 0){
				return null;
			}else{
				//get first node text
				Element child = childs.get(0);
				return child.text();
			}
		}
		return null;
	}
	

}

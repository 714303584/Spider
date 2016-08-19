package com.ifreeshare.spider.entity;

import java.util.HashSet;
import java.util.Set;

public class Iterm {
	
	private  String id;
	
	private String title;
	
	private String description;
	
	private String keywords;
	
	private Set<String> urls = new HashSet<String>();
	
	private double price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "Iterm [id=" + id + ", title=" + title + ", description="
				+ description + ", keywords=" + keywords + ", urls=" + urls
				+ ", price=" + price + "]";
	}
	

}

package com.ifreeshare.spider.http.server.page;

public class PageDocument {
	
	private String uuid;
	
	private String name;
	
	private String keywords;
	
	private String description;
	
	private String thumbnail;
	
	private long size;
	
	private long favorite;
	
	private long download;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getFavorite() {
		return favorite;
	}

	public void setFavorite(long favorite) {
		this.favorite = favorite;
	}

	public long getDownload() {
		return download;
	}

	public void setDownload(long download) {
		this.download = download;
	}

}

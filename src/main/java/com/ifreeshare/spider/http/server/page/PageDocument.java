package com.ifreeshare.spider.http.server.page;

public class PageDocument {
	
	private String uuid;
	
	private String title;
	
	private String name;
	
	private String type;
	
	private String keywords;
	
	private String description;
	
	private String thumbnail = "#";
	
	private String src = "#";
	
	private String resolution;
	
	private String origin;
	
	private long size;
	
	private long favorite;
	
	private long download;
	
	

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getResolution() {
		return resolution;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	@Override
	public String toString() {
		return "PageDocument [uuid=" + uuid + ", title=" + title + ", name=" + name + ", type=" + type + ", keywords=" + keywords + ", description=" + description + ", thumbnail=" + thumbnail + ", src=" + src + ", resolution=" + resolution + ", origin=" + origin + ", size=" + size + ", favorite=" + favorite + ", download=" + download + "]";
	}

}

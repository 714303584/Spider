package com.ifreeshare.spider.http.server.page;

/**
 * Type information
 * @author zhuss
 */
public class Classification {
	
	//the id of Classification
	private String id;
	
	//the name of Classification
	//the display of Classification
	private String name;
	
	//The storage location of the index  
	//the index of elasticsearch or type of elasticsearch
	private String alias;
	
	//the description of Classification
	private String description;
	
	private String keywords;

	//The parent class of the classification
	//the index of elasticseach
	private String parent;
	
	//The keywords contained in the category
	private  String tags;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Classification [id=" + id + ", name=" + name + ", alias=" + alias + ", description=" + description + ", keywords=" + keywords + ", parent=" + parent + ", tags=" + tags + "]";
	}
}

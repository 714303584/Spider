package com.ifreeshare.spider.http.chat;

import java.util.HashMap;
import java.util.Map;

public class ChatRoom {
	
	private String name;
	
	private Map<String, Actor> actors = new HashMap<String,Actor>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Actor> getActors() {
		return actors;
	}

	public void setActors(Map<String, Actor> actors) {
		this.actors = actors;
	}
}

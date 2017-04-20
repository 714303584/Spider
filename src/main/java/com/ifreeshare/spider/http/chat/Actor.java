package com.ifreeshare.spider.http.chat;

import io.vertx.core.http.ServerWebSocket;

public class Actor {
	
	private String name;
	
	private ServerWebSocket sws;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServerWebSocket getSws() {
		return sws;
	}

	public void setSws(ServerWebSocket sws) {
		this.sws = sws;
	}
}

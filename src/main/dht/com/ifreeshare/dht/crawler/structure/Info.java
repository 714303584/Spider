package com.ifreeshare.dht.crawler.structure;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;
import java.util.List;

import com.ifreeshare.spider.core.CoreBase;

public class Info {
	
	private String name;
	private Long length;
	private Long pieceLength;
	private byte[] pieces;
	
	private List<SubFile> files;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Long getPieceLength() {
		return pieceLength;
	}

	public void setPieceLength(Long pieceLength) {
		this.pieceLength = pieceLength;
	}

	public byte[] getPieces() {
		return pieces;
	}

	public void setPieces(byte[] pieces) {
		this.pieces = pieces;
	}

	public List<SubFile> getFiles() {
		return files;
	}

	public void setFiles(List<SubFile> files) {
		this.files = files;
	}
	
	
	public JsonArray subFilesToJson(){
		JsonArray subFiles = new JsonArray();
		Iterator<SubFile> it = files.iterator();
		while (it.hasNext()) {
			SubFile subFile = it.next();
			JsonObject json = new JsonObject();
			json.put(CoreBase.FILE_PATH, subFile.getPath());
			json.put(CoreBase.FILE_SIZE, subFile.getLength());
			subFiles.add(json);
		}
		return subFiles;
	}

	
}

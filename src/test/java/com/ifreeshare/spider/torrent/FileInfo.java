package com.ifreeshare.spider.torrent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileInfo {
	
	private String path;
	
	private String path_utf8;
	
	private long  length;
	
	private byte[] filehash;
	

	public FileInfo() {
	}
	
	public FileInfo(Map<Object, Object> info) {
		init(info);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath_utf8() {
		return path_utf8;
	}

	public void setPath_utf8(String path_utf8) {
		this.path_utf8 = path_utf8;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public byte[] getFilehash() {
		return filehash;
	}

	public void setFilehash(byte[] filehash) {
		this.filehash = filehash;
	}

	@Override
	public String toString() {
		return "FileInfo [path=" + path + ", path_utf8=" + path_utf8 + ", length=" + length + ", filehash=" + Arrays.toString(filehash) + "]";
	}
		
	public void init(Map<Object, Object> info){
		
		Object path = info.get("path");
		if(path != null){
			if(path instanceof String){
				this.path = Torrent.coverString(path);
			}else if (path instanceof List){
				List<Object> paths = (List<Object>)path;
				int size = paths.size();
				int end = size - 1;
				String pathBuffer = "";
				for (int i = 0; i < size; i++) {
					Object value = paths.get(i);
					pathBuffer = pathBuffer + Torrent.coverString(value);
					if(i < end){
						pathBuffer = pathBuffer + "/";
					}
				}
				this.path = pathBuffer;
			}
		}
		
		
		Object pathUtf8 = info.get("path.utf-8");
		if(pathUtf8 != null){
			if(pathUtf8 instanceof String){
				this.path = Torrent.coverString(pathUtf8);
			}else if (pathUtf8 instanceof List){
				List<Object> paths = (List<Object>)pathUtf8;
				int size = paths.size();
				int end = size - 1;
				String pathBuffer = "";
				for (int i = 0; i < size; i++) {
					Object value = paths.get(i);
					pathBuffer = pathBuffer + Torrent.coverString(value);
					if(i < end){
						pathBuffer = pathBuffer + "/";
					}
				}
				this.path_utf8 = pathBuffer;
			}
		}
		
		Object len = info.get("length");
		if(len != null){
			this.length = (long)len;
		}
		
		Object filehash = info.get("filehash");
		if(filehash != null){
			this.filehash = (byte[])filehash;
		}
		
	}
	
	
}

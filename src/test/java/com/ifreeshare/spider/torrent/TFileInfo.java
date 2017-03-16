package com.ifreeshare.spider.torrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TFileInfo {
	
		private byte[] pieces;
		
		private String name;
		
		private String name_utf8;
		
		private String publisher;
		
		
		private String publisher_utf8;
		
		private long piece_length ; 
		
		private String publisher_url;
		
		private String publisher_url_utf8;
		
		
		private List<FileInfo> files;
		
		public List<FileInfo> getFiles() {
			return files;
		}

		public void setFiles(List<FileInfo> files) {
			this.files = files;
		}

		public byte[] getPieces() {
			return pieces;
		}

		public void setPieces(byte[] pieces) {
			this.pieces = pieces;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName_utf8() {
			return name_utf8;
		}

		public void setName_utf8(String name_utf8) {
			this.name_utf8 = name_utf8;
		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getPublisher_utf8() {
			return publisher_utf8;
		}

		public void setPublisher_utf8(String publisher_utf8) {
			this.publisher_utf8 = publisher_utf8;
		}
		
		public long getPiece_length() {
			return piece_length;
		}

		public void setPiece_length(long piece_length) {
			this.piece_length = piece_length;
		}

		public String getPublisher_url() {
			return publisher_url;
		}

		public void setPublisher_url(String publisher_url) {
			this.publisher_url = publisher_url;
		}

		public String getPublisher_url_utf8() {
			return publisher_url_utf8;
		}

		public void setPublisher_url_utf8(String publisher_url_utf8) {
			this.publisher_url_utf8 = publisher_url_utf8;
		}

		@Override
		public String toString() {
			return "TFileInfo [pieces=" + Arrays.toString(pieces) + ", name=" + name + ", name_utf8=" + name_utf8 + ", publisher=" + publisher + ", publisher_utf8=" + publisher_utf8
					+ ", piece_length=" + piece_length + ", publisher_url=" + publisher_url + ", publisher_url_utf8=" + publisher_url_utf8 + ", files=" + files + "]";
		}
		
		/**
		 * Parse the info of the Torrent file
		 * @param info
		 */
		public void init(Map<Object, Object> info){
			
			this.pieces = (byte[])info.get("pieces");
			this.piece_length = (Long)info.get("piece length");
			
			Object publisher = info.get("publisher");
			if(publisher != null){
				this.publisher = Torrent.coverString(publisher);
			}
			
			
			Object publisherUtf8 = info.get("publisher.utf-8");
			if(publisherUtf8 != null){
				this.publisher_utf8 = Torrent.coverString(publisherUtf8);
			}
			
			Object publisherUrl = info.get("publisher-url");
			if(publisherUrl != null){
				this.publisher_url = Torrent.coverString(publisherUrl);
			}
			
			Object publisherUrlUtf8 = info.get("publisher-url.utf-8");
			if(publisherUrlUtf8 != null){
				this.publisher_url_utf8 = Torrent.coverString(publisherUrlUtf8);
			}
			
			Object name = info.get("name");
			if(name != null){
				this.name = Torrent.coverString(name);
			}
			
			Object nameutf8 = info.get("name.utf-8");
			if(nameutf8 != null){
				this.name_utf8 = Torrent.coverString(nameutf8);
			}
			
			this.files = new ArrayList<FileInfo>();
			Object fs = info.get("files");
			if(fs instanceof List){
				this.files = new ArrayList<FileInfo>();
				List<Object> fileList = (List<Object>)fs;
				Iterator<Object> it = fileList.iterator();
				while (it.hasNext()) {
					Object file = it.next();
					if(file instanceof Map){
						FileInfo fileInfo = new FileInfo((Map<Object, Object>)file);
						this.files.add(fileInfo);
					}
				}
			}
			
			
		}
}

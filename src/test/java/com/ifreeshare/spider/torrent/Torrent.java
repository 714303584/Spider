package com.ifreeshare.spider.torrent;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Torrent {
	
	private String announce;
	
	private List<String> announce_list;
	
	private long creation_date;
	
	private String encoding;
	
	private String comment;
	
	private String create_by;
	
	public TFileInfo info;

	public String getAnnounce() {
		return announce;
	}

	public void setAnnounce(String announce) {
		this.announce = announce;
	}

	public List<String> getAnnounce_list() {
		return announce_list;
	}

	public void setAnnounce_list(List<String> announce_list) {
		this.announce_list = announce_list;
	}

	public long getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(long creation_date) {
		this.creation_date = creation_date;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public TFileInfo getInfo() {
		return info;
	}

	public void setInfo(TFileInfo info) {
		this.info = info;
	}
	
	@Override
	public String toString() {
		return "Torrent [announce=" + announce + ", announce_list=" + announce_list + ", creation_date=" + creation_date + ", encoding=" + encoding + ", comment=" + comment + ", create_by="
				+ create_by + ", info=" + info + "]";
	}

	public void  init(Map<Object, Object> torrentMap){
		
		Object announce = torrentMap.get("announce");
		if(announce != null ) this.announce = coverString(announce);
		
		Object announceList = torrentMap.get("announce-list");
		if(announceList != null){
			if(this.announce_list == null) this.announce_list = new ArrayList<String>();
			List<Object> list =  (List<Object>)announceList;
			Iterator<Object> it =  list.iterator();
			while (it.hasNext()) {
				Object url =  it.next();
				if(url instanceof  byte[]){
					this.announce_list.add(coverString(url));
				}else if (url instanceof List){
					List<Object> list2 = (List<Object>)url;
					for (int i = 0; i < list2.size(); i++) {
						Object childurl = list2.get(i);
						if(childurl instanceof byte[]) this.announce_list.add(coverString(childurl));
						System.out.println(coverString(childurl));
					}
					
				}
				
			}
		}
		
		Object createDate = torrentMap.get("creation date");
		if(createDate != null){
			this.creation_date = (Long)createDate;
		}
		
		Object createBy = torrentMap.get("created by");
		if(createDate != null){
			this.create_by = coverString(createBy);
		}
		
		Object encoding = torrentMap.get("encoding");
		if(encoding != null){
			this.encoding = coverString(encoding);
		}
		
		Object info = torrentMap.get("info");
		
		this.info = new TFileInfo();
		this.info.init((Map<Object, Object>)info);
		
	}
	
	
	
	public static String coverString(Object value){
		if(value instanceof String){
			return value.toString();
		}else if (value instanceof byte[]){
			byte[] stringBytes = (byte[])value;
			Charset cn = Charset.forName("UTF-8"); 
			CharBuffer cf = cn.decode(ByteBuffer.wrap(stringBytes, 0, stringBytes.length)); 
			return  new String(cf.array(), 0, cf.limit());
		}else{
			return null;
		}
	}
	

}

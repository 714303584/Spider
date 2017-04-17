package com.ifreeshare.dht.crawler.structure;

import java.util.Date;
import java.util.List;

import com.ifreeshare.dht.crawler.util.StringUtil;
/**
 * torrent file structure
 * @author xwl
 * @version 
 * Created on 2016年4月4日 下午9:08:04
 */
public class Torrent {

	private String info_hash;
	private String announce;
	private List<String> announceList;
	private Date creationDate;
	private String comment;
	private String createdBy;
	private String type;
	
	
	
	private Info info;

	public String getInfo_hash() {
		return info_hash;
	}

	public void setInfo_hash(String info_hash) {
		this.info_hash = info_hash;
	}

	public String getAnnounce() {
		return announce;
	}

	public void setAnnounce(String announce) {
		this.announce = announce;
	}

	public List<String> getAnnounceList() {
		return announceList;
	}

	public void setAnnounceList(List<String> announceList) {
		this.announceList = announceList;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getsSize() {
		return StringUtil.formatSize((double)this.getInfo().getLength());
	}


	@Override
	public String toString() {
		return "Torrent [info_hash=" + info_hash + ", announce=" + announce + ", announceList=" + announceList + ", creationDate=" + creationDate + ", comment=" + comment + ", createdBy=" + createdBy
				+ ", type=" + type + ", info=" + info.subFilesToJson().toString() + "]";
	}
	
	
}

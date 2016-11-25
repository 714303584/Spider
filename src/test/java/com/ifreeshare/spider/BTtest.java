package com.ifreeshare.spider;

import java.io.File;
import java.util.UUID;

import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.util.FileAccess;

public class BTtest {
	
	public static void main(String[] args) {
		String path = "d:/"+UUID.randomUUID();
		File file = new File(path);
		
//		file.mkdir();
//		System.out.println(System.getProperty("java.class.path"));;
//		System.out.println();
//		System.out.println(FileAccess.getFileName(file.getName()));
//		System.out.println(FileAccess.getFileType(file.getName()));
		
		
		Configuration.load("", BTtest.class.getResource("/spider-config.xml").getPath());
		
		System.err.println(Configuration.getConfig("redis", "server"));;
	}

}

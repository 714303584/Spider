package com.ifreeshare.spider;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.redis.RedisPool;

public class RedisClear {

	
	public static void main(String[] args) {
		
		
		
		
		String[] keys = {"wall.alphacoders.com","alphacoders.com","games.alphacoders.com","new.alphacoders.com","initiate.alphacoders.com","avatars.alphacoders.com","www.jb51.net",
				"gifs.alphacoders.com","pics.alphacoders.com","forum.alphacoders.com","new.alphacoders.com","art.alphacoders.com","communities.alphacoders.com"
				,CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY,CoreBase.MD5_SHA1_SHA512_EXIST_IMAGES_KEY,CoreBase.MD5_UUID_IMAGE,CoreBase.SHA1_UUID_IMAGE,CoreBase.SHA512_UUID_IMAGE
				};
		
		
		for (int i = 0; i < keys.length; i++) {
			String string = keys[i];
			RedisPool.delKey(string);
			System.out.println("Clear ------------"+string);
		}
		
		
	}
}

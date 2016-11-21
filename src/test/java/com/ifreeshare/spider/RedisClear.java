package com.ifreeshare.spider;

import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.redis.RedisPool;

public class RedisClear {

	
	public static void main(String[] args) {
		
		
		
		
		String[] keys = {"wall.alphacoders.com", "alphacoders.com", "games.alphacoders.com","new.alphacoders.com","initiate.alphacoders.com","avatars.alphacoders.com","www.jb51.net",
				"gifs.alphacoders.com","pics.alphacoders.com","forum.alphacoders.com","new.alphacoders.com","art.alphacoders.com","communities.alphacoders.com",
				"zitian.jb51.net","jjbig.jb51.net","uuid_md5_sha1_sha512_compressed_file_info_ifreeshare_com","covers.alphacoders.com","fbcovers.alphacoders.com","photos.alphacoders.com",
				"mobile.alphacoders.com",
				
				"jvniu.jb51.net", "wd.jb51.net", "360jq.jb51.net", "xiazai.jb51.net", "99idc.jb51.net", "sf.jb51.net", "xz5.jb51.net", "wt4.jb51.net", "vzidc.jb51.net",
				"enkj.jb51.net", "cywl.jb51.net", "xz.jb51.net", "big.jb51.net", "xz6.jb51.net", "jjidc.jb51.net", "dd.jb51.net",
				CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY,CoreBase.MD5_SHA1_SHA512_EXIST_IMAGES_KEY,CoreBase.MD5_UUID_IMAGE,CoreBase.SHA1_UUID_IMAGE,CoreBase.SHA512_UUID_IMAGE
				};
		
		
		for (int i = 0; i < keys.length; i++) {
			String string = keys[i];
			RedisPool.delKey(string);
			System.out.println("Clear ------------"+string);
		}
		
		
	}
}

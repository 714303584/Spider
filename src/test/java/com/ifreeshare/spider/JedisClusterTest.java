package com.ifreeshare.spider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ifreeshare.spider.core.CoreBase;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterTest {
	
	public static void main(String[] args) {
		Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
		hostAndPorts.add(new HostAndPort("127.0.0.1", 6379));
		JedisCluster jedisCluster = new JedisCluster(hostAndPorts);
		
		
		Set<String> keys  = jedisCluster.hkeys(CoreBase.UUID_MD5_SHA1_SHA512_IMAGES_KEY);
		
		Iterator<String> it = keys.iterator();
		
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		
	}

}

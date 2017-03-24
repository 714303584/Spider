package com.ifreeshare.dht.crawler.handler;

import java.net.InetSocketAddress;

public interface IInfoHashHandler {

	public void handler(InetSocketAddress address, byte[] info_hash) throws Exception;
}

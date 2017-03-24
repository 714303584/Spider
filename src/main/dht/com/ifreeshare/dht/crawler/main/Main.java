package com.ifreeshare.dht.crawler.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import redis.clients.jedis.Jedis;



//import com.jfinal.kit.Prop;
//import com.jfinal.kit.PropKit;
import com.ifreeshare.dht.crawler.listener.OnAnnouncePeerListener;
import com.ifreeshare.dht.crawler.listener.OnGetPeersListener;
import com.ifreeshare.dht.crawler.server.DHTServer;
import com.ifreeshare.dht.crawler.structure.DownloadPeer;
import com.ifreeshare.dht.crawler.structure.MyQueue;
import com.ifreeshare.dht.crawler.task.CheckExistTask;
import com.ifreeshare.dht.crawler.util.ByteUtil;
import com.ifreeshare.spider.http.parse.BaseParser;
import com.ifreeshare.util.PropertiesUtil;
//import com.so_cili.jfinal.entity.Torrent;

public class Main extends Thread {
	
	public static Main me = new Main();
	
	public static AtomicLong count = new AtomicLong(0);
	public static AtomicLong update_count = new AtomicLong(0);
	public static AtomicLong success_count = new AtomicLong(0);
//	public static MyQueue<Torrent> torrentQueue = new MyQueue<>();
	private DHTServer server = null;
	private ExecutorService metadataDwonloadThreadPool;
	private ExecutorService CheckExistThreadPool;
	private long redis_size = 0;
	
	private Jedis jedis;
	
	@Override
	public void run() {
		
		System.out.println("Main start...");
		
		try {
			Properties properties = PropertiesUtil.getProperties(BaseParser.class.getResourceAsStream("/crawler.properties"));
			
			final Long MAX_INFO_HASH = Long.parseLong(properties.getProperty("main.dhtserver.max.info_hash"));
			final Integer MAX_THREAD = Integer.parseInt(properties.getProperty("main.metadata.thread.num")) + 20000;
			
			System.out.println("MAX_INFO_HASH:" + MAX_INFO_HASH + ", MAX_THREAD:" + MAX_THREAD);

			metadataDwonloadThreadPool = Executors.newFixedThreadPool(Integer.parseInt(properties.getProperty("main.metadata.thread.num")));
			CheckExistThreadPool = Executors.newFixedThreadPool(Integer.parseInt(properties.getProperty("main.checkexist.thread.num")));

			server = new DHTServer(properties.getProperty("main.dhtserver.host"), 
					Integer.parseInt(properties.getProperty("main.dhtserver.port")),Integer.parseInt(properties.getProperty("main.dhtserver.max.node")));
			
			
			

			//配置爬取国家白名单
			/*Map<String, String> allowedIp = new HashMap<>();
			allowedIp.put("CN", "CN");
			allowedIp.put("TW", "TW");
			allowedIp.put("HK", "HK");
			allowedIp.put("JP", "JP");
			allowedIp.put("KR", "KR");
			//allowedIp.put("SG", "SG");
			
			server.setAllowedIp(allowedIp);*/
			
			//配置get_peer请求监听器
			server.setOnGetPeersListener(new OnGetPeersListener() {
				
				@Override
				public void onGetPeers(InetSocketAddress address, byte[] info_hash) {
					System.out.println("get_peers request, address:" + address.getHostString() + ", info_hash:" + ByteUtil.byteArrayToHex(info_hash));
				}
			});

			//配置announce_peers请求监听器
			server.setOnAnnouncePeerListener(new OnAnnouncePeerListener() {
				 
				@Override
				public void onAnnouncePeer(InetSocketAddress address, byte[] info_hash, int port) {
					System.out.print("info_hash:" + ByteUtil.byteArrayToHex(info_hash) + "\r");
					if (redis_size > MAX_INFO_HASH) {
						return;
					}
//					if (jedis.getSet(info_hash, new byte[]{0}) == null) {
						System.out.print(Main.count.getAndIncrement() + "\r");
						CheckExistThreadPool.execute(new CheckExistTask(new DownloadPeer(address.getHostString(), port, info_hash), metadataDwonloadThreadPool));
//						redis_size++;
//					}
				}
			});
			server.start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopAll() {
		if (server != null)
			server.stopAll();
		
		if (CheckExistThreadPool != null) {
			CheckExistThreadPool.shutdown();
			try {
				CheckExistThreadPool.awaitTermination(10, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (metadataDwonloadThreadPool != null) {
			metadataDwonloadThreadPool.shutdown();
			try {
				metadataDwonloadThreadPool.awaitTermination(10, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("webapp and crawler are all stoped.!!!");
	}
	
}

package com.ifreeshare.dht.crawler.task;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

import com.ifreeshare.dht.crawler.handler.AnnouncePeerInfoHashWireHandler;
import com.ifreeshare.dht.crawler.listener.OnMetadataListener;
import com.ifreeshare.dht.crawler.main.Main;
import com.ifreeshare.dht.crawler.structure.DownloadPeer;
import com.ifreeshare.dht.crawler.structure.Torrent;
import com.ifreeshare.dht.crawler.util.StringUtil;
import com.ifreeshare.dht.crawler.util.ZipUtil;

import redis.clients.jedis.Jedis;

public class WireMetadataDownloadTask implements Runnable {
	
	private DownloadPeer peer;
	
	public WireMetadataDownloadTask(DownloadPeer peer) {
		this.peer = peer;
	}

	@Override
	public void run() {

			AnnouncePeerInfoHashWireHandler handler = new AnnouncePeerInfoHashWireHandler();
			initHandler(handler);
			try {
				handler.handler(new InetSocketAddress(peer.getIp(), peer.getPort()), peer.getInfo_hash());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Main.count.decrementAndGet();
				handler.release();
				handler = null;
				peer = null;
			}

	}
	
	private void initHandler(AnnouncePeerInfoHashWireHandler handler) {
		handler.setOnMetadataListener(new OnMetadataListener() {
			@Override
			public void onMetadata(Torrent torrent) {
					//System.out.println("finished,dps size:" + Main.count.get());
					if (torrent == null || torrent.getInfo() == null)
						return;
					System.out.println(torrent.toString());
					//入库操作
//					Record record = new Record();
//					String jsonSubFiles = JSON.toJSONString(torrent.getInfo().getFiles());
//					System.out.println(jsonSubFiles);
//					record.set("info_hash", torrent.getInfo_hash())
//					.set("name", torrent.getInfo().getName().getBytes())
//					.set("type", torrent.getType())
//					.set("find_date", new Timestamp(new java.util.Date().getTime()))
//					.set("size", torrent.getInfo().getLength())
//					.set("hot", 1)
//					.set("subfiles", torrent.getInfo().getFiles() != null && torrent.getInfo().getFiles().size() > 0 ? ZipUtil.compress(jsonSubFiles) : null);
//					boolean success = true;
//					if (success) {
//						record.set("name", torrent.getInfo().getName());
//						record.set("flag", record.get("id") + "-" + torrent.getInfo_hash().substring(0, 1) + StringUtil.formatStr(torrent.getInfo().getName()));
//						record.set("subfiles", jsonSubFiles);
//						System.out.print("success count:" + Main.success_count.getAndIncrement() + ", " + Main.count.get() + "\r");
//					}
			}
		});
	}
	
}

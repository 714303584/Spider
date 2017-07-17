package com.ifreeshare.spider;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.ifreeshare.spider.torrent.BTClient;

/**
 * 客户端
 * 
 * @author Joeson
 */
public class UDPClient
{
	DatagramChannel channel;
	Selector selector;

	public void work()
	{

		try
		{
			// 开启一个通道
			channel = DatagramChannel.open();

			channel.configureBlocking(false);

			SocketAddress sa = new InetSocketAddress("localhost", 6881);

			channel.connect(sa);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_READ);
//			ByteBuffer bf = Charset.defaultCharset().encode("d1:ad2:id20:");
//			bf.put(BTClient.getPeerId());
//			bf.put("1:q4:ping1:t5:bbbbb1:y1:qe".getBytes());
			channel.write(Charset.defaultCharset().encode("d1:ad2:id20:abcdefghij01234567896:target20:mnopqrstuvwxyz123456e1:q9:find_node1:t2:aa1:y1:qe"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		while (true)
		{
			try
			{
				int n = selector.select();
				if (n > 0)
				{

					Iterator iterator = selector.selectedKeys().iterator();

					while (iterator.hasNext())
					{
						SelectionKey key = (SelectionKey) iterator.next();
						iterator.remove();
						if (key.isReadable())
						{
							channel = (DatagramChannel) key.channel();
							channel.read(byteBuffer);

							System.out.println(new String(byteBuffer.array()));
							
							
							byteBuffer.clear();
							
							channel.write(Charset.defaultCharset().encode(
									"data come from client"));
						}
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args)
	{
		new UDPClient().work();
	}
}
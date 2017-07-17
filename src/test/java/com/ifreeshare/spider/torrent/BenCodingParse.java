package com.ifreeshare.spider.torrent;

import io.vertx.core.json.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ifreeshare.util.RegExpValidatorUtils;

public class BenCodingParse {

	public static ByteArrayInputStream bin;

	public static void main(String[] args) {
		// String path = "d:/"+UUID.randomUUID();
		// File file = new File(path);
		//
		// // file.mkdir();
		// // System.out.println(System.getProperty("java.class.path"));;
		// // System.out.println();
		// // System.out.println(FileAccess.getFileName(file.getName()));
		// // System.out.println(FileAccess.getFileType(file.getName()));
		//
		//
		// Configuration.load("",
		// BTtest.class.getResource("/spider-config.xml").getPath());
		//
		// System.err.println(Configuration.getConfig("redis", "server"));;

//		File file = new File("F:/迅雷下载/add0024601a4d0371e15c3f8f318c6742da9ea7e.torrent");
//
//		try {
//			FileInputStream in = new FileInputStream(file);
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//			byte[] bytes = new byte[1024];
//			int i;
//
//			while ((i = in.read(bytes)) > -1) {
//				out.write(bytes, 0, i);
//			}
//			in.close();
//
//			bin = new ByteArrayInputStream(out.toByteArray());
//
//			out.flush();
//			out.close();
//
//			Object obj = parseBenCoding(bin);
//
//			if (obj instanceof Map) {
//				JsonObject json = new JsonObject((Map<String, Object>)obj);
//				System.out.println(json.encodePrettily());
//			}
//
//		} catch (FileNotFoundException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
		
		
		Map<Object, Object> result = parse("F:/迅雷下载/add0024601a4d0371e15c3f8f318c6742da9ea7e.torrent");
		
		
		
		Torrent torrent = new Torrent();
		
		torrent.init(result);
		
		
		BTClient btClient = new BTClient();
		
		byte[] results = btClient.tracker(torrent);
		
		if(results != null){
			
			ByteArrayInputStream	bin = new ByteArrayInputStream(results);
			
			Map<String, Object> map =  (Map<String, Object>) parseBenCoding(bin);
			JsonObject jsonObject = new JsonObject(map);
			
			System.out.println(jsonObject.encodePrettily());
			
		}
		
		
		System.out.println(torrent.toString());
		
		
		
		
		
		
		
		
		
		
	}
	
	public static Map<Object, Object> parse(String torrent){
		File file = new File(torrent);
		try {
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			byte[] bytes = new byte[1024];
			int i;

			while ((i = in.read(bytes)) > -1) {
				out.write(bytes, 0, i);
			}
			in.close();

			bin = new ByteArrayInputStream(out.toByteArray());

			out.flush();
			out.close();

			Object obj = parseBenCoding(bin);

			if (obj instanceof Map) {
				return (Map<Object, Object>)obj;
			}

		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}

	public static String Onebyte(int Spk, ByteArrayInputStream bin) { // 单独写个方法 更加好处理
		byte[] b = new byte[Spk]; // Spk  多少个byte
		int len = 0;
		try {
			len = bin.read(b);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}// 在临时输入流里 读多少个byte  bin输入流是 全局定义的

		return new String(b, 0, len);// byte 转 字符串
	}


	public static Object parseBenCoding(ByteArrayInputStream bin) {

		Object obj = null;
		int len = -1;
		byte[] b = new byte[1];
		try {
			while ((len = bin.read(b)) != -1) {
				String c = new String(b, 0, len);

				if (c.equals("d")) {
					Map<Object, Object> result = new HashMap<Object, Object>();
					while (!c.equals("e")) {
						Object key = parseBenCoding(bin);
						if (key.equals("e")) {
							break;
						}
						Object value = parseBenCoding(bin);
//						if(key.equals("pieces")){
//							byte[] piecesB = (byte[])value;
//							System.out.println(piecesB.length);
//						}
						result.put(key, value);
					}
					return result;
				} else if (c.equals("i")) {
					String valueI = "";
					while (true) {
						String charb = Onebyte(1, bin);
						if ("e".equals(charb)) {
							break;
						}
						valueI = valueI + charb;
					}
					
					return Long.parseLong(valueI);
				} else if (c.equals("l")) {
					List<Object> list = new ArrayList<Object>();
					while (true) {
						Object valueList = parseBenCoding(bin);
						if ("e".equals(valueList.toString())) {
							break;
						}
						list.add(valueList);
					}
					return list;

				} else if (RegExpValidatorUtils.IsIntNumber(c)) {
					String stringLen = c;
					// String
					while (true) {
						String charb = Onebyte(1, bin);

						if ("e".equals(charb)) {
							break;
						}
						if (":".equals(charb))
							break;
						stringLen = stringLen + charb;
					}
					// String valueString = Onebyte();

					int lengths = Integer.parseInt(stringLen);

					byte[] stringBytes = new byte[lengths];
					bin.read(stringBytes);

					StringBuffer sb = new StringBuffer();

					int ts = 0;
					for (int i = 0; i < stringBytes.length; i++) {
						if (stringBytes[i] < 0) { // 小于的话    等于不正常的字符，特殊的。
							ts = 1;
							break;
						}
					}
					
//					Charset cn = Charset.forName("UTF-8"); // 这是全局定义的
//					CharBuffer cf = cn.decode(ByteBuffer.wrap(stringBytes, 0, lengths)); // 这就不会出现乱码，坑爹情况了。我之前都不知道了
////					System.out.println(cf.toString());
//					return  new String(cf.array(), 0, cf.limit());
					if (ts == 0) {
						Charset cn = Charset.forName("UTF-8"); // 这是全局定义的
						CharBuffer cf = cn.decode(ByteBuffer.wrap(stringBytes, 0, lengths)); // 这就不会出现乱码，坑爹情况了。我之前都不知道了
						return  new String(cf.array(), 0, cf.limit());
					} else { // 如果特殊的 就是二进制的话直接byte数组返回
						return stringBytes;
					}
				} else if (c.equals("e")) {
					return c;
				} else {
					System.out.println("parse error!");
					return null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
}

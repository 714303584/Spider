package com.ifreeshare.spider;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpiderFileDecompression {
	
	public static void main(String[] args) {
		
//		Channel<String> channel 
		
		File file = new File("C:\\Users\\Administrator\\Downloads\\726667.jpg");
	
		try {
			long current = System.currentTimeMillis();
			System.out.println(current);
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			System.out.println(width);
			System.out.println(height);
			System.out.println(System.currentTimeMillis() -current);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}

}

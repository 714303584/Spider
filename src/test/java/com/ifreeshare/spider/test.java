package com.ifreeshare.spider;

import java.time.Instant;
import java.util.Date;



public class test {
	
	public static void main(String[] args) {
		
//		System.out.println(" 11111 ");
//		System.out.println(" 11111 ".trim());
//		
//		System.out.println("111 11".trim());
//		
//		String str = " 111 222 333 444 555 ";
//		
//		String[] strs = str.split(" ");
//		for (int i = 0; i < strs.length; i++) {
//			String string = strs[i];
//			System.out.println("+++" + string);
//		}
//	
	
		System.out.println(Instant.now().toString());
		
		
//		
//		Person p = null;
//		
//		test.changName("xiaozhu", p);
//		
//		
//		System.out.println("my old name is " + p.getName());
//		
		
		
		if(true || false){
			
		}
	}
	
	
	
	
	public static void changName(String name , Person p){
		if(p == null){
			p = new Person(name);
		}
		p.setName(name);
		System.out.println("myname is " + p.getName());
	}

}


class Person {
	public Person(String name) {
		this.name = name;
	}
	
	
	private  String name ;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}

package com.ifreeshare.spider;



public class test {
	
	public static void main(String[] args) {
		
		Person p = null;
		
		test.changName("xiaozhu", p);
		
		
		System.out.println("my old name is " + p.getName());
		
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

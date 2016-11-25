package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

public class BaseVerticle<T> extends AbstractVerticle {
	
	
	// eventBus consumer address 
	private String vAddress;
	
	//Entities need to deal with the size of the buffer
	private int bufferSize;

	//Need to deal with the entity's buffer
	private  Channel<T> buffer = null;
	
	public BaseVerticle(String vAddress, int bufferSize) {
		super();
		this.vAddress = vAddress;
		this.bufferSize = bufferSize;
		buffer = Channels.newChannel(bufferSize);
	}

	
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
	}

	@Override
	public void start() throws Exception {
		// listen
		vertx.eventBus().consumer(vAddress, msg -> {
			T t =  (T)msg.body();
//			msg.address();
			
		});
	}





	public String getvAddress() {
		return vAddress;
	}

	public void setvAddress(String vAddress) {
		this.vAddress = vAddress;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
}

package com.ifreeshare.spider.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import org.apache.logging.log4j.Logger;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import com.ifreeshare.spider.log.Log;

/**
 * Handles the parent class of a class
 * 
 * @author zhuss
 * @param <T>  ----- The type of data that needs to be manipulated
 */
public abstract class BaseVerticle<T> extends AbstractVerticle {
	
	protected  Logger logger = null;
	
	// eventBus consumer address 
	protected String vAddress;
	
	//Entities need to deal with the size of the buffer
	protected int bufferSize;

	//Need to deal with the entity's buffer
	protected  Channel<T> buffer = null;
	
	public BaseVerticle(String vAddress, int bufferSize) {
		super();
		this.vAddress = vAddress;
		this.bufferSize = bufferSize;
		logger  = Log.register(this.getClass().getName());
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
			processor(t);
		});
		
		startworker();
		
	}

	public abstract void startworker();


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
	
	/**
	 * Processing received messages
	 * @param message
	 */
	private void processor(T message) {
		try {
			buffer.send(message);
		} catch (SuspendExecution e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

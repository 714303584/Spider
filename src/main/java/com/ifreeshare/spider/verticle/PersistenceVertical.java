package com.ifreeshare.spider.verticle;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.Suspendable;

import com.ifreeshare.persistence.elasticsearch.ElasticSearchPersistence;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;

public class PersistenceVertical extends BaseVerticle<JsonObject> {
	
	ElasticSearchPersistence persistence = null;
	
	public static final String PERSISTENCE_VERTICAL_ADDRESS = "com.ifreeshare.spider.verticle.PersistenceVertical";
	
	FiberScheduler fs = null;

	public PersistenceVertical(Vertx vertx , Context context, ElasticSearchPersistence persistence) {
		super(vertx, context, PERSISTENCE_VERTICAL_ADDRESS, 100000);
		this.vertx = vertx;
		this.context = context;
		fs =  new  FiberForkJoinScheduler(PERSISTENCE_VERTICAL_ADDRESS, 10000);
		this.persistence = persistence;
	}
	

	@Suspendable
	public void startworker() {
		Fiber fiber = new Fiber(fs, () -> {
			JsonObject message = null;
			while ((message = buffer.receive()) != null) {
				try {
				 	int operateCode = (int) message.remove(CoreBase.OPERATE);
				 	
				 	switch (operateCode) {
					case CoreBase.OPERATE_I:
							if(persistence.insert(message)){
								Log.log(this.logger, Level.INFO, "persistence insert success -----------------------------    message:[%s]", message);
							}else{
								Log.log(this.logger, Level.INFO, "persistence insert failed -----------------------------    message:[%s]", message);
							}
						break;
						
					case CoreBase.OPERATE_U:
						if(persistence.update(message)){
							Log.log(this.logger, Level.INFO, "persistence update success -----------------------------    message:[%s]", message);
						}else{
							Log.log(this.logger, Level.INFO, "persistence update failed -----------------------------    message:[%s]", message);
						}
					break;

					default:
						break;
					}

						
				 	
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.log(this.logger, Level.INFO, "html pase success -----------------------------   message:[%s] e.message:[%s]",message, e.getMessage());
				}
			}
		}	);
		fiber.start();
		
	}
	
	
	
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
	}

}

package com.ifreeshare.spider.verticle;

import com.ifreeshare.persistence.IDataPersistence;
import com.ifreeshare.persistence.elasticsearch.ElasticSearchPersistence;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;

import co.paralleluniverse.fibers.Fiber;
import io.vertx.core.json.JsonObject;

public class PersistenceVertical extends BaseVerticle<JsonObject> {
	
	IDataPersistence<JsonObject> persistence = new ElasticSearchPersistence();
	
	public static final String PERSISTENCE_VERTICAL_ADDRESS = "com.ifreeshare.spider.verticle.PersistenceVertical";

	public PersistenceVertical() {
		super(PERSISTENCE_VERTICAL_ADDRESS, 100000);
	}

	@Override
	public void startworker() {
		Fiber fiber = new Fiber(() -> {
			JsonObject message = null;
			while ((message = buffer.receive()) != null) {
				try {
					persistence.insert(message);
					Log.log(this.logger, Level.INFO, "html pase success -----------------------------    message:[%s]", message);
				} catch (Exception e) {
					e.printStackTrace();
					Log.log(this.logger, Level.INFO, "html pase success -----------------------------   message:[%s] e.message:[%s]",message, e.getMessage());
				}
			}
		}	);

		fiber.start();
		
	}

}

package com.ifreeshare.spider.log;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.spider.log.Loggable.Level;

public class LogMessage {
	
	public final Logger logger;
	public final String name;
	public final Level level;
	public final String format;
	public final Object[] objects;
	
	public LogMessage(String name, Level level, String format, Object... objects) {
		this(null, name, level, format, objects);
	}
	
	public LogMessage(Logger logger, Level level, String format, Object... objects) {
		this(logger, null, level, format, objects);
	}
	
	public LogMessage(Logger logger, String name, Level level, String format, Object... objects) {
		this.logger = logger;
		this.name = name;
		this.level = level;
		this.format = format;
		this.objects = objects;
	}

}

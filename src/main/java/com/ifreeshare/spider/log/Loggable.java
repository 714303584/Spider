package com.ifreeshare.spider.log;

public interface Loggable {
	
	public enum Level {
		INFO, WARN, ERROR, FATAL, DEBUG, TRACE, PRINT, EPRINT
	}

	void log(LogMessage msg);
	
	void log(String name, Level level, Object... objects);
	
	void log(String name, Level level, String format, Object... objects);
	
}

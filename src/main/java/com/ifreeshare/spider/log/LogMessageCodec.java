package com.ifreeshare.spider.log;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;

import com.ifreeshare.util.Serializer;


public class LogMessageCodec implements MessageCodec<LogMessage, LogMessage> {
		
	@Override
	public void encodeToWire(Buffer buffer, LogMessage msg) {
		try {
			buffer.appendBytes(Serializer.serialize(msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public LogMessage decodeFromWire(int pos, Buffer buffer) {
		try {
			return (LogMessage) Serializer.deserialize(buffer.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public LogMessage transform(LogMessage msg) {
		return msg;
	}

	@Override
	public String name() {
		return "LogMessage";
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}

}


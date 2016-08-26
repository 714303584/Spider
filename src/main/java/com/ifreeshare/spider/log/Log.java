package com.ifreeshare.spider.log;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.sync.HandlerReceiverAdaptor;
import io.vertx.ext.sync.Sync;
import io.vertx.ext.sync.SyncVerticle;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.channels.Channels.OverflowPolicy;
import co.paralleluniverse.strands.channels.QueueObjectChannel;
import co.paralleluniverse.strands.queues.ArrayQueue;
import com.ifreeshare.spider.log.Loggable.Level;

public class Log {
	
	private static final List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
	private static final Map<String, Logger> loggers = new HashMap<String, Logger>();
	private static Vertx vx; 
	
	static boolean outFlag = Log.isJvmArg("true", "logger.print");
	static boolean errFlag = Log.isJvmArg("true", "logger.eprint");
	/* Please append JVM parameters -Dlogger.print=true -Dlogger.eprint=true 
	 * to enable log(Level.PRINT, ...) or log(Level.EPRINT, ...) .
	 */
	
	private static final String LOG_MESSAGE_ADDRESS = "com.svocloud.log";
	private static final String EB_LOGGING_SYNC_VERTICLE_READY = "com.svocloud.logging.ready";
	private static final String COMMON_LOGGING_NAME = "Svoc.Logging";
	private static final Logger commonLogger = LogManager.getLogger(COMMON_LOGGING_NAME);
	
	public static void setOutFlag(boolean flag) {
		outFlag = flag;
	}
	
	public static void setErrFlag(boolean flag) {
		errFlag = flag;
	}
	
	private static final int CHANNEL_QUEUE_SIZE = 4;
	private static final long CHANNEL_IDLE_PERIOD = 3000;
	private static int idleCount = 0;
	private static int useCount = 0;
	private static final QueueObjectChannel<LogMessage> channel = new QueueObjectChannel<>(
			new ArrayQueue<LogMessage>(CHANNEL_QUEUE_SIZE), OverflowPolicy.BLOCK, false, false);
	private static CountDownLatch latch;
	
	public synchronized static void initLogging() {
		if (vx != null) return;
		
		vx = Vertx.vertx();
		vx.eventBus().registerDefaultCodec(LogMessage.class, new LogMessageCodec());
		if (Vertx.currentContext() == null || !Vertx.currentContext().isEventLoopContext()) {
			latch = new CountDownLatch(1);
		}

		vx.deployVerticle(new SyncVerticle() {
			@Suspendable
			@Override
			public void start() throws Exception {
				FiberScheduler scheduler = Sync.getContextScheduler();
				EventBus eb = vx.eventBus();
				/*HandlerReceiverAdaptor<Message<LogMessage>> adaptor = Sync.streamAdaptor();
				vx.eventBus().<LogMessage> consumer(LOG_MESSAGE_ADDRESS).handler(adaptor);
				while (true) {
					Message<LogMessage> msg = adaptor.receive();
					LogMessage message = msg.body();
					__log(message.logger, message.level, message.format, message.objects);
				}*/
				
				eb.consumer(LOG_MESSAGE_ADDRESS, msg -> {
					LogMessage lmsg = (LogMessage) msg.body();
					//__log(commonLogger, Level.PRINT, "Msg = %s, Logger = (%s), Level = (%s), format = (%s)", lmsg, lmsg.logger, lmsg.level, lmsg.format);
					if (idleCount > 0 && channel.getQueueLength() < CHANNEL_QUEUE_SIZE) {
						try {
							// send message directly
							channel.send(lmsg);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return;
						
					} else if (useCount == CHANNEL_QUEUE_SIZE || channel.getQueueLength() == CHANNEL_QUEUE_SIZE) {
						// channel is full, try send message within fiber
						new Fiber<Void>(scheduler, () -> { channel.send(lmsg); }).start();
						return;
					}
					
					// none idle fiber, create fresh fiber
					Fiber fiber = new Fiber<Void>(scheduler, () -> {
						LogMessage message = lmsg;
						while (message != null) {
							// check message objects
							// if (message.objects.length == 0) continue; 
							
							// get logger and write logging message
							Logger logger = message.logger;
							if (logger == null) {
								logger = loggers.get(message.name);
								if (logger == null) {
									logger = commonLogger;
								}
							}
							
							__log(logger, message.level, message.format, message.objects);
							
							// receive next message
							idleCount++;
							message = channel.receive(CHANNEL_IDLE_PERIOD, TimeUnit.MILLISECONDS);
							idleCount--;
						}
						
						// dismiss unused fiber
						Fiber.currentFiber().cancel(true);
						useCount--;
						__log(commonLogger, Level.DEBUG, "Dismiss Logging strand [%s].",  Strand.currentStrand().getName());
						
					}).start();
					useCount++;
				});
				
			}
			
		}, new DeploymentOptions(), svr -> {
			if (latch != null) {
				// outside event-loop
				latch.countDown();
				
			} else {
				// inside event-loop
				vx.eventBus().send(EB_LOGGING_SYNC_VERTICLE_READY, true);
			}
		});
		
		if (latch == null) {
			// inside event-loop wait until sync-verticle is ready
			HandlerReceiverAdaptor<Message<Boolean>> adaptor = Sync.streamAdaptor();
			vx.eventBus().<Boolean> consumer(EB_LOGGING_SYNC_VERTICLE_READY).handler(adaptor);
			Message<Boolean> ready = adaptor.receive();
			assert(ready.body());

		} else {
			// outside event-loop
			try {
				latch.await();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*vertx.eventBus().consumer(LOG_MESSAGE_ADDRESS, msg -> {
			LogMessage lmsg = (LogMessage) msg.body();
			if (lmsg.objects.length == 0) return; 
			
			Logger logger = loggers.get(lmsg.name);
			if (logger == null) {
				logger = register(lmsg.name);
			}
			
			log(logger, lmsg.level, lmsg.format, lmsg.objects);
			
		});*/
	}
	
	public static void log(Level level, String format, Object... objects) {
		log (commonLogger, level, format, objects);
	}
	
	public static void log(Logger logger, Level level, String format, Object... objects) {
		__log(logger, level, format, objects);
	}	
	
	private static void __log(Logger logger, Level level, String format, Object... objects) {
		switch (level) {
		case INFO:
			logger.info(composeString(format, objects));
			break;
			
		case WARN:
			logger.warn(composeString(format, objects));
			break;
			
		case ERROR:
			logger.error(composeString(format, objects));
			break;
			
		case FATAL:
			logger.fatal(composeString(format, objects));
			break;
		
		case DEBUG:
			logger.debug(composeString(format, objects));
			break;
			
		case TRACE:
			logger.trace(composeString(format, objects));
			break;
			
		case PRINT:
			if (outFlag) System.out.println(now() + " " + composeString(format, objects));
			break;
			
		case EPRINT:
			if (errFlag) System.err.println("\n" + now() + " " + composeString(format, objects));
			break;
		
		default:
			/* do nothing */
		}

	}
	
	/*private static void log(String address, Logger logger, Level level) {
		vertx.eventBus().consumer(address, msg -> {
			log(logger, level, "EventBus [%s] received : %s", address, msg.body());
		});
	}*/
	
	public static String composeString(String format, Object... objs) {
		if (format == null || "".equals(format)) {
			StringBuffer buffer = new StringBuffer();
			for (Object o : objs) {
				buffer.append(o);
			}
			return buffer.toString();
		} 

		return String.format(" $%s(%d-%d)..", Strand.currentStrand().getId(), useCount, idleCount) + String.format(format, objs);
	}
	
	public static Logger register(String name) {
		if (name == null) return commonLogger;
		
		initLogging();
		return putOrRemove(name, true);
	}
	
	public static Logger unregister(String name) {
		return putOrRemove(name, false);
	}
	
	private synchronized static Logger putOrRemove(String name, boolean flag) {
		Logger logger;
		if (flag) {
			// put
			logger = LogManager.getLogger(name);
			loggers.putIfAbsent(name, logger);
			
		} else {
			// remove 
			logger = loggers.remove(name);
		
		}
		
		return logger;
	}
	
	public static void log(String name, Level level, Object... objects) {
		log(name, level, null, objects);
	}
	
	public static void log(String name, Level level, String format, Object... objects) {
		log(new LogMessage(name, level, format, objects));
	}
	
	public static void log(LogMessage msg) {
		if (msg == null) {
			__log(commonLogger, Level.WARN, "Logging message cannot be null!");
			return;
		}
		vx.eventBus().send(LOG_MESSAGE_ADDRESS, msg);
	}
	
	private static final Date dt = new Date();
	public static synchronized String now() {
		dt.setTime(System.currentTimeMillis());
		return String.format("%1$tF %1$tH:%1$tM:%1$tS.%1$tL", dt);
	}
	
	public static boolean isJvmArg(String target, String key) {
		return jvmArgs.contains("-D" + key + "=" + target);
	}

}

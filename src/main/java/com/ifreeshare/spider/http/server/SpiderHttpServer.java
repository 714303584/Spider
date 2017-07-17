package com.ifreeshare.spider.http.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;



import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.apache.logging.log4j.Logger;



import com.ifreeshare.framework.HttpServerShell;
import com.ifreeshare.spider.Runner;
import com.ifreeshare.spider.config.Configuration;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.http.chat.Actor;
import com.ifreeshare.spider.http.chat.ChatRoom;
import com.ifreeshare.spider.http.server.route.BaseRoute;
import com.ifreeshare.spider.http.server.route.image.GetImageRouter;
import com.ifreeshare.spider.http.server.route.image.LoveImageRouter;
import com.ifreeshare.spider.http.server.route.image.SearchImageRouter;
import com.ifreeshare.spider.http.server.route.image.SearchImageTagRouter;
import com.ifreeshare.spider.http.server.route.image.SearchResourceRouter;
import com.ifreeshare.spider.http.server.route.image.ShowImageRouter;
import com.ifreeshare.spider.http.server.route.image.ShowResouceRouter;
import com.ifreeshare.spider.http.server.route.image.UpdateImageRouter;
import com.ifreeshare.spider.http.server.route.users.UserDetailsPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserLoginPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserLoginPostRouter;
import com.ifreeshare.spider.http.server.route.users.UserRegistPageRouter;
import com.ifreeshare.spider.http.server.route.users.UserRegistPostRouter;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;import com.ifreeshare.util.FileAccess;


/**
 * User services 
 * @author zhuss
 */
public class SpiderHttpServer extends AbstractVerticle {
	private static  Logger logger  = Log.register(SpiderHttpServer.class.getName());
	public static Vertx vertx = null;
	
	public static Map<String, ChatRoom> rooms = new HashMap<String, ChatRoom>();
	
	Set<BaseRoute> routers = new HashSet<BaseRoute>();
	
	public static String upload_path = "G:\\nginx-1.9.4\\html\\file";
	
	
	public SpiderHttpServer(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.context = context;
	}

	@Override
	public void start() throws Exception {

		Vertx vertx = Vertx.vertx();
		CoreBase.vertx = vertx;
		HttpServer httpServer = vertx.createHttpServer();
		FreeMarkerTemplateEngine freeMarkerTemplateEngine = FreeMarkerTemplateEngine.create();
		// init Route 
		routers.add(new GetImageRouter(freeMarkerTemplateEngine));
		routers.add(new UpdateImageRouter(freeMarkerTemplateEngine));
		routers.add(new UserRegistPageRouter());
		routers.add(new SearchImageRouter());
		routers.add(new UserRegistPostRouter());
		routers.add(new UserLoginPostRouter());
		routers.add(new UserLoginPageRouter());
		routers.add(new LoveImageRouter());
		routers.add(new ShowResouceRouter());
		routers.add(new ShowImageRouter());
		routers.add(new UserDetailsPageRouter());
		routers.add(new SearchResourceRouter());
		routers.add(new SearchImageTagRouter());
		
		Iterator<BaseRoute> rit = routers.iterator();
		Router router = Router.router(vertx);
		
		//cookies, sessions and request bodies
		router.route().handler(BodyHandler.create().setUploadsDirectory(upload_path));
		router.route().handler(CookieHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		
		/**
		 * login
		 */
		AuthProvider authProvider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, new JsonObject());
		
//		authProvider.authenticate(new JsonObject(), result -> {
//			User user = result.result();
//		});
//		
		
	    router.route().handler(UserSessionHandler.create(authProvider));
		router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/login/get/html/"));
		router.route("/login/").handler(FormLoginHandler.create(authProvider).setDirectLoggedInOKURL("/user/details/get/html/"));
		
	    router.route().handler(context -> {
	    	HttpServerRequest request = context.request();
	    	SocketAddress clientAddress = request.remoteAddress();
	    	
	    	
	    	System.out.println();
	    	String ipAddress = clientAddress.host();
	    	String path = request.path();
	    	Log.log(logger, Level.INFO, "client host[%s], request path[%s]", ipAddress, path);
	    	
	    	Iterator<Entry<String, String>>  it = request.headers().iterator();
	     	while (it.hasNext()) {
	     		Entry<String, String> entry = it.next();
	     		Log.log(logger, Level.DEBUG, "header info -------------------- key[%s], value[%s] ", entry.getKey(), entry.getValue());
			}
	    	context.next();
	    });
	    
	    router.route().handler(CookieHandler.create());
//	    SessionStore store = ClusteredSessionStore.create(vertx);
//	    SessionHandler sessionHandler = SessionHandler.create(store);
//	    router.route().handler(sessionHandler);
	    
	    
	    
//	    router.post("/form").handler(ctx -> {
//	        ctx.response().putHeader("Content-Type", "text/plain");
//
//	        ctx.response().setChunked(true);
//	        
//	        
//	        for (FileUpload f : ctx.fileUploads()) {
//	      	  String fileName = f.fileName();
//	      	  System.out.println("FileName:"+f.fileName());
//	      	  System.out.println("uploadedFileName:"+f.uploadedFileName());
//	      	  System.out.println("FileName:"+f.size());
//	      	  
//	      	  File file = new File(f.uploadedFileName());
//	      	  String fileType = FileAccess.getFileType(f.fileName());
//	      	  
//	      	  file.renameTo(new File(f.uploadedFileName()+"."+fileType));
//	      	  
//	        }
//
//	        ctx.response().end();
//	      });
	    
	    
	    router.route("/logout").handler(context -> {
	        context.clearUser();
	        // Redirect back to the index page
	        context.response().putHeader("location", "/").setStatusCode(302).end();
	      });
		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
		
		
		while (rit.hasNext()) {
			BaseRoute r = rit.next();
			Log.log(logger, Level.WARN, "router[%s]", r.toString());
			switch (r.getMethod()) {
			case BaseRoute.GET:
				router.get(r.getUrl()).handler(context -> {
					r.process(context);
				});
				break;
			case BaseRoute.POST:
				router.post(r.getUrl()).handler(context -> {
					r.process(context);
				});
				break;
			case BaseRoute.PATCH:
				router.patch(r.getUrl()).handler(context -> {
					r.process(context);
				});
				break;
			default:
				break;
			}
		}
		
		HttpServerShell hss = new HttpServerShell(httpServer, router , "com.ifreeshare.spider.http.server.pubcon");
		hss.setFreeMarkerTemplateEngine(FreeMarkerTemplateEngine.create());
		hss.initRouter();
		
		
		
		httpServer.websocketHandler(sws -> {
			String path = sws.path();
			Pattern pattern =Pattern.compile("/websocket/(.*?)/(.*?)/");
			Matcher m = pattern.matcher(path);
			String room = null;
			String name = null;
			ChatRoom chatRoom = null;
			Actor actor = null;
			
			if(m.matches()){
				String[] strs = path.split("/");
				room = strs[2];
				name = strs[3];
				
				if(room != null){
					 	chatRoom = rooms.get(room);
						if(chatRoom != null){
							actor = chatRoom.getActors().get(name);
							if (actor == null) {
								actor = new Actor();
								actor.setName(name);
								actor.setSws(sws);
								chatRoom.getActors().put(name, actor);
								Map<String, Actor> roomActors = chatRoom.getActors();
								
								Iterator<Actor> it = roomActors.values().iterator();
								 while (it.hasNext()) {
									 Actor roomActor = it.next();
									 JsonObject message = new JsonObject();
									 message.put("type","sys_new_actor");
									 message.put("data", new JsonObject().put("name", name));
									 message.put("time", new Date().toString());
									 roomActor.getSws().writeFinalTextFrame(message.toString());
								}
								
							}else{
								JsonObject json = new JsonObject();
								json.put("type", 0);
								json.put("message", "用户名称已经存在，请改变用户名称！");
								sws.writeFinalTextFrame(json.toString());
								sws.close();
							}
						}else{
							chatRoom = new ChatRoom();
							rooms.put(room, chatRoom);
							actor = new Actor();
							actor.setName(name);
							actor.setSws(sws);
							chatRoom.setOwner(actor);
							chatRoom.getActors().put(name, actor);
						}
				}else{
					sws.close();
				}
				
			}else{
				sws.close();
				return;
			}
			
			
			sws.closeHandler(handler -> {
				System.out.println("" + sws.toString());
			});
			
			sws.handler(data -> {
					JsonObject recevied = new JsonObject(data.toString());
					JsonObject message = new JsonObject();
					message.put("name", "广播");
					message.put("message", recevied.getString("message"));
					message.put("sender", recevied.getString("sender"));
					message.put("time", new Date().toString());
					
					String roomName = recevied.getString("room");
					ChatRoom cr =  rooms.get(roomName);
					Iterator<Actor> ait =  cr.getActors().values().iterator();
					while (ait.hasNext()) {
						Actor a =  ait.next();
						 a.getSws().writeFinalTextFrame(message.toString());
					}
					System.out.println("message:"+message.toString());
			});
		});
		
		
		
		
		
		String listen_port = Configuration.getConfig(CoreBase.HTTP_SERVER, CoreBase.LISTEN_PORT);
		httpServer.requestHandler(router::accept).listen(Integer.parseInt(listen_port));
	}

	@Override
	public void init(Vertx vertx, Context context) {

	}

	public static void main(String[] args) {
		Configuration.load(Runner.defaultConfigPah,SpiderHttpServer.class.getResource("/spider-config.xml").getPath());
		Vertx vertx = Vertx.vertx();
		SpiderHttpServer.vertx = vertx;
		Context context = vertx.getOrCreateContext();
		
//		ElasticSearchPersistence persistence = new ElasticSearchPersistence();

//		vertx.deployVerticle(new SpiderHeaderVerticle(vertx, context));
//		vertx.deployVerticle(new SpiderHtmlVerticle(vertx, context));
//		vertx.deployVerticle(new SpiderImageVerticle(vertx, context));
//		vertx.deployVerticle(new SpiderFileVerticle(vertx, context));
//		vertx.deployVerticle(new PersistenceVertical(vertx, context, persistence));

		
		vertx.deployVerticle(new SpiderHttpServer(vertx, context));

	}

}

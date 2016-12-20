package com.ifreeshare.spider.http.server.route;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;

import java.net.HttpURLConnection;

import org.apache.logging.log4j.Logger;

import com.ifreeshare.spider.Runner;
import com.ifreeshare.spider.core.CoreBase;
import com.ifreeshare.spider.core.ErrorBase;
import com.ifreeshare.spider.log.Log;
import com.ifreeshare.spider.log.Loggable.Level;

/**
 * The parent of the routing address
 * @author zhuss
 */
public class BaseRoute {
	
	//Http request method
	public final static String GET = "GET";
	public final static String HEAD = "HEAD";
	public final static String PUT = "PUT";
	public final static String DELETE = "DELETE";
	public final static String POST = "POST";
	public final static String OPTIONS = "OPTIONS";
	public final static String PATCH = "PATCH";
	
	//Log output
	protected static  Logger logger  = Log.register(BaseRoute.class.getName());
	
	public static  Vertx vertx = Runner.vertx;
	private  String url;
	private String method;
	private String template;
	
	//Template Engine (freemarker)
	FreeMarkerTemplateEngine freeMarkerTemplateEngine = FreeMarkerTemplateEngine.create();
	
	public BaseRoute(String url, String method, FreeMarkerTemplateEngine freeMarkerTemplateEngine) {
		this.url = url;
		this.method = method;
		this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
	}

	public BaseRoute(String url, String method, String template, FreeMarkerTemplateEngine freeMarkerTemplateEngine) {
		super();
		this.url = url;
		this.method = method;
		this.template = template;
		this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
	}

	public BaseRoute(String url, String method, String template) {
		super();
		this.url = url;
		this.method = method;
		this.template = template;
	}

	public void process(RoutingContext context){
		
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public FreeMarkerTemplateEngine getFreeMarkerTemplateEngine() {
		return freeMarkerTemplateEngine;
	}

	public void setFreeMarkerTemplateEngine(FreeMarkerTemplateEngine freeMarkerTemplateEngine) {
		this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BaseRoute)){
			return false;
		}
		BaseRoute target = (BaseRoute)obj;
		return url.equals(target.getUrl()) && method.equals(target.getMethod());
	}

	@Override
	public String toString() {
		return "Route [url=" + url + ", method=" + method + "]";
	}

	public void resposeString(RoutingContext context,String end){
		context.response().end(end);
	}
	
	/**
	 * The rendering of the response
	 * @param context  ------ The context of a request
	 */
	public void render(RoutingContext context){
		freeMarkerTemplateEngine.render(context, template, res -> {
			if (res.succeeded()) {
				// context.response.putHeader("content-type",
				// "application/json;charset=UTF-8");
				context.response().putHeader("content-type", "text/html");
				context.response().end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
		
	}
	
	/**
	 * Feedback on error requests 
	 * @param response 
	 * @param errMessage    Prompt information for feedback 
	 */
	public void faultRequest(HttpServerResponse response,String errMessage){
		response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
		response.end(errMessage);
	}
	
	/**
	 * Feedback on error requests 
	 * @param response
	 * @param errorCode The Code of Error.
	 */
	public void faultRequest(HttpServerResponse response,int errorCode){
		response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
		String errorMessage = ErrorBase.getErrorMesage(errorCode);
		response.end(errorMessage);
	}
	
	/**
	 *  Check format of request parameters  
	 *  Method: GET
	 *  Submit parameters using URL splicing 
	 * @param itype CoreBase.DATA_TYPE_GET
	 * @return
	 */
	public boolean getItypeCheck(String itype){
		return CoreBase.DATA_TYPE_GET.equals(itype);
	}
	
	/**
	 *	Check Output data format 
	 *	The format of the data returned by the request 
	 * @param otype: html , xml , json;
	 * @return
	 */
	public boolean otypeCheck(String otype){
		return CoreBase.DATA_TYPE_HTML.equals(otype) || CoreBase.DATA_TYPE_XML.equals(otype) || CoreBase.DATA_TYPE_JSON.equals(otype);
	}
	
	/**
	 * Input format for POST requests 
	 * @param itype  form -> form submission,   xml and json ----> parameters in the request body 
	 * @return
	 */
	public boolean postItypeCheck(String itype){
		return CoreBase.DATA_TYPE_FORM.equals(itype) || CoreBase.DATA_TYPE_XML.equals(itype) || CoreBase.DATA_TYPE_JSON.equals(itype);
	}
	
	
	/**
	 * Router log 
	 */
	protected void  log(String message) {
		Log.log(logger, Level.WARN, "router path: [%s] router method: [%s] message: [%s]", this.url, this.method, message);
	}
	
	/**
	 * Final response function 
	 * @param context   Request context 
	 * @param response  Case of response 
	 * @param otype Response data format 
	 */
	public void response(RoutingContext context, HttpServerResponse response,String otype,Object... obj){
		
		if(CoreBase.DATA_TYPE_JSON.equals(otype)){
			response.end(obj[0].toString());
		}else if(CoreBase.DATA_TYPE_XML.equals(otype)){
			
			
		}else if(CoreBase.DATA_TYPE_HTML.equals(otype)){
			render(context);
		}
	}
	
	
}

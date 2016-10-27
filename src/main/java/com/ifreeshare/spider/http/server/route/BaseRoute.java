package com.ifreeshare.spider.http.server.route;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
/**
 * 
 * @author zhuss
 */
public class BaseRoute {
	
	public final static String GET = "GET";
	public final static String HEAD = "HEAD";
	public final static String PUT = "PUT";
	public final static String DELETE = "DELETE";
	public final static String POST = "POST";
	public final static String OPTIONS = "OPTIONS";
	public final static String PATCH = "PATCH";
	
	private  String url;
	private String method;
	private String template;
	FreeMarkerTemplateEngine freeMarkerTemplateEngine;
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
	
	public void render(RoutingContext context){
		freeMarkerTemplateEngine.render(context, template, res -> {
			if (res.succeeded()) {
				// context.response.putHeader("content-type",
				// "application/json;charset=UTF-8");
				context.response().end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
		
	}
}

package com.ifreeshare.spider;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Upload extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
	  Vertx.vertx().deployVerticle(new Upload());
	  
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    // Enable multipart form data parsing
    router.route().handler(BodyHandler.create().setUploadsDirectory("uploads"));

    router.route("/").handler(routingContext -> {
      routingContext.response().putHeader("content-type", "text/html").end(
        "<form action=\"/form\" method=\"post\" enctype=\"multipart/form-data\">\n" +
          "    <div>\n" +
          "        <label for=\"name\">Select a file:</label>\n" +
          "        <input type=\"file\" name=\"file1\" />\n" +
          "    </div>\n" +
          "    <div class=\"button\">\n" +
          "        <button type=\"submit\">Send</button>\n" +
          "    </div>" +
          "</form>"
      );
    });

    // handle the form
    router.post("/form").handler(ctx -> {
      ctx.response().putHeader("Content-Type", "text/plain");

      ctx.response().setChunked(true);
      
      
      for (FileUpload f : ctx.fileUploads()) {
    	  String fileName = f.fileName();
    	  System.out.println("FileName:"+f.fileName());
    	  System.out.println("uploadedFileName:"+f.uploadedFileName());
    	  System.out.println("FileName:"+f.size());
    	  
    	  
    	  
        ctx.response().write("Filename: " + f.fileName());
        ctx.response().write("\n");
        ctx.response().write("Size: " + f.size());
      }

      ctx.response().end();
    });

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
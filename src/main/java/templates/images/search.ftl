<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/static/js/bootstrap/dist/css/bootstrap.css">
    
    <link rel="stylesheet" href="/static/css/falls/style.css">
    <link rel="stylesheet" href="/static/css/lightbox/lightbox.css">
    
       <!-- jQuery first, then Bootstrap JS. -->
    <script src="/static/js/jquery/jquery.min.js"></script>
   	<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>
   	
   	
   	      <style type="text/css">
			.item {  
			  width: 220px;  
			  margin: 10px;  
			  float: left;  
			}  
       </style>
   	
   
  </head>
  <body style="background: url(/static/css/falls/images/body_bg.jpg);">

    <nav class="navbar navbar-dark navbar-fixed-top bg-inverse">
      <button class="navbar-toggler visible-xs" aria-expanded="false" aria-controls="navbar" type="button" data-toggle="collapse" data-target="#navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">ifreeshare</a>
      <nav class="nav navbar-nav pull-left">
        <a class="nav-item nav-link" href="#">首页</a>
        <a class="nav-item nav-link" href="#">分类</a>
        <a class="nav-item nav-link" href="#">论坛</a>
        <a class="nav-item nav-link" href="#">关于</a>
      </nav>
    </nav>
    
    <div class="jumbotron">
	  <div class="container">
			  <div class="row">
			  		<h1 class="display-3" style="text-align: center;">ifreeshare</h1>
					  <p class="lead"  style="text-align: center;">This is a simple hero unit, a simple jumbotron-style component for calling extra attention to featured content or information.</p>
					  <hr class="m-y-md">
					  <p class="lead">
					    <form class="form-inline" method="get" action="/search/image/get/html/" style="text-align: center;">
					    <input type="text" name="size"  style="width: 500px;display:none" value="20" class="form-control" id="exampleInputEmail2" required="required"/>
						  <div class="form-group">
						    <label for="exampleInputEmail2"></label>
						    <input type="text" name="keys" style="width: 500px" value="${context.keys}" class="form-control" id="exampleInputEmail2" required="required"/>
						  </div>
						  <button type="submit" class="btn btn-primary">搜索一下</button>
						</form>
					  </p>
			  </div>
	  
	  </div>
	</div>
    
	 	<div id="mainScreen" class="container"> 
						 
						 <div id="main">
							<ul id="waterfall" class="masonry" style="position: relative;">
									<#list context.pages as item>
										 <li class="masonry-brick" style="position: absolute; top: 0px; left: 0px;">
								        	<div class="img_block">
								        	 <a href="/show/image/get/html/?id=${ item.uuid }" >
								            	<img src="http://192.168.3.148:808/thumbnail${item.thumbnail}" alt="">
								             </a>
								            	
								            	 <a href="http://192.168.3.148:808/${item.src}"  rel="lightbox[plants]" class="zoom" style="display: none;">放大</a>
								               
								            	
								            	 <a href="#" class="ilike" onclick="loveImage('${item.uuid}')" style="display: none;">我喜欢</a>
								          </div>
								            <h3></h3>
								            <div class="iNum"><span>1</span><a href="http://www.niurenzm.com/demo/969/#">4</a></div>
								          <p>${item.keywords}</p>
										</li>
									</#list>
							</ul>
						</div>
						 
		</div>
						 	<div style="visibility: hidden" id="loading" class="loading">
									<p>
										<img src="/static/images/loading.gif"><img
											src="/static/images/load.gif">
									</p>
								</div>
						  

		
			<div class="container" style="text-align:center">
				  <ul class="pagination pagination-lg">
				    <li class="page-item">
				      <a class="page-link" href="/search/image/get/html/?keys=${context.keys}&index=${context.previous}&size=${context.sizep}" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				    <li class="page-item">
				      <a class="page-link" href="/search/image/get/html/?keys=${context.keys}&index=${context.nextp }&size=${context.sizep}" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				        <span class="sr-only">Next</span>
				      </a>
				    </li>
				  </ul>
			</div>
    
    <div class="jumbotron" style="margin-bottom: 0px">
	  
	</div>
	
		<div id="lightboxOverlay" style="display: none;"></div>
	<div id="lightbox" style="display: none;">
		<div class="lb-outerContainer">
			<div class="lb-container">
				<img class="lb-image">
				<div class="lb-nav">
					<a class="lb-prev"></a><a class="lb-next"></a>
				</div>
				<div class="lb-loader">
					<a class="lb-cancel"><img
						src="/static/images/loading(1).gif"></a>
				</div>
			</div>
		</div>
		<div class="lb-dataContainer">
			<div class="lb-data">
				<div class="lb-details">
					<span class="lb-caption"></span><span class="lb-number"></span>
				</div>
				<div class="lb-closeContainer">
					<a class="lb-close"><img
						src="/static/images/close.png"></a>
				</div>
			</div>
		</div>
	</div>
	
	
</body>
  <!-- 
  	<script type="/static/js/imagesloaded/imagesloaded.pkgd.js"></script>
   -->
 <script src="/static/js/imagesloaded/imagesloaded.pkgd.min.js"></script>
 
  <script src="/static/js/lightbox/lightbox.js"></script>
 
  <!--
  	 <script type="/static/js/masonry/masonry.pkgd.js"></script>
   -->
<script src="/static/js/masonry/masonry.pkgd.min.js"></script>
   
   
   
   <script type="text/javascript">
   $(document).ready(function() {
   			 var $maincontainer = $("#main");
				$maincontainer.imagesLoaded( function() {
					$maincontainer.masonry({
					 columnWidth : 230 ,
					itemSelector: '.masonry-brick'
					});
				});
   });
   
   
   function loveImage(uuid){
		   	$.ajax({
				type: "GET",
				url: "/love/image/get/get/"+uuid+"/",
				success:function(data){
					alert("thank you!")
					console.log("success:"+data);
				},
				error: function(data){
					console.log("error:"+data);
				}
			});
   }
   
			 
   </script>
   
   
   <script type="text/javascript">
$(document).ready(function(){	
	$(".zoom,.ilike").hide();

	//$(".zoom").each(function(){//遍历所有对象
	//var src=$(this).siblings("img").attr("src");
		//$(this).attr({href:src});
	//});
	
	$("#nav li").click(function(){
		$("#nav a").removeClass("hover");
		$(this).find("a").addClass("hover");
	});
	
	$("#waterfall li").mouseover(function(){
		$(this).addClass("hover");
		$(this).find(".zoom,.ilike").show();
	});
	
	$("#waterfall li").mouseout(function(){
		$(this).removeClass("hover");
		$(this).find(".zoom,.ilike").hide();
	});
});
</script>
   
   
  

</html>
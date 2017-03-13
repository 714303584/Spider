<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <!-- Bootstrap CSS -->
    
    <!-- include linker.ftl -->
<title>${context.tags.title} -- 爱享网 -- 免费的分享平台</title>
<meta name="keywords" content="${context.tags.keywords}">
<meta name="description" content="${context.tags.keywords}">
	<link rel="stylesheet"
		href="/static/js/bootstrap/dist/css/bootstrap.css">
	<link href="/static/ifreeshare.ico" rel="shortcut icon">
    
    
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
  <body>
  
	<!-- include  top -->
   <#include "../../toper.ftl">
	 	<div id="mainScreen" class="container"> 
						 
						 <div id="main">
							<ul id="waterfall" class="masonry" style="position: relative;">
									<#list context.pages.elements as item>
										 <li class="masonry-brick" style="position: absolute; top: 0px; left: 0px;">
								        	<div class="img_block">
								        	 <a href="/public/show/image/get/html/?id=${ item.uuid }" >
								            	<img src="http://${context.domain}/thumbnail${item.thumbnail}" alt="">
								             </a>
								            	
								            	 <a href="http://${context.domain}/${item.src}"  rel="lightbox[plants]" class="zoom" style="display: none;">放大</a>
								               
								            	
								            	 <!-- <a href="#" class="ilike" onclick="loveImage('${item.uuid}')" style="display: none;">我喜欢</a> -->
								          </div>
								            <h3></h3>
								          		 <!-- <div class="iNum"><span>1</span><a href="http://www.niurenzm.com/demo/969/#">4</a></div>  -->
								          <p style="word-break:break-all">${item.keywords}</p>
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
						  

		
			<div class="container" >
				  <div class="row justify-content-md-center">
				  	<ul class="pagination pagination-lg">
				    <li class="page-item">
				      <a class="page-link" href="/public/tags/list/${context.tags.id}/?index=${context.pages.previousPageNo}&size=${context.pages.pageSize}" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				    <li class="page-item">
				      <a class="page-link" href="/public/tags/list/${context.tags.id}/?index=${context.pages.nextPageNo}&size=${context.pages.pageSize}" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				        <span class="sr-only">Next</span>
				      </a>
				    </li>
				  </ul>
				  
				   </div>
			</div>
    
   <!-- include  footer -->
   <#include "../../footer.ftl">
	
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
  
<!--  Submit the link address to the search engine -->
</html>
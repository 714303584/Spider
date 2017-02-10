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
  <body>
      <#include "../admin/topMenu.ftl">
      
      
          <div class="jumbotron" >
			  <div class="container" style="text-align: center">
			  					<h1 class="display-4" style="text-align: center;">ifreeshare图片管理</h1>
							  <p  style="text-align: center;">本网站所有数据均来自于网络上的公开数据并致力于对其进行免费的分享。</p>
							  <hr class="m-y-md">
							  <div class="row justify-content-md-center">
		                  		 <form class="form-inline" method="get" action="/admin/search/image/get/html/" style="text-align: center;">
								  <input type="text" name="keys" style="width: 500px" class="form-control" id="exampleInputEmail2"/>
								  <button type="submit" class="btn btn-primary">搜索图片</button>
								</form>
							</div>
			  </div>
			</div>
    
	 	<div id="mainScreen" class="container"> 
	 				
	 				
	 				<table class="table">
						  <thead class="thead-default">
						    <tr>
						      <th>缩略图</th>
						      <th>分辨率</th>
						      <th>关键字</th>
						      <th>操作</th>
						    </tr>
						  </thead>
						  <tbody>
						  <#list context.pages.elements as item>
						    <tr>
						      <td> <a href="http://${context.domain}/${item.src}"  rel="lightbox[plants]" class="enlarge" >
						       <img src="http://${context.domain}/thumbnail${item.thumbnail}" height="50" width="50"></img>
						       </a>
						       </td>
						      <td>${item.resolution}</td>
						      <td>${item.keywords}</td>
						      <td><a  href="/admin/image/update/get/html/?uuid=${item.uuid}" class="btn btn-primary btn-sm">编辑</a>
						      	 <a  href="/admin/image/delete/get/html/?uuid=${item.uuid}" class="btn btn-primary btn-sm">删除</a> </td>
						    </tr>
						    </#list>
						  </tbody>
					</table>
	 	
					
						 
		</div>
						 	<div style="visibility: hidden" id="loading" class="loading">
									<p>
										<img src="/static/images/loading.gif"><img
											src="/static/images/load.gif">
									</p>
								</div>
						  

		
			<div class="container" style="text-align:center">
				<div class="row justify-content-md-center">
					 <ul class="pagination pagination-lg">
				    <li class="page-item">
				      <a class="page-link" href="/admin/search/image/get/html/?index=${context.pages.previousPageNo}&size=${context.pages.pageSize}&keys=${context.keys}" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				    <li class="page-item">
				     <a class="page-link" href="/admin/search/image/get/html/?index=${context.pages.nextPageNo}&size=${context.pages.pageSize}&keys=${context.keys}" aria-label="Next">
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
   			/**
   			 var $maincontainer = $("#main");
				$maincontainer.imagesLoaded( function() {
					$maincontainer.masonry({
					 columnWidth : 230 ,
					itemSelector: '.masonry-brick'
					});
				});
   			
   			**/
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
	//$(".zoom,.ilike").hide();

	$(".enlarge").each(function(){//遍历所有对象
		var src=$(this).siblings("img").attr("src");
		$(this).attr({href:src});
	});
	
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
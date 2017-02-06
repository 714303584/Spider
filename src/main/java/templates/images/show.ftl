<!DOCTYPE html>
<html lang="en">
<head>
<!-- Required meta tags always come first -->
<meta charset="utf-8">
<Meta http-equiv="Content-Type" Content="text/html; Charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <meta http-equiv="x-ua-compatible" content="ie=edge"> -->
<!-- Bootstrap CSS -->

<!-- include linker.ftl -->
 <meta name="description" content="${context.doc.keywords}">
<#include "../linker.ftl">
	<!-- jQuery first, then Bootstrap JS. -->

  <link rel="stylesheet" href="/static/css/falls/style.css"> 

</head>
<body >

	 	<!-- include  top -->
  	 <#include "../toper.ftl">

	<div class="container">
		<div class="card" style="background-color: white;">
			<div class="card-header">
					<#list context.doc.tags as item>
						<a href="/public/search/image/get/html/?keys=${ item }&size=20&index=0" class="btn btn-secondary btn-sm" role="button">${item}</a>
					</#list>
			</div>
			<img style="width: 100%"
				src="http://${context.domain}/${ context.doc.src }"
				data-holder-rendered="true">
			<div class="card-footer">
				<button id="btn_love" type="button" onclick="loveImage('${context.doc.uuid}')" class="btn btn-success">喜欢(111)</button>
				<button type="btn_collect" onclick="loveImage('${context.doc.uuid}')" class="btn btn-info">收藏(1)</button>
				<a class="btn btn-success download-button"
					href="http://${context.domain}/${ context.doc.src }" download> 
					<span class="glyphicon glyphicon-download-alt"></span>&nbsp;Download
					(${context.doc.resolution})
				</a>
			</div>
		</div>

	</div>



	  <!-- include  footer -->
   <#include "../footer.ftl">
</body>

<script src="/static/js/jquery/jquery.min.js"></script>
<script src="/static/js/bootstrap/dist/js/tether.min.js"></script>

<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>


   
   <script type="text/javascript">
   
   function loveImage(uuid){
		   	$.ajax({
				type: "GET",
				url: "/public/love/image/get/get/"+uuid+"/",
				success:function(data){
					$("#btn_love").text("已喜欢")
					console.log("success:"+data);
				},
				error: function(data){
					console.log("error:"+data);
				}
			});
   }
   
      function collectImage(uuid){
		   	$.ajax({
				type: "GET",
				url: "/public/collect/image/get/get/"+uuid+"/",
				success:function(data){
					$("#btn_collect").text("已收藏")
					console.log("success:"+data);
				},
				error: function(data){
					console.log("error:"+data);
				}
			});
   }
   
   
   
			 
   </script>
   
   
<!--  Submit the link address to the search engine -->
<#include "../linkpush.ftl">

</html>
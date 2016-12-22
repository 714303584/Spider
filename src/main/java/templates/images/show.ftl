<!DOCTYPE html>
<html lang="en">
<head>
<!-- Required meta tags always come first -->
<meta charset="utf-8">
<Meta http-equiv="Content-Type" Content="text/html; Charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <meta http-equiv="x-ua-compatible" content="ie=edge"> -->
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="/static/js/bootstrap/dist/css/bootstrap.css">
<!-- jQuery first, then Bootstrap JS. -->

</head>
<body style="background: url(/static/css/falls/images/body_bg.jpg);">

	<nav class="navbar navbar-dark navbar-fixed-top bg-inverse">
		<button class="navbar-toggler visible-xs" aria-expanded="false"
			aria-controls="navbar" type="button" data-toggle="collapse"
			data-target="#navbar">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="#">ifreeshare</a>
		<nav class="nav navbar-nav pull-left">
			<a class="nav-item nav-link" href="#">首页</a> <a
				class="nav-item nav-link" href="#">分类</a> <a
				class="nav-item nav-link" href="#">论坛</a> <a
				class="nav-item nav-link" href="#">关于</a>
		</nav>
	</nav>

	<div class="jumbotron" style="margin-bottom: 0px;">
		<div class="container">
			<div class="row">
				<h1 class="display-3" style="text-align: center;">ifreeshare</h1>
				<p class="lead" style="text-align: center;">This is a simple
					hero unit, a simple jumbotron-style component for calling extra
					attention to featured content or information.</p>
				<hr class="m-y-md">
				<p class="lead">
				<form class="form-inline" method="get" action="/file/search"
					style="text-align: center;">
					<div class="form-group">
						<label for="exampleInputEmail2"></label> <input type="text"
							name="keys" style="width: 500px" class="form-control"
							id="exampleInputEmail2" required="required">
					</div>
					<button type="submit" class="btn btn-primary">搜索一下</button>
				</form>
				</p>
			</div>
		</div>
	</div>

	<div class="container">
		<div class="card" style="background-color: white;">
			<div class="card-header">
					<#list context.doc.tags as item>
						<a href="/search/image/get/html/?keys=${ item }&size=20&index=0" class="btn btn-secondary btn-sm" role="button">${item}</a>
					</#list>
			</div>
			<img style="width: 100%"
				src="http://localhost:808/${ context.doc.src }"
				data-holder-rendered="true">
			<div class="card-footer">
				<button id="btn_love" type="button" onclick="loveImage('${context.doc.uuid}')" class="btn btn-success">喜欢(111)</button>
				<button type="btn_collect" onclick="loveImage('${context.doc.uuid}')" class="btn btn-info">收藏(1)</button>
				<a class="btn btn-success download-button"
					data-href="https://initiate.alphacoders.com/download/art/16236/jpg">
					<span class="glyphicon glyphicon-download-alt"></span>&nbsp;Download
					(2560x1600)
				</a>
			</div>
		</div>

	</div>



	<div class="jumbotron" style="margin-bottom: 0px"></div>
</body>

<script src="/static/js/jquery/jquery.min.js"></script>
<script src="/static/js/bootstrap/dist/js/tether.min.js"></script>

<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>


   
   <script type="text/javascript">
   
   function loveImage(uuid){
		   	$.ajax({
				type: "GET",
				url: "/love/image/get/get/"+uuid+"/",
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
				url: "/collect/image/get/get/"+uuid+"/",
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


</html>
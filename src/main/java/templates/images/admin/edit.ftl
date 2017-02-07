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
<body>

 		<#include "../../topMenu.ftl">
      
      
          <div class="jumbotron" >
			  <div class="container" style="text-align: center">
			  					<h1 class="display-4" style="text-align: center;">ifreeshare</h1>
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


	<div class="container">
		<form action="/admin/image/update/form/html/" method="post" >
		
				<div class="form-group row" style="display:none">
				<label for="example-text-input" class="col-xs-2 col-form-label">唯一标识:</label>
				<div class="col-xs-10">
					<input id="uuid" name="uuid" class="form-control" type="" value="${context.doc.uuid}"
						id="example-text-input">
				</div>
			</div>
		
		<!--
					<div class="form-group row">
						<label for="example-text-input" class="col-xs-2 col-form-label">文件名称:</label>
						<div class="col-xs-10">
							<input id="filename" name="filename" class="form-control" type="text" value="${context.doc.name}"
								id="example-text-input">
						</div>
					</div>
		 -->
			
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">关键字:</label>
					<input id="keywords" name="keywords" class="form-control" type="text"  value="${context.doc.keywords}"
						id="example-text-input">
			</div>
			
			<div class="form-group row">
		      <div class="offset-sm-2 col-sm-10">
		        <button type="submit" class="btn btn-primary">Update</button>
		      </div>
		    </div>
			
		</form>

	</div>

	<div class="jumbotron" style="margin-bottom: 0px"></div>
</body>

<script src="/static/js/jquery/jquery.min.js"></script>
<script
	src="/static/js/bootstrap/dist/js/tether.min.js"
	></script>

<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>
</html>
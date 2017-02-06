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

	<div class="jumbotron">
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
		<form action="/admin/image/resource/create/" method="post" >
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">名称:</label>
				<div class="col-xs-10">
					<input id="id_title" name="title" class="form-control" type="text"  
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">地址:</label>
				<div class="col-xs-10">
					<input id="id_path" name="path" class="form-control" type="text"   
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">关键字:</label>
				<div class="col-xs-10">
					<input id="id_keywords" name="keywords" class="form-control" type="text"   
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">缩略图:</label>
				<div class="col-xs-10">
					<input id="id_thumbnail" name="thumbnail" class="form-control" type="text"   
						id="example-text-input">
				</div>
			</div>
			
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">描述:</label>
				<div class="col-xs-10">
					<input id="id_description" name="description" class="form-control" type="text"   
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
		      <div class="offset-sm-2 col-sm-10">
		        <button type="submit" class="btn btn-primary">创建</button>
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
<!DOCTYPE html>
<html lang="en">
<head>
<!-- Required meta tags always come first -->
<meta charset="utf-8">
<Meta http-equiv="Content-Type" Content="text/html">
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
				<p class="lead" style="text-align: center;">Welcome to join Ifreeshare! Our aim is: absolutely free ! </p>
				<hr class="m-y-md"/>
			</div>
		</div>
	</div>



	<div class="container">
		<form action="/user/regist/form/html/" method="post" >
		
				<div class="form-group row" >
				<label for="example-text-input" class="col-xs-2 col-form-label">用户名称:</label>
				<div class="col-xs-10">
					<input id="txt_username" name="username" class="form-control" type="text" 
						id="example-text-input">
				</div>
			</div>
		
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">密码:</label>
				<div class="col-xs-10">
					<input id="txt_password" name="password" class="form-control" type="text" 
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
		      <div class="offset-sm-2 col-sm-10">
		        <button type="submit" class="btn btn-primary">注册</button>
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
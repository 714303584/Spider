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
	
	<link rel="stylesheet" href="/static/css/falls/style.css"> 


<!-- jQuery first, then Bootstrap JS. -->

</head>
<body>

  <#include "../topMenu.ftl">

	<div class="jumbotron">
		<div class="container">
				<h1 class="display-3" style="text-align: center;">ifreeshare</h1>
				<p class="lead" style="text-align: center;">Welcome to join Ifreeshare! Our aim is: absolutely free ! </p>
				<hr class="m-y-md"/>
		</div>
	</div>



	<div class="container">
		<form action="/login" method="post" >
		
				<div class="form-group row" >
					<label for="example-text-input" class="col-xs-2 col-form-label">用户名称:</label>
					<input id="txt_username" name="username" class="form-control" type="text" 
						id="example-text-input">
			</div>
		
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">密码:</label>
				<input id="txt_password" name="password" class="form-control" type="text" 
						id="example-text-input">
			</div>
			
			<div class="form-group row">
		      <div class="offset-sm-2 col-sm-10">
		        <button type="submit" class="btn btn-primary">登录</button>
		      </div>
		    </div>
			
		</form>

	</div>
	
		 <#include "../footer.ftl">
</body>

<script src="/static/js/jquery/jquery.min.js"></script>
<script
	src="/static/js/bootstrap/dist/js/tether.min.js"
	></script>

<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>
</html>
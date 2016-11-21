<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/static/js/bootstrap/dist/css/bootstrap.css">
       <!-- jQuery first, then Bootstrap JS. -->
   
  </head>
  <body>

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
    
		
		
			<#list context.pages as item>
							<div class="container">
						  <p class="lead">${item.name}</p>
						   <p class="lead">${item.keywords}</p>
						  <p>${item.description}</p>
						  <img style="height:500px,width:500px" src="http://localhost:808/thumbnail${item.thumbnail}"/>
						  	<a href="/image/${item.uuid}/get/html/">编辑</a>
						   <hr class="m-y-md">
			    			</div>
			    			
			</#list>
		
			<div class="container">
				  <ul class="pagination pagination-lg">
				    <li class="page-item">
				      <a class="page-link" href="/search/image/get/html/?keys=${context.keys}&index=${context.index - 1}" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				    <li class="page-item">
				      <a class="page-link" href="/search/image/get/html/?keys=${context.keys}&index=${context.index - 1}" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				        <span class="sr-only">Next</span>
				      </a>
				    </li>
				  </ul>
			</div>
		
    
    
    
    
    
    
    
    
    <div class="jumbotron" style="margin-bottom: 0px">
	  
	</div>
</body>
  
   <script src="/static/js/jquery/jquery.min.js"></script>
   <script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>
  
  
  
</html>
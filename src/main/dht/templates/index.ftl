<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <!-- Bootstrap CSS -->
    
    <!-- include linker.ftl -->
	<#include "link.ftl">
    
    
    <link rel="stylesheet" href="/static/css/falls/style.css"> 
    
       <!-- jQuery first, then Bootstrap JS. -->
    <script src="/static/js/jquery/jquery.min.js"></script>
   
  </head>
  <body>
  
	<!-- include  top -->
	<#include "top.ftl">
	
	
	<div class="jumbotron" style="padding-bottom:16px" >
	
								<h1 class="display-4" style="text-align: center;">我搜BT</h1>
							  <p  style="text-align: center;">本网站所有数据均来自于网络上的公开数据并致力于对其进行免费的分享。</p>
							  <div class="row justify-content-md-center">
		                  		 <form class="form-inline" method="get" action="/search.html" style="text-align: center;">
								  <input type="text" name="keys" style="width: 500px" class="form-control" id="exampleInputEmail2"/>
								  <button type="submit" class="btn btn-primary">搜索</button>
								</form>
							</div>
	</div>
			  <div class="container" >
						<div class="row ">
							<table class="table table-hover">
								  <thead>
								    <tr>
								      <th>名称</th>
								      <th>大小</th>
								    </tr>
								  </thead>
								  <tbody>
								  
								  	<#list context.pages.elements as item>
									 <tr>
								      <td><a href="/btinfo.html?hash=${item.info_hash}">${item.info.name}</a></td>
								      <td>${item.sSize}</td>
								    </tr>
				          		</#list>
								  </tbody>
								</table>
						
						</div>
			  </div>
	  
						 
						 
						  

		
    
   <!-- include  footer -->
   <#include "foot.ftl">
	
	
	
</body>
  
<!--  Submit the link address to the search engine -->
</html>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    
    <title>${context.classification.name} ----- 爱享网</title>
	<meta name="keywords" content="${context.classification.keywords}">
	<meta name="description" content="${context.classification.description}">
    
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/static/js/bootstrap/dist/css/bootstrap.css">
    
    <link rel="stylesheet" href="/static/css/falls/style.css">
    <link rel="stylesheet" href="/static/css/lightbox/lightbox.css">
    
       <!-- jQuery first, then Bootstrap JS. -->
    <script src="/static/js/jquery/jquery.min.js"></script>
   	<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>
   
  </head>
  <body>
       <#include "../../topMenu.ftl">
      
          <div class="jumbotron" >
			  <div class="container" style="text-align: center">
			  					<h1 class="display-4" style="text-align: center;">${context.classification.name}</h1>
							  <p  style="text-align: center;">${context.classification.name}</p>
							  <hr class="m-y-md">
							  <div class="row justify-content-md-center">
							</div>
			  </div>
			</div>
    
	 	<div class="container"> 
	 				<#list context.pages.elements as item>
	 				  		<a href="/public/tags/list/${item.id}/" class="btn btn-secondary btn-sm" role="button">${item.name}</a>
					</#list>
		</div>
			<div class="container" style="text-align:center">
				<div class="row justify-content-md-center">
					 <ul class="pagination pagination-lg">
				    <li class="page-item">
				      <a class="page-link" href="/public/classic/tags/${context.classification.id}/?index=${context.pages.previousPageNo}&size=${context.pages.pageSize}" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				    <li class="page-item">
				     <a class="page-link" href="/public/classic/tags/${context.classification.id}/?index=${context.pages.nextPageNo}&size=${context.pages.pageSize}" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				        <span class="sr-only">Next</span>
				      </a>
				    </li>
				     <li class="page-item">
				      	<a class="page-link" href="#" >
				         <span aria-hidden="true">共${context.pages.totalCount}张</span>
				        <span class="sr-only">Previous</span>
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
   
</html>
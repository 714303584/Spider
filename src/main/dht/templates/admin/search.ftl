<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <!-- Bootstrap CSS -->
    
    <!-- include linker.ftl -->
	
    
    <link rel="stylesheet" href="/static/css/falls/style.css"> 
    
       <!-- jQuery first, then Bootstrap JS. -->
    <script src="/static/js/jquery/jquery.min.js"></script>
    
    <#include "../link.ftl">
    <style type="text/css">

		.c-666 {
		    color: #666;
		    margin-bottom: 0;
		
		}
		
		.mb-20 {
			margin-bottom: 20px;
		}
		
		.f-18 {
			font-size: 18px;
		
		}
		
			.f-12 {
			font-size: 12px;
		}

</style>
   
  </head>
  <body>
  
	<!-- include  top -->
	<#include "../top.ftl">
	
	
	<div class="jumbotron" style="padding-bottom:16px;margin:0px" >
	 		 <div class="row" >
	 		 <div class="col-1"> </div>
			<div class="col-8">
			  <form class="form-inline" method="get" action="/admin/torrent/list/" style="text-align: center;">
							<input type="text" name="keys" value="${context.keys}" style="width: 500px" class="form-control" id="exampleInputEmail2"/>
							<button type="submit" class="btn btn-primary">搜索</button>
						</form>
			</div>
		               
			</div>
	</div>
	<div class="container-fluid">
		 <div class="row" >
		  <div class="col-1"> </div>
			<div class="col-10">
				<ul class="list-unstyled">
				  <li class="mb-20">
				  <h4>共查找到<span class="badge badge-pill badge-success">${context.pages.totalCount}</span>条数据。
				  </h4>
				  	<hr class="m-y-md" style="margin:0px">
				  </li>
					
					
					
						<#list context.pages.elements as item>
									<li class="mb-20">
									  		<a class="f-18" href="/btinfo.html?hash=${item.info_hash}">
									  			${item.info.name}
									  		 </a>
									  		 <div class="f-12">
										  		 <#list item.info.files as file>
										  		 	<p class="c-666">${file.path}</p>
										  		 	<#if (file_index == 2)>
										  		 		<p class="c-666">.......</p>
										  		 		<#break>
										  		 	</#if>
											  	</#list>
										  	</div>
										  	<div style="color: green;font-size: 14px;">
												文件类型：${item.type}
												<span style="margin: 0 5px;color: #CCC;font-size: 10px!important;">|</span>
												发现时间：${item.creationDate?string("yyyy-MM-dd HH:mm:ss zzzz")}
												<span style="margin: 0 5px;color: #CCC;font-size: 10px!important;">|</span>
												大小：${item.sSize}
												<span style="margin: 0 5px;color: #CCC;font-size: 10px!important;">|</span>
												文件数量：${item.info.files?size }
												<span style="margin: 0 5px;color: #CCC;font-size: 10px!important;">|</span>
												文件热度：1 ℃
											</div>
											
											
											<#if (item.status == 1) >
											 <a class="btn btn-primary" href="/admin/torrent/show/?hash=${item.info_hash}&status=0">
									  			隐藏
									  		 </a>
												
											<#else>
												<a class="btn btn-primary" href="/admin/torrent/show/?hash=${item.info_hash}&status=1">
										  			显示
										  		 </a> 
											</#if>
									  		
									  </li>
				          		</#list>
				</ul>
				<div class="container" >
				  <div class="row justify-content-md-center">
				  	<ul class="pagination pagination-lg">
				    <li class="page-item">
				      <a class="page-link" href="/admin/torrent/list/?index=${context.pages.previousPageNo}&size=${context.pages.pageSize}&keys=${context.keys}" aria-label="Previous">
				        <span aria-hidden="true">&laquo;</span>
				        <span class="sr-only">Previous</span>
				      </a>
				    </li>
				    <li class="page-item">
				      <a class="page-link" href="/admin/torrent/list/?index=${context.pages.nextPageNo}&size=${context.pages.pageSize}&keys=${context.keys}" aria-label="Next">
				        <span aria-hidden="true">&raquo;</span>
				        <span class="sr-only">Next</span>
				      </a>
				    </li>
				  </ul>
				   </div>
			</div>
				
				
			
			</div>
			<div class="col-1"> </div>
		 </div>
	</div>
    
   <!-- include  footer -->
   <#include "../foot.ftl">
	
</body>
  
<!--  Submit the link address to the search engine -->
</html>
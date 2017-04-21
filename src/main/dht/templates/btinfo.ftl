<!DOCTYPE html>
<html lang="en">
<head>
<!-- Required meta tags always come first -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<!-- Bootstrap CSS -->

<!-- include linker.ftl -->
<title>${context.torrent.info.name} -- 我搜BT</title>
<meta name="keywords" content="${context.torrent.info.name},磁力下载,迅雷下载,种子下载,资源下载">
<meta name="description" content="BT磁力链接搜索引擎是一个实现了BT协议的DHT网络爬虫BT资源搜索网站，索引了全球最新最热门的BT种子信息和磁力链接，提供磁力链接搜索、BT搜索、种子搜索等强大功能。">
<link rel="stylesheet"
	href="/static/js/bootstrap/dist/css/bootstrap.css">
<link href="/static/ifreeshare.ico" rel="shortcut icon">


<link rel="stylesheet" href="/static/css/falls/style.css">


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
		
		
		.text-r {
			text-align: right!important;
			width:100px;
		}

</style>



<script src="/static/js/webThunder/webThunderDetect.js"></script> 
<script src="/static/js/webThunder/base64.js"></script> 

</head>
<body>

	<!-- include  top -->
	<#include "top.ftl">


	<div class="jumbotron" style="padding-bottom: 16px; margin: 0px">
		<div class="row">
		
		<div class="col-1"> </div>
			<div class="col-8">
				<form class="form-inline" method="get" action="/search.html"
				style="text-align: center;">
				<input type="text" name="keys" style="width: 500px"
					class="form-control" id="exampleInputEmail2" />
				<button type="submit" class="btn btn-primary">搜索</button>
			</form>
			</div>
		
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
		</div>
	
		<div class="row">
			<div class="col-1"> </div>
			<div class="col-8">
				<h4>${context.torrent.info.name}</h4>
				<table class="table table-bordered">
					<tbody>
						<tr >
							<th class="table-info" colspan="5">文件描述</th>
						</tr>
						<tr>
							<td class="text-r">文件类型:</td>
							<td colspan="2">${context.torrent.type}</td>
						</tr>
						<tr>
							<td class="text-r">发现时间:</td>
							<td colspan="2">${context.torrent.creationDate?string("yyyy-MM-dd HH:mm:ss zzzz")}</td>
						</tr>
						<tr>
							<td class="text-r">文件大小:</td>
							<td colspan="2">${context.sSize}</td>
						</tr>
						<tr>
							<td class="text-r">文件数量:</td>
							<td colspan="2">${context.torrent.info.files?size}</td>
						</tr>
						<tr>
							<td class="text-r">文件热度:</td>
							<td colspan="2">1 ℃</td>
						</tr>
						<tr>
							<th class="table-info" colspan="5">下载地址</th>
						</tr>
						<tr>
							<td  class="text-r">磁力链接:</td>
							<td colspan="2"><a class="c-blue"
								href="magnet:?xt=urn:btih:${context.torrent.info_hash}&amp;dn=${context.torrent.info.name}">
								magnet:?xt=urn:btih:${context.torrent.info_hash}&amp;dn=${context.torrent.info.name}</a></td>
						</tr>
						<tr>
							<td class="text-r">迅雷链接:</td>
							<td colspan="2">
							<a class="c-blue" style="word-break:break-all" id="thunder-link"
								href="magnet:?xt=urn:btih:${context.torrent.info_hash}&amp;dn=${context.torrent.info.name}">
									<p>
										<script type="text/javascript">document.write(ThunderEncode("magnet:?xt=urn:btih:${context.torrent.info_hash}&amp;dn=${context.torrent.info.name}"))</script>
									</p>
								
								</a></td>
						</tr>
						<tr>
							<th class="table-info" colspan="5">文件列表</th>
						</tr>
						
								<#list context.torrent.info.files as item>
										<tr>
											<th colspan="5">
											
												<span style="float: left!important;"> ${item.path} </span>
												
												<span style="float: right!important;"> ${item.length} </span>
												
											</th>
										</tr>
				          		</#list>
						
						
					</tbody>
				</table>
			</div>
			<div class="col-2">
				<h4>	&nbsp;</h4>
				
					<table class="table table-bordered">
					<tbody>
						<tr >
							<th class="table-info" colspan="5">相关搜索</th>
						</tr>
						<!--
							<tr>
								<td colspan="2">视频</td>
							</tr>
							<tr>
								<td colspan="2">2017-01-31 21:45:27</td>
							</tr>
							<tr>
								<td colspan="2">1.30 GB</td>
							</tr>
							<tr>
								<td colspan="2">3</td>
							</tr>
							<tr>
								<td colspan="2">1 ℃</td>
							</tr>
							<tr>
								<td colspan="2">没有了</td>
							</tr>
							<tr>
								<td colspan="2">没有了</td>
							</tr>
						
						 -->
					</tbody>
				</table>
			
			</div>
			<div class="col-1">
			
			</div>
		</div>
	</div>







	<!-- include  footer -->
	<#include "foot.ftl">



</body>

<!-- jQuery first, then Bootstrap JS. -->

<script src="/static/js/jquery/jquery.min.js"></script>
<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>

<!--  Submit the link address to the search engine -->
</html>
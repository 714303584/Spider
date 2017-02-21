<!DOCTYPE html>
<html lang="en">
<head>
<!-- Required meta tags always come first -->
<meta charset="utf-8">
<Meta http-equiv="Content-Type" Content="text/html; Charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <meta http-equiv="x-ua-compatible" content="ie=edge"> -->
<!-- Bootstrap CSS -->
<!-- include linker.ftl -->
<title>爱享网 -- 免费的分享平台</title>
<meta name="keywords" content="${context.doc.keywords},图片">
<meta name="description" content="爱享网免费为您提供动物，美女，汽车，搞笑、艺术、科技,电影,电视,摄影等图片和高清壁纸。">
<link rel="stylesheet"
	href="/static/js/bootstrap/dist/css/bootstrap.css">
<link href="/static/ifreeshare.ico" rel="shortcut icon">
	<!-- jQuery first, then Bootstrap JS. -->

  <link rel="stylesheet" href="/static/css/falls/style.css"> 
<script src="/static/js/jquery/jquery.min.js"></script>
<script src="/static/js/bootstrap/dist/js/tether.min.js"></script>

<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>
<script src="/static/js/other/ads.js"></script>

<script type="text/javascript">
    (function(win,doc){
        var s = doc.createElement("script"), h = doc.getElementsByTagName("head")[0];
        if (!win.alimamatk_show) {
            s.charset = "gbk";
            s.async = true;
            s.src = "http://a.alimama.cn/tkapi.js";
            h.insertBefore(s, h.firstChild);
        };
        var o = {
            pid: "mm_46625958_21094119_71756354",/*推广单元ID，用于区分不同的推广渠道*/
            appkey: "",/*通过TOP平台申请的appkey，设置后引导成交会关联appkey*/
            unid: "",/*自定义统计字段*/
            type: "click" /* click 组件的入口标志 （使用click组件必设）*/
        };
        win.alimamatk_onload = win.alimamatk_onload || [];
        win.alimamatk_onload.push(o);
    })(window,document);
</script>





  
	<style>
		#pop{background:#fff;width:320px; height:292px;font-size:12px;position:fixed;right:0;bottom:0;}
		#popHead{line-height:32px;background:#f6f0f3;border-bottom:1px solid #e0e0e0;font-size:12px;padding:0 0 0 10px;}
		#popHead h2{font-size:14px;color:#666;line-height:32px;height:32px;}
		#popHead #popClose{position:absolute;right:10px;top:1px;}
		#popHead a#popClose:hover{color:#f00;cursor:pointer;}
	</style>
  
  

</head>
<body >
		<div id="pop" style="display:none;"> 
		      <div id="popHead"> 
		        <a id="popClose" title="关闭">关闭</a> 
		        <h2>赞助广告</h2> 
		    </div> 
		    <div id="popContent"> 
		    	<a data-type="3" data-tmpl="320x250" data-tmplid="186" data-rd="2" data-style="2" data-border="1" href="#"></a>
		    </div> 
		</div> 
	 	<!-- include  top -->
  	 <#include "../toper.ftl">

	<div class="container">
		<div class="card" style="background-color: white;">
			<div class="card-header">
					<#list context.doc.tags as item>
						<a href="/public/search/image/get/html/?keys=${ item }&size=20&index=0" class="btn btn-secondary btn-sm" role="button">${item}</a>
					</#list>
			</div>
			<img style="width: 100%"
				src="http://${context.domain}/${ context.doc.src }"
				data-holder-rendered="true">
			<div class="card-footer">
				<button id="btn_love" type="button" onclick="loveImage('${context.doc.uuid}')" class="btn btn-success">喜欢(111)</button>
				<button type="btn_collect" onclick="loveImage('${context.doc.uuid}')" class="btn btn-info">收藏(1)</button>
				<a class="btn btn-success download-button"
					href="http://${context.domain}/${ context.doc.src }" download> 
					<span class="glyphicon glyphicon-download-alt"></span>&nbsp;Download
					(${context.doc.resolution})
				</a>
			</div>
		</div>

	</div>



	  <!-- include  footer -->
   <#include "../footer.ftl">
</body>




   
   <script type="text/javascript">
   
   var popad=new Pop(); 
   function loveImage(uuid){
		   	$.ajax({
				type: "GET",
				url: "/public/love/image/get/get/"+uuid+"/",
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
				url: "/public/collect/image/get/get/"+uuid+"/",
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
   
   
<!--  Submit the link address to the search engine -->
<#include "../linkpush.ftl">

</html>
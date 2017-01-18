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
<#include "../linker.ftl">

<!-- jQuery first, then Bootstrap JS. -->

</head>
<body>

	
	<!-- include  top -->
   <#include "../toper.ftl">

	<div class="container">
		<form action="/image/${context.doc.uuid}/form/html/" method="post" >
		
				<div class="form-group row" >
				<label for="example-text-input" class="col-xs-2 col-form-label">唯一标识:</label>
				<div class="col-xs-10">
					<input id="uuid" name="uuid" class="form-control" type="" value="${context.doc.uuid}"
						id="example-text-input">
				</div>
			</div>
		
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">文件名称:</label>
				<div class="col-xs-10">
					<input id="filename" name="filename" class="form-control" type="text" value="${context.doc.name}"
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">标题:</label>
				<div class="col-xs-10">
					<input id="title" name="title" class="form-control" type="text"  value="${context.doc.title}"
						id="example-text-input">
				</div>
			</div>
			
			
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">关键字:</label>
				<div class="col-xs-10">
					<input id="keywords" name="keywords" class="form-control" type="text"  value="${context.doc.keywords}"
						id="example-text-input">
				</div>
			</div>
			
			<div class="form-group row">
				<label for="example-text-input" class="col-xs-2 col-form-label">描述:</label>
				<div class="col-xs-10">
					<input id="description" name="description" class="form-control" type="text"   value="${context.doc.description}"
						id="example-text-input">
				</div>
			</div>
			
		
			
			
			
			<!--
					<div class="form-group row">
						<label for="example-text-input" class="col-xs-2 col-form-label">描述:</label>
						<div class="col-xs-10">
							<input id="description" name="description" class="form-control" type="text"   value="${context.doc.description}"
								id="example-text-input">
						</div>
					</div>
			
					<div class="form-group row">
						<label for="example-text-input" class="col-xs-2 col-form-label">来源:</label>
						<div class="col-xs-10">
							<input id="description" name="origin" class="form-control" type="text"   value="${context.doc.origin}"
								id="example-text-input">
						</div>
					</div>
			 -->
			<!--
					<div class="form-group row">
						<label for="example-text-input" class="col-xs-2 col-form-label">分辨率:</label>
						<div class="col-xs-10">
							<input id="description" class="form-control" type="text"   value="${context.doc.resolution}"
								id="example-text-input">
						</div>
					</div>
			-->
			
			
		
			
			<div class="form-group row">
		      <div class="offset-sm-2 col-sm-10">
		        <button type="submit" class="btn btn-primary">Update</button>
		      </div>
		    </div>
			
		</form>

	</div>

	  <!-- include  footer -->
   <#include "../footer.ftl">
</body>

<script src="/static/js/jquery/jquery.min.js"></script>
<script
	src="/static/js/bootstrap/dist/js/tether.min.js"
	></script>

<script src="/static/js/bootstrap/dist/js/bootstrap.js"></script>

<!--  Submit the link address to the search engine -->
<#include "../linkpush.ftl">
</html>
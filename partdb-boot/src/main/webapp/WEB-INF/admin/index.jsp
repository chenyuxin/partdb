<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<!DOCTYPE html> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<base href="<%=basePath%>">

<script src="static/index.js" type="module"></script>

<title>test-partdb-jsp</title>
</head>
<body>
<h2>Hello World! Part Data Base v0.0.1</h2>
<br>
<textarea style="width:100%" name="sql" id="sql"  rows="10" placeholder="SQL TEXT"></textarea>
<button id="button">执行</button>







</body>
</html>

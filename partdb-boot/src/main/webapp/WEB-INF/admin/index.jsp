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



<title>test-partdb-jsp</title>
</head>
<body>
<div id='root'></div>
<br>
<textarea style="width:100%" name="sql" id="sql"  rows="10" placeholder="SQL TEXT"></textarea>
<button id="button">执行</button>





<script src="static/common/babel.min.js"></script>
<script src="static/common/react.production.min.js"></script>
<script src="static/common/react-dom.production.min.js"></script>
<!-- 
<script src="static/common/react.development.js"></script>
<script src="static/common/react-dom.development.js"></script>
 -->


<script src="static/index.js" type="module"></script>
<script src="static/react.jsx" type="text/babel"></script>
</body>
</html>

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
<h2>Hello World! Part Data Base v0.0.1</h2>
<br>
<textarea style="width:100%" name="sql" id="sql"  rows="10" placeholder="SQL TEXT"></textarea>
<button onclick="executeSql()">执行</button>








<script type="text/javascript">

const HTTP = new XMLHttpRequest();
const url = 'http://localhost:8765/partdb/executesql';

function executeSql(){
	let sql = document.getElementById('sql').value;
	
	HTTP.open('POST',url);
	HTTP.setRequestHeader('Content-Type', 'application/json');
	//HTTP.send("{'sql' : '" + sql + "' }");
	
	data = {sql: sql };
	
	HTTP.send(JSON.stringify(data));
	
	HTTP.onreadystatechange = function(){
		if(this.readyState===4 && this.status === 200) {
			console.log(HTTP.responseText);
		}
	}
	
	HTTP.close;
}

</script>
</body>
</html>

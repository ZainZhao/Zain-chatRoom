<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>Chat Room</title>
	<!-- 引入CSS文件 -->
	<link rel="stylesheet" type="text/css" href="ext4/resources/css/ext-all.css">
	<link rel="stylesheet" type="text/css" href="ext4/shared/example.css" />
	<link rel="stylesheet" type="text/css" href="css/websocket.css" />
	
	<script type="text/javascript" src="ext4/ext-all-debug.js"></script>
	<script type="text/javascript" src="websocket.js"></script>
	<script type="text/javascript">
		var user = "FH110";
		function setUser(){
			var guser = document.getElementById("user").value;
			if(guser == ''){
				alert("请输入昵称");
			}else{
				user = guser;
				creatw();
			}
		}
	</script>
</head>

<body style="background-image: url(images/bg.jpg)">
	<div id="websocket_button"></div>
	<div style="margin-left: 30px; margin-top: 300px">
		昵称：<input style="background-color: transparent;" name="user" id="user"
			type="text" value="" />
	</div>
	<div style="margin-left: 95px; margin-top: 30px">
		<button style="background-color: transparent;" onclick="setUser();">进入聊天室</button>
	</div>
</body>
</html>

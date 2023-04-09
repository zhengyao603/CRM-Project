<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"; %>
<html>

<head>
	<meta charset="UTF-8">
	<base href="<%=basePath%>">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">

		$(function () {
			// 实现回车登陆
			$(window).keydown(function (event) {
				if (event.keyCode == 13) {
					$("#loginBtn").click();
					alert("回车键");
				}
			});

			// 登陆按钮功能
			$("#loginBtn").click(function () {
				// 获取用户名密码相关输入
				var loginAct = $.trim($("#loginAct").val());
				var loginPwd = $.trim($("#loginPwd").val());
				var isRemPwd = $("#isRemPwd").prop("checkbox");

				// 判断用户名密码是否为空
				if (loginAct == "") {
					alert("用户名不能为空");
					return;
				}
				if (loginPwd == "") {
					alert("密码不能为空");
					return;
				}

				// 发送异步请求
				$.ajax({
					url:"settings/qx/user/doLogin.do",
					data:{
						loginAct:loginAct,
						loginPwd:loginPwd,
						isRemPwd:isRemPwd
					},
					type:"post",
					dataType:"json",
					// 若登录成功
					success: function (data) {
						if (data.code == "1") {
							window.location.href = "workbench/index.do";
						} else {
							$("#msg").text(data.message);
						}
					},
					// 等待验证提示信息
					beforeSend: function () {
						$("#msg").text("正在努力验证...");
					}
				});
			});
		});
	</script>
</head>

<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;</div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" id="loginAct" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" id="loginPwd" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<input type="checkbox" id="isRemPwd"> 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg" style="color: red"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>

</html>
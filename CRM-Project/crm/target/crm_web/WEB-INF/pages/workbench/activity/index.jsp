<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"; %>

<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	// 分页显示数据
	function queryActivityByConditionForPage(pageNo, pageSize) {
		// 收集参数
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		// var pageNo = 1;
		// var pageSize = 10;

		// 发送ajax异步请求
		$.ajax({
			url: 'workbench/activity/queryActivityByConditionForPage.do',
			data: {
				"name": name,
				"owner": owner,
				"startDate": startDate,
				"endDate": endDate,
				"pageNo": pageNo,
				"pageSize": pageSize
			},
			type: 'post',
			dataType: 'json',
			success: function (data) {
				// 显示总条数
				$("#totalRowsB").text(data.totalRows);
				// 显示市场活动数据列表
				var htmlStr = "";
				$.each(data.activityList, function (index, obj) {
					htmlStr += "<tr class=\"active\">";
					htmlStr += "<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
					htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do?id="+obj.id+"'\">"+ obj.name+ "</a></td>";
					htmlStr += "<td>"+obj.owner+"</td>";
					htmlStr += "<td>"+obj.startDate+"</td>";
					htmlStr += "<td>"+obj.endDate+"</td>";
					htmlStr += "</tr>";
				});
				$("#tBody").html(htmlStr);

				//计算总页数
				var totalPages = 1;
				if (data.totalRows % pageSize == 0) {
					totalPages = data.totalRows / pageSize;
				} else {
					totalPages = parseInt(data.totalRows / pageSize) + 1;
				}

				//对容器调用bs_pagination工具函数，显示翻页信息
				$("#demo_pag1").bs_pagination({
					currentPage: pageNo, //当前页号,相当于pageNo

					rowsPerPage: pageSize, //每页显示条数,相当于pageSize
					totalRows: data.totalRows, //总条数
					totalPages: totalPages,  //总页数,必填参数.

					visiblePageLinks: 5, //最多可以显示的卡片数

					showGoToPage: true, //是否显示"跳转到"部分,默认true--显示
					showRowsPerPage: true, //是否显示"每页显示条数"部分。默认true--显示
					showRowsInfo: true, //是否显示记录的信息，默认true--显示

					// 用户每次切换页号，都自动触发本函数;
					// 每次返回切换页号之后的pageNo和pageSize
					onChangePage: function(event, pageObj) { // returns page_num and rows_per_page after a link has clicked
						// js代码
						// alert(pageObj.currentPage);
						// alert(pageObj.rowsPerPage);
						queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});
			}
		});
	}

	$(function(){
		//当市场活动主页面加载完成，查询所有数据的第一页以及所有数据的总条数,默认每页显示10条
		queryActivityByConditionForPage(1, 10);

		//给"查询"按钮添加单击事件
		$("#queryActivityBtn").click(function () {
			//查询所有符合条件数据的第一页以及所有符合条件数据的总条数;
			queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});

		//给"全选"按钮添加单击事件
		$("#checkAll").click(function () {
			//如果"全选"按钮是选中状态，则列表中所有checkbox都选中
			/*if(this.checked==true){
				$("#tBody input[type='checkbox']").prop("checked",true);
			}else{
				$("#tBody input[type='checkbox']").prop("checked",false);
			}*/

			$("#tBody input[type='checkbox']").prop("checked", this.checked);
		});

		/*$("#tBody input[type='checkbox']").click(function () {
			//如果列表中的所有checkbox都选中，则"全选"按钮也选中
			if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
				$("#chckAll").prop("checked",true);
			}else{//如果列表中的所有checkbox至少有一个没选中，则"全选"按钮也取消
				$("#chckAll").prop("checked",false);
			}
		});*/
		$("#tBody").on("click", "input[type='checkbox']", function () {
			//如果列表中的所有checkbox都选中，则"全选"按钮也选中
			if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()){
				$("#checkAll").prop("checked",true);
			} else {//如果列表中的所有checkbox至少有一个没选中，则"全选"按钮也取消
				$("#checkAll").prop("checked",false);
			}
		});

		$("#deleteActivityBtn").click(function () {
			//获取列表中所有被选中的checkbox
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if (checkedIds.size() == 0){
				alert("请选择要删除的市场活动");
				return;
			}

			if (window.confirm("确定删除吗？")){
				var ids = "";
				$.each(checkedIds, function () {
					ids += "id=" + this.value + "&";
				});
				ids = ids.substr(0, ids.length-1);
				alert(ids);

				//发送请求
				$.ajax({
					url: 'workbench/activity/deleteActivityByIds.do',
					data: ids,
					type: 'post',
					dataType: 'json',
					success:function (data) {
						if(data.code == "1"){
							//刷新市场活动列表,显示第一页数据,保持每页显示条数不变
							queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
						}else{
							//提示信息
							alert(data.message);
						}
					}
				});
			}
		});

		// 给创建按钮添加单击事件
		$("#createActivityBtn").click(function () {
			// 重置表单
			$("#createActivityForm").get(0).reset();
			// 显示模态窗口
			$("#createActivityModal").modal("show");
		});

		// 配置日历选择器
		$(".date-pick").datetimepicker({
			language: "zh-CN",
			format: "yyyy-mm-dd",
			minView: "month",
			initialDate: new Date(),
			autoclose: true,
			todayBtn: true,
			clearBtn: true
		});

		// 给保存按钮添加单击事件
		$("#activitySaveBtn").click(function () {
			// 获取表单数据
			var owner = $("#create-marketActivityOwner").val();
			var name = $("#create-marketActivityName").val();
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $("#create-cost").val();
			var description = $("#create-describe").val();

			// 表单非空验证
			if (owner == "") {
				alert("所有者不能为空！");
				return;
			}
			if (name == "") {
				alert("名称不能为空！");
				return;
			}

			// 判断终止时间是否大于起始时间
			if (startDate != "" && endDate != "") {
				if (endDate < startDate) {
					alert("起始时间不能大于终止时间！");
					return
				}
			}

			/*
			  正则表达式：
			     1，语言，语法：定义字符串的匹配模式，可以用来判断指定的具体字符串是否符合匹配模式。
			     2,语法通则：
			       1)//:在js中定义一个正则表达式.  var regExp=/...../;
			       2)^：匹配字符串的开头位置
			         $: 匹配字符串的结尾
			       3)[]:匹配指定字符集中的一位字符。 var regExp=/^[abc]$/;
			                                    var regExp=/^[a-z0-9]$/;
			       4){}:匹配次数.var regExp=/^[abc]{5}$/;
			            {m}:匹配m此
			            {m,n}：匹配m次到n次
			            {m,}：匹配m次或者更多次
			       5)特殊符号：
			         \d:匹配一位数字，相当于[0-9]
			         \D:匹配一位非数字
			         \w：匹配所有字符，包括字母、数字、下划线。
			         \W:匹配非字符，除了字母、数字、下划线之外的字符。

			         *:匹配0次或者多次，相当于{0,}
			         +:匹配1次或者多次，相当于{1,}
			         ?:匹配0次或者1次，相当于{0,1}
			 */
			// 判断成本是否为非负整数
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)) {
				alert("成本不能为负数或小数！");
				return;
			}

			// 发送ajax异步请求
			$.ajax({
				url: "workbench/activity/saveCreateActivity.do",
				data: {
					"owner": owner,
					"name": name,
					"startDate": startDate,
					"endDate": endDate,
					"cost": cost,
					"description": description
				},
				type:"post",
				dataType:"json",
				success: function (data) {
					if (data.code == "1") {
						// 关闭模态窗口
						$("#createActivityModal").modal("hide");
						// 刷新市场活动数据
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'), $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'))
					} else {
						// 提示错误信息
						alert(data.message);
					}
				}
			});
		});

		// 给"修改"按钮添加单击事件
		$("#editActivityBtn").click(function () {
			// 收集参数
			// 获取列表中被选中的checkbox
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if (checkedIds.size() == 0) {
				alert("请选择要修改的市场活动");
				return;
			}
			if (checkedIds.size() > 1){
				alert("每次只能修改一条市场活动");
				return;
			}
			// var id = checkedIds.val();
			// var id = checkedIds.get(0).value;
			var id = checkedIds[0].value;
			// 发送请求
			$.ajax({
				url: 'workbench/activity/queryActivityById.do',
				data: {
					id: id
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					// 把市场活动的信息显示在修改的模态窗口上
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);
					// 弹出模态窗口
					$("#editActivityModal").modal("show");
				}
			});
		});

		$("#saveEditActivityBtn").click(function () {
			// 获取更新后的数据
			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var name = $("#edit-marketActivityName").val();
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var cost = $("#edit-cost").val();
			var description = $("#edit-description").val();

			// 表单非空验证
			if (owner == "") {
				alert("所有者不能为空！");
				return;
			}
			if (name == "") {
				alert("名称不能为空！");
				return;
			}

			// 判断终止时间是否大于起始时间
			if (startDate != "" && endDate != "") {
				if (endDate < startDate) {
					alert("起始时间不能大于终止时间！");
					return
				}
			}

			// 判断成本是否为非负整数
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)) {
				alert("成本不能为负数或小数！");
				return;
			}

			// 发送ajax异步请求
			$.ajax({
				url: "workbench/activity/saveEditActivity.do",
				data: {
					id: id,
					owner: owner,
					name: name,
					startDate: startDate,
					endDate: endDate,
					cost: cost,
					description: description
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if(data.code == "1"){
						// 关闭模态窗口
						$("#editActivityModal").modal("hide");
						// 刷新市场活动列表,保持页号和每页显示条数都不变
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'), $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						// 提示信息
						alert(data.message);
						// 模态窗口不关闭
						$("#editActivityModal").modal("show");
					}
				}
			});
		});

		$("#exportActivityAllBtn").click(function () {
			window.location.href='workbench/activity/exportAllActivities.do';
		});

		$("#importActivityBtn").click(function () {
			// 判断是否为xls文件
			var activityFileName = $("#activityFile").val();
			var suffix = activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLowerCase();
			if (suffix != "xls") {
				alert("只支持xls文件");
				return;
			}
			// 判断是否大于5MB
			var activityFile = $("#activityFile")[0].files[0];
			if (activityFile > 1024*1024*5) {
				alert("文件大小不超过5MB");
				return;
			}

			// FormData是ajax提供的接口,可以模拟键值对向后台提交参数;
			// FormData最大的优势是不但能提交文本数据，还能提交二进制数据
			var formData = new FormData();
			formData.append("activityFile", activityFile);

			$.ajax({
				url: 'workbench/activity/importActivities.do',
				data: formData,
				type: 'post',
				dataType: 'json',
				processData: false, //设置ajax向后台提交参数之前，是否把参数统一转换成字符串：true--是,false--不是,默认是true
				contentType: false, //设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码：true--是,false--不是，默认是true
				success:function (data) {
					if (data.code == "1"){
						//提示成功导入记录条数
						alert("成功导入" + data.retData + "条记录");
						//关闭模态窗口
						$("#importActivityModal").modal("hide");
						//刷新市场活动列表,显示第一页数据,保持每页显示条数不变
						queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						//提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#importActivityModal").modal("show");
					}
				}
			});
		});
	});
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="u">
									  <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control date-pick" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control date-pick" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="activitySaveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name" >
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner" >
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate" >
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody"></tbody>
					<tbody id="tBody"></tbody>
				</table>
				<div id="demo_pag1"></div>
			</div>
		</div>
	</div>
</body>
</html>
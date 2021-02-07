<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>


<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">

		$(function () {
			//窗口加载完毕，添加时间控件
			$(".time").datetimepicker({
				minView: "month",
				language: 'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			//添加按钮 点击事件
			$("#addBtn").click(function () {
				// 后端取user 数据
				$.ajax({
					url: "workbench/activity/getUserList.do",
					data: {},
					type: "get",
					dataType: "json",
					success: function (data) {
						/*
                            data: [{用户1},{用户2},{用户3}...]
                         */
						var html = "<option></option>";
						$.each(data, function (i, e) {
							html += "<option value='" + e.id + "'>" + e.name + "</option>"
						});
						$("#create-marketActivityOwner").html(html);

						//将当前登陆的用户设为下拉框默认
						//在js 中，使用el表达式，el表达式一定要套用在字符串中
						var id = "${user.id}";
						$("#create-marketActivityOwner").val(id);

						// 处理完数据 展现模态窗口
						$("#createActivityModal").modal("show");
					}
				});
			});

			// 保存按钮单击事件，执行添加操作
			$("#saveBtn").click(function () {
				$.ajax({
					url: "workbench/activity/save.do",
					data: {
						"owner": $.trim($("#create-marketActivityOwner").val()),
						"name": $.trim($("#create-marketActivityName").val()),
						"startDate": $.trim($("#create-startTime").val()),
						"endDate": $.trim($("#create-endTime").val()),
						"cost": $.trim($("#create-cost").val()),
						"description": $.trim($("#create-describe").val())
					},
					type: "post",
					dataType: "json",
					success: function (data) {
						/* data {"success":true/false}
                        **/
						// alert(data.success);
						if (data.success) {
							//添加市场活动成功后
							//1、刷新市场活动信息列表（局部刷新）
							/*
							* pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							* 第一个参数：停留在当前页
							*  第二个参数：保持每页的记录条数不变
							* */
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							//2、模态窗口内容清空
							$("#activityForm")[0].reset();
							//3、关闭添加操作的模态窗口
							$("#createActivityModal").modal("hide");
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						} else {
							alert("添加市场活动失败");
						}
					}
				});
			});
			//页面加载完成，触发一个方法，刷新市场活动列表
			pageList(1, 6);

			// 给全选框绑定事件
			$("#qx").click(function () {
				$("input[name=xz]").prop("checked", this.checked);
			});
			//单选框 操作复选框 给单选框绑定事件

			/*$("input[name=xz]").click(function () {
                    动态生成的元素，不能以普通元素处理
            });*/
			/*
                动态生成的元素，需要用on 方法绑定事件
                $(需要绑定的外层的有效元素).on(绑定事件的方式,需要绑定事件的jquery对象,回调函数)
             */
			$("#activityBody").on("click", $("input[name=xz]"), function () {
				// alert(123);
				$("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);
			});

			//为查询按钮添加点击事件
			$("#searchBtn").click(function () {
				//点击查询，将文本框中内容保存到隐藏域
				$("#hidden-name").val($.trim($("#search-name").val()));
				$("#hidden-owner").val($.trim($("#search-owner").val()));
				$("#hidden-startDate").val($.trim($("#search-startTime").val()));
				$("#hidden-endDate").val($.trim($("#search-endTime").val()));
				pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
			});

			//为删除按钮添加点击事件
			$("#deleteBtn").click(function () {
				//给用户反悔的机会
				if (confirm("确定要做掉吗？")) {
					//获取 将要删除元素的id
					var $xz = $("input[name=xz]:checked");
					if ($xz.length == 0) {
						//没有元素被勾选
						alert("请选择需要删除的记录");
						return false;
					}
					//请求url： workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
					//有元素被勾选， 1条或者多条
					var param = "";
					//拼接参数
					/*for (var i = 0; i < $xz.length; i++) {
                        param +="id="+$($xz[i]).val()+"&";
                    }
                    param = param.substr(0, param.length - 1);*/
					for (var i = 0; i < $xz.length; i++) {
						param += "id=" + $($xz[i]).val();
						if (i<$xz.length - 1) {
							param += "&";
						}
					}
					// alert(param);
					$.ajax({
						url:"workbench/activity/delete.do",
						data: param,
						type: "post",
						dataType:"json",
						success: function (data) {
							//data {"success",true/false}
							if (data.success) {
								//flush
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else alert("删除失败");

						}
					});
				}
			});

			//为修改按钮添加点击事件
			$("#editBtn").click(function () {
				//从后台拿 user 列表 和一个 activity 对象
				// 查询条件 就是 打勾 的id
				var $xz = $("input[name=xz]:checked");
				if ($xz.length == 0) {
					//没打勾
					alert("请选择需要修改的记录");
					return;
				}
				if ($xz.length > 1) {
					//选多了
					alert("每次至多修改一天记录");
					return ;
				}
				// $xz.length ==1  true
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data: {
						"id": $xz.val()
					},
					type: "post",
					dataType:"json",
					success: function (data) {
						/*
						data:{"userList":[{user1},{user2}...],"activity":[{field1},{field2},{field3}...]}
						 */
						var html = "<option></option>";
						$.each(data.userList, function (i,n) {
							html += "<option  value='" + n.id + "'>" + n.name + "</option>";
						});
						$("#edit-marketActivityOwner").html(html);

						//default option
						var id = "${user.id}";
						$("#edit-marketActivityOwner").val(id);

						$("#edit-marketActivityName").val(data.activity.name);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-description").val(data.activity.description);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-id").val(data.activity.id);

						//打开模态窗口
						$("#editActivityModal").modal("show");
					}
				});
			});

			//为更新按钮 添加点击事件
			$("#updateBtn").click(function () {
				$.ajax({
					url: "workbench/activity/update.do",
					data: {
						"id": $.trim($("#edit-id").val()),
						"owner": $.trim($("#edit-marketActivityOwner").val()),
						"name": $.trim($("#edit-marketActivityName").val()),
						"startDate": $.trim($("#edit-startDate").val()),
						"endDate": $.trim($("#edit-endDate").val()),
						"cost": $.trim($("#edit-cost").val()),
						"description": $.trim($("#edit-description").val())
					},
					type: "post",
					dataType: "json",
					success: function (data) {
						/* data {"success":true/false}
                        **/
						// alert(data.success);
						if (data.success) {
							//修改市场活动成功后
							//1、刷新市场活动信息列表（局部刷新）
							// pageList(1, 6);
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							//3、关闭修改操作的模态窗口
							$("#editActivityModal").modal("hide");
						} else {
							alert("修改市场活动失败");
						}
					}
				});
			});

			//为修改模态窗口的 关闭按钮绑定事件
			$("#modalClose").click(function () {
				$("input[name=xz]:checked").prop("checked", false);
				$("#qx").prop("checked", false);
			});
		});
	/*
	* 对于所有关系型数据库，做前端的分页相关的操作的基础组件
	* 	pageNO：当前页码
	* 	pageSize：每页展现的记录条数
	*  	（1） 什么地方调用 pageList
	* 			左侧 市场活动点击时
	* 			添加 修改 模态窗口关闭后
	* 			查询按钮
	* 			首页，尾页 页码
	*
	* */
	function pageList(pageNo, pageSize) {
		//search 文本框从隐藏域中取值
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startTime").val()));
		$("#search-endDate").val($.trim($("#hidden-endTime").val()));

		//reset checkedbox state
		$("#qx").prop("checked", false);

		$.ajax({
			url:"workbench/activity/pageList.do",
			data: {
				"pageNo":pageNo,
				"pageSize": pageSize,
				"name": $.trim($("#search-name").val()),
				"owner": $.trim($("#search-owner").val()),
				"startDate": $.trim($("#search-startTime").val()),
				"endDate": $.trim($("#search-endTime").val())
			},
			type: "get",
			dataType:"json",
			success: function (data) {
				/*
                * 	data [{市场活动1},{市场活动2},{市场活动3}....]
                * 	分页插件 数据总条数
                * 	{"total":100,"dataList": [{市场活动1},{市场活动2},{市场活动3}....] }
                *
                * */

				var html = "";
				$.each(data.dataList, function (i, n) {
					html += '	<tr class="active">';
					html += '	<td><input type="checkbox" name="xz" value="' + n.id + '"/></td>';
					html += '	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\'">' + n.name + '</a></td>';
					html += '	<td>' + n.owner + '</td>';
					html += '	<td>' + n.startDate + '</td>';
					html += '	<td>' + n.endDate + '</td>';
					html += '	</tr>';

				});
				$("#activityBody").html(html);
				// alert(data.total);
				var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
				//数据处理完毕后，结合分页查询，前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 6, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});



			}
		});


	}

</script>
</head>
<body>

	<%--四个搜索框隐藏域--%>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">


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

					<form class="form-horizontal" id="activityForm" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" >
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
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
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
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label time">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" >
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label time">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" id="modalClose" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" id="updateBtn" class="btn btn-primary" data-dismiss="modal">更新</button>
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
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endTime">
				    </div>
				  </div>

				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					&lt;%&ndash;<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>&ndash;%&gt;
				</div>--%>
			</div>

		</div>

	</div>
</body>
</html>
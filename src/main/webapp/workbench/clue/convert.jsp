<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		//页面加载完成 添加时间控件
		$(".time").datetimepicker({
			minView: "month",
			language: 'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//给放大镜绑定事件
		$("#fangdajing").click(function () {
			//打开模态窗口	输入框获取焦点
			$('#searchActivityModal').on('shown.bs.modal', function () {
				$('#queryInput').focus();
			});
			$('#searchActivityModal').modal('show');

		});

		//给queryInput 搜索框绑定 敲回车事件 模糊查询 潜在客户 关联的市场活动列表
		$("#queryInput").keydown(function (e) {
			if (e.keyCode == 13) {
				$.ajax({
					url: "workbench/clue/getActivityClueHave.do",
					data: {
						"pageNo": 1,
						"pageSize": 10,
						"name": $.trim($("#queryInput").val()),
						"clueId":"${param.id}"
					},
					type: "get",
					dataType:"json",
					success: function (data) {
						// data:{[市场活动1],[],][]}
						var html = "";
						$.each(data, function (i, e) {
							html+='	<tr>';
							html+='	<td><input value="'+e.id+'" type="radio" name="xz"/></td>';
							html+='	<td id="t'+e.id+'">'+e.name+'</td>';
							html+='	<td>'+e.startDate+'</td>';
							html+='	<td>'+e.endDate+'</td>';
							html+='	<td>'+e.owner+'</td>';
							html+='	</tr>';
						});
						$("#acmodalTbody").html(html);
					}
				});
				///模态窗口，默认敲回车 会清空当前页面数据 直接return 给这个事件禁用掉 妙！！！

				return false;
			}
		});

		//给 放大镜 模态窗口 中的取消按钮绑定事件
		$("#cancelModalBtn").click(function () {
			$("#searchActivityModal").modal("hide");
		});
		//给 放大镜 模态窗口 中的提交按钮绑定事件
		$("#subActivityBtn").click(function () {
			var id =$("input[name=xz]:checked").val();	// id
			var name = $("#t"+id+"").html();	//市场活动名字
			//名字给到文本框
			$("#activity").val(name);
			//id 给到 隐藏域 转换需要知道 市场活动id
			$("#selectActId").val(id);

			//最后关闭模态窗口
			$("#searchActivityModal").modal("hide");

		});

		//给页面最下角 转换按钮 绑定事件
		$("#convertBtn").click(function () {
			// alert(123);
			/*
				提交请求到后台，执行删除 线索 完了 要给客户表 新增一条记录
				交易表改动取决于 勾不勾 创建交易的框
				最后要返回 clue.jsp 看到结果是 关于 刘东东 的线索 被干掉了
			 */
			if ($("#isCreateTransaction").prop("checked")) {
				//创建交易框 挑勾 了 不仅干掉一条线索， 还要给 交易表添加一条记录
				//workbench/clue/convert.do?clueId=xxx&....
				$("#convertForm").submit();

			} else {
				// 创建交易框没挑勾 直接干掉一条线索就行了，传个clueID
				//workbench/clue/convert.do?clueId=xxx&....
				window.location.href = "workbench/clue/convert.do?clueId=${param.id}";

			}


		});
	});
</script>

</head>
<body>

	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="queryInput" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="acmodalTbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" id="cancelModalBtn">取消</button>
						<button type="button" id="subActivityBtn" class="btn btn-primary" >提交</button>
					</div>
				</div>

			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<form id="convertForm" method="post" action="workbench/clue/convert.do">
			<%--隐藏域 标识 线索 id--%>
			<input type="hidden" value="${param.id}" name="clueId">
<%--				用于标记是否需要 创建交易--%>
				<input type="hidden" value="flag" name="flag">

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
				<c:forEach var="stagev" items="${stage}">
					<option value="${stagev.value}">${stagev.text}</option>
				</c:forEach>

		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="fangdajing" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity"  placeholder="点击上面搜索" readonly name="source">
			  <input type="hidden" id="selectActId" name="activityId">
			  <input type="hidden" value="${param.owner}" name="owner">

		  </div>
		</form>

	</div>

	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" onclick="window.location.href='workbench/clue/detail.do?id=${param.id}'" value="取消">
	</div>
</body>
</html>
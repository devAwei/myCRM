<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
</head>
<script>
    $.ajax({
        url:"",
        data: {},
        type: "",
        dataType:"json",
        success: function (data) {

        }
    });


    //打开模态窗口	输入框获取焦点
    $('#bundModal').on('shown.bs.modal', function () {
        $('#queryInput').focus();
    });
    $('#bundModal').modal('show');

    //页面加载完成 添加时间控件
    $(".time").datetimepicker({
        minView: "month",
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
    });
</script>
<body>

        //创建时间
        String createTime = DateTimeUtil.getSysTime();
        ac.setCreateTime(createTime);
        String name = ((User) request.getSession().getAttribute("user")).getName();
        ac.setCreateBy(name);


</body>
</html>

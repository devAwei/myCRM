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
</script>
<body>

        //创建时间
        String createTime = DateTimeUtil.getSysTime();
        ac.setCreateTime(createTime);
        String name = ((User) request.getSession().getAttribute("user")).getName();
        ac.setCreateBy(name);


</body>
</html>

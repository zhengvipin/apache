<%--
  Created by IntelliJ IDEA.
  User: Zheng
  Date: 2018\4\10 0010
  Time: 20:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Car List</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body>
    <div class="container text-center">
        <h3>小汽车列表</h3>
        <table class="table table-hover table-striped table-bordered">
            <thead>
                <tr>
                    <th>序号</th>
                    <th>车名</th>
                    <th>价格</th>
                    <th>生产日期</th>
                    <th>图片路径</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${cars}" var="car" varStatus="rows">
                <tr>
                    <td>${rows.index + 1}</td>
                    <td>${car.name}</td>
                    <fmt:formatNumber value="${car.price}" var="price" pattern="######.00"/>
                    <td>${price}</td>
                    <fmt:formatDate value="${car.createDate}" var="createDate" pattern="yyyy-MM-dd"/>
                    <td>${createDate}</td>
                    <td>${car.img}</td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
        <a class="btn btn-outline-primary" id="btnExport">生成xml表格</a>
    </div>
<script src="${pageContext.request.contextPath}/static/js/jquery.2.1.4.min.js"></script>
<script>
    $('#btnExport').click(()=>{
        if(confirm("确认要生成xml表格么？")){
            location.href = "/download.action";
        }
    })
</script>
</body>
</html>

    
    
    
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
<style>
ul.pagination {
    display: inline-block;
    padding: 0;
    margin: 0;
}

ul.pagination li {display: inline;}

ul.pagination li a {
    color: black;
    float: left;
    padding: 8px 16px;
    text-decoration: none;
    transition: background-color .3s;
    border: 1px solid #ddd;
}

ul.pagination li a.active {
    background-color: #4CAF50;
    color: white;
    border: 1px solid #4CAF50;
}

ul.pagination li a:hover:not(.active) {background-color: #ddd;}
</style>
</head>
<body>
	<%@ include file="VM_Backend_FunMenu.jsp" %>
	<h2>商品列表</h2><br/>
	<div style="margin-left:25px;">
	<p style="color:blue;">${sessionScope.deleteMsg}</p>
	<% session.removeAttribute("deleteMsg"); %>
	<table border="1">
		<tbody>
			<tr height="50" align="center">
				<td width="75"><b>商品編號</b></td> 
				<td width="200"><b>商品名稱</b></td> 
				<td width="100"><b>商品價格</b></td>
				<td width="100"><b>現有庫存</b></td>
				<td width="100"><b>商品狀態</b></td>
				<td width="100"><b>商品刪除</b></td>
			</tr>
			<c:forEach items="${goods}" var="good">
				<tr height="30" align="center">
					<td>${good.goodsID}</td> 
					<td>${good.goodsName}</td>
					<td>${good.goodsPrice}</td>
					<td>${good.goodsQuantity}</td>
					<td>
						<c:if test="${good.status eq '1'}">
								<font style="color:blue;">上架</font>
							</c:if>
						<c:if test="${good.status eq '0'}">
								<font style="color:red;">下架</font>
							</c:if>
						</td>
					<td>
						<c:url value="/BackendServlet.do" var="deleUrl">
					        <c:param name="action" value="deleteGoods"/>
					        <c:param name="id" value="${good.goodsID}"/>
					        <c:param name="goodsImageName" value="${good.goodsImageName}"/>
					    </c:url>
						<a href="${deleUrl}">刪除</a>
					</td>
				</tr>			
			</c:forEach>
		</tbody>
	</table>
		<ul class="pagination">	
			<c:url value="BackendServlet.do" var="pageUrl">
				<c:param name="action" value="queryGoods"/>
				<c:param name="jessionid" value="${jessionid}"/>
			</c:url>
			<c:if test="${1 ne param.pageNo }">
				<c:url value="${pageUrl}" var="previousPageUrl">
					<c:param name="pageNo" value="${param.pageNo-1}"/>
				</c:url>
				<li> <a href="${previousPageUrl}"> 上一頁 </a> </li>
			</c:if>
			<c:if test="${1 eq param.pageNo }">
				<li> <a href="#"> 上一頁 </a> </li>
			</c:if>
			<c:forEach items="${pageGoods.pagination}" var="pageNo">
				<c:url value="${pageUrl}" var="assignPageUrl">
					<c:param name="pageNo" value="${pageNo}" />
				</c:url>
				<li> 
					<a href="${assignPageUrl}" <c:if test="${pageNo eq param.pageNo}">style="color:red;"</c:if> >${pageNo}</a> 
				</li>
			</c:forEach>
			<c:if test="${param.pageNo ne pageGoods.endPageNo }">
				<c:url value="${pageUrl}" var="nextPageUrl">
					<c:param name="pageNo" value="${param.pageNo+1}"/>
				</c:url>
				<li> <a href="${nextPageUrl}"> 上一頁 </a> </li>
			</c:if>
			<c:if test="${param.pageNo eq pageGoods.endPageNo }">
				<li> <a href="#" > 上一頁 </a> </li>
			</c:if>
		</ul>
	</div>
</body>
</html>
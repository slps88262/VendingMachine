<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
</head>
<body>
	<%@ include file="VM_Backend_FunMenu.jsp" %>
	<h2>銷售報表</h2><br/>
	<div style="margin-left:25px;">
	<form action="BackendServlet.do" method="get">
		<input type="hidden" name="action" value="querySalesReport"/>
		起 &nbsp; <input type="date" value="${param.queryStartDate}" name="queryStartDate" style="height:25px;width:180px;font-size:16px;text-align:center;"/>
		&nbsp;
		迄 &nbsp; <input type="date" value="${param.queryEndDate}" name="queryEndDate" style="height:25px;width:180px;font-size:16px;text-align:center;"/>	
		<input type="submit" value="查詢" style="margin-left:25px; width:50px;height:32px"/>
	</form>
	<br/>
	<table border="1">
		<tbody>
			<tr height="50">
				<td width="100"><b>訂單編號</b></td>
				<td width="100"><b>顧客姓名</b></td>
				<td width="100"><b>購買日期</b></td>
				<td width="125"><b>飲料名稱</b></td> 
				<td width="100"><b>購買單價</b></td>
				<td width="100"><b>購買數量</b></td>
				<td width="100"><b>購買金額</b></td>
			</tr>
				<c:forEach items="${SalesReports}" var="SalesReport">
					<tr height="30">
						<td>${SalesReport.orderID}</td>
						<td>${SalesReport.customerName}</td>
						<td>${SalesReport.orderDate}</td>
						<td>${SalesReport.goodsName}</td>
						<td>${SalesReport.goodsBuyPrice}</td> 
						<td>${SalesReport.buyQuantity}</td>
						<td>${SalesReport.buyAmount}</td>	
					</tr>
				</c:forEach>
		</tbody>
	</table>
	</div>

</body>
</html>